package com.anje.kelvin.aconting.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anje.kelvin.aconting.data.database.entities.Category
import com.anje.kelvin.aconting.data.database.entities.Expense
import com.anje.kelvin.aconting.data.repository.ExpenseRepository
import com.anje.kelvin.aconting.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class ExpenseUiState(
    val currentUserId: Long = 0L,
    val description: String = "",
    val amount: String = "",
    val selectedCategory: Category? = null,
    val availableCategories: List<Category> = emptyList(),
    val recurrence: String = "Nenhuma",
    val startDate: Date = Date(),
    val endDate: Date? = null,
    val notes: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    val recurrenceOptions = listOf("Nenhuma", "Fixa")

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            // Load current user
            authRepository.currentUser.collect { user ->
                _uiState.update { it.copy(currentUserId = user?.id ?: 0L) }
            }
        }
        
        // Load available categories
        viewModelScope.launch {
            expenseRepository.getAllCategories().collect { categories ->
                _uiState.update { state ->
                    state.copy(
                        availableCategories = categories,
                        selectedCategory = if (state.selectedCategory == null) 
                            categories.find { it.name == "Despesa Geral" } 
                            else state.selectedCategory
                    )
                }
            }
        }
    }

    fun updateDescription(description: String) {
        _uiState.update { it.copy(description = description, error = null) }
    }

    fun updateAmount(amount: String) {
        _uiState.update { it.copy(amount = amount, error = null) }
    }

    fun updateCategory(category: Category) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun updateRecurrence(recurrence: String) {
        _uiState.update { it.copy(recurrence = recurrence) }
    }

    fun updateStartDate(date: Date) {
        _uiState.update { it.copy(startDate = date) }
    }

    fun updateEndDate(date: Date?) {
        _uiState.update { it.copy(endDate = date) }
    }

    fun updateNotes(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun saveExpense() {
        viewModelScope.launch {
            val state = _uiState.value
            
            // Validation
            if (state.description.isBlank()) {
                _uiState.update { it.copy(error = "Descrição é obrigatória") }
                return@launch
            }
            
            if (state.amount.isBlank()) {
                _uiState.update { it.copy(error = "Valor é obrigatório") }
                return@launch
            }
            
            val amountValue = state.amount.toDoubleOrNull()
            if (amountValue == null || amountValue <= 0) {
                _uiState.update { it.copy(error = "Valor deve ser um número positivo") }
                return@launch
            }

            if (state.currentUserId <= 0) {
                _uiState.update { it.copy(error = "Usuário não identificado. Faça login para continuar.") }
                return@launch
            }

            if (state.selectedCategory == null) {
                _uiState.update { it.copy(error = "Selecione uma categoria") }
                return@launch
            }
            
            if (state.recurrence == "Fixa" && state.endDate == null) {
                _uiState.update { it.copy(error = "Data de fim é obrigatória para despesas fixas") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val result = expenseRepository.createExpense(
                    userId = state.currentUserId,
                    amount = amountValue,
                    description = state.description,
                    category = state.selectedCategory.name,
                    expenseDate = state.startDate,
                    notes = state.notes.takeIf { it.isNotBlank() }
                )
                
                if (result.isSuccess) {
                    _uiState.update { 
                        ExpenseUiState(
                            currentUserId = state.currentUserId,
                            availableCategories = state.availableCategories,
                            selectedCategory = state.availableCategories.find { it.name == "Despesa Geral" },
                            success = true
                        )
                    }
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = result.exceptionOrNull()?.message ?: "Erro ao salvar despesa"
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Erro ao salvar despesa: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(success = false) }
    }

    fun reset() {
        val currentUserId = _uiState.value.currentUserId
        val categories = _uiState.value.availableCategories
        _uiState.value = ExpenseUiState(
            currentUserId = currentUserId,
            availableCategories = categories,
            selectedCategory = categories.find { it.name == "Despesa Geral" }
        )
    }
}