package com.anje.kelvin.aconting.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anje.kelvin.aconting.data.repository.DataClearingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataClearingViewModel @Inject constructor(
    private val dataClearingRepository: DataClearingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DataClearingUiState())
    val uiState: StateFlow<DataClearingUiState> = _uiState.asStateFlow()

    fun updateSelectedOptions(option: ClearDataOption, isSelected: Boolean) {
        val currentOptions = _uiState.value.selectedOptions.toMutableSet()
        if (isSelected) {
            currentOptions.add(option)
        } else {
            currentOptions.remove(option)
        }
        _uiState.value = _uiState.value.copy(selectedOptions = currentOptions)
    }

    fun clearData(userId: Long) {
        val selectedOptions = _uiState.value.selectedOptions

        if (selectedOptions.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please select at least one data type to clear"
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = "",
            successMessage = ""
        )

        viewModelScope.launch {
            try {
                var hasError = false

                if (selectedOptions.contains(ClearDataOption.ALL)) {
                    val result = dataClearingRepository.clearAllData(userId)
                    if (result.isFailure) hasError = true
                } else {
                    if (selectedOptions.contains(ClearDataOption.SALES)) {
                        val result = dataClearingRepository.clearAllSales(userId)
                        if (result.isFailure) hasError = true
                    }
                    if (selectedOptions.contains(ClearDataOption.EXPENSES)) {
                        val result = dataClearingRepository.clearAllExpenses(userId)
                        if (result.isFailure) hasError = true
                    }
                    if (selectedOptions.contains(ClearDataOption.DEPOSITS)) {
                        val result = dataClearingRepository.clearAllDeposits(userId)
                        if (result.isFailure) hasError = true
                    }
                    if (selectedOptions.contains(ClearDataOption.TRANSACTIONS)) {
                        val result = dataClearingRepository.clearAllTransactions()
                        if (result.isFailure) hasError = true
                    }
                    if (selectedOptions.contains(ClearDataOption.PRODUCTS)) {
                        val result = dataClearingRepository.clearAllProducts()
                        if (result.isFailure) hasError = true
                    }
                }

                _uiState.value = if (hasError) {
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Some data could not be cleared",
                        selectedOptions = emptySet()
                    )
                } else {
                    _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Data cleared successfully",
                        selectedOptions = emptySet()
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}",
                    selectedOptions = emptySet()
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = "",
            successMessage = ""
        )
    }
}

data class DataClearingUiState(
    val selectedOptions: Set<ClearDataOption> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = ""
)

enum class ClearDataOption {
    SALES,
    EXPENSES,
    DEPOSITS,
    TRANSACTIONS,
    PRODUCTS,
    ALL
}
