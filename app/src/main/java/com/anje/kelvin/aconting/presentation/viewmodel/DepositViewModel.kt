package com.anje.kelvin.aconting.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anje.kelvin.aconting.data.database.entities.Category
import com.anje.kelvin.aconting.data.database.entities.Deposit
import com.anje.kelvin.aconting.data.repository.DepositRepository
import com.anje.kelvin.aconting.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class DepositUiState(
    val currentUserId: Long = 0L,
    val description: String = "",
    val amount: String = "",
    val date: Date = Date(),
    val selectedCategory: Category? = null,
    val availableCategories: List<Category> = emptyList(),
    val notes: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val depositRepository: DepositRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DepositUiState())
    val uiState: StateFlow<DepositUiState> = _uiState.asStateFlow()

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
            depositRepository.getDepositCategories(1L).collect { categories ->
                _uiState.update { state ->
                    state.copy(
                        availableCategories = categories,
                        selectedCategory = if (state.selectedCategory == null) 
                            categories.find { it.name == "Receita Geral" } 
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

    fun updateDate(date: Date) {
        _uiState.update { it.copy(date = date) }
    }

    fun updateCategory(category: Category) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun updateNotes(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun saveDeposit() {
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

            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val result = depositRepository.createDeposit(
                    userId = state.currentUserId,
                    amount = amountValue,
                    description = state.description,
                    category = state.selectedCategory.name,
                    depositDate = state.date,
                    notes = state.notes.takeIf { it.isNotBlank() }
                )
                
                if (result.isSuccess) {
                    _uiState.update { 
                        DepositUiState(
                            currentUserId = state.currentUserId,
                            availableCategories = state.availableCategories,
                            selectedCategory = state.availableCategories.find { it.name == "Receita Geral" },
                            success = true
                        )
                    }
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = result.exceptionOrNull()?.message ?: "Erro ao salvar depósito"
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Erro ao salvar depósito: ${e.message}"
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
        _uiState.value = DepositUiState(
            currentUserId = currentUserId,
            availableCategories = categories,
            selectedCategory = categories.find { it.name == "Receita Geral" }
        )
    }
}