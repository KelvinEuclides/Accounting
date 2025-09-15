package com.anje.kelvin.aconting.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anje.kelvin.aconting.data.database.entities.Transaction
import com.anje.kelvin.aconting.data.database.entities.TransactionType
import com.anje.kelvin.aconting.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SimpleTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _filteredTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val filteredTransactions: StateFlow<List<Transaction>> = _filteredTransactions.asStateFlow()

    private val _selectedFilter = MutableStateFlow("all")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            transactionRepository.getAllTransactions().collect { transactionList ->
                _transactions.value = transactionList
                applyFilter(_selectedFilter.value)
            }
        }
    }

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
        applyFilter(filter)
    }

    private fun applyFilter(filter: String) {
        val filtered = when (filter) {
            "income" -> _transactions.value.filter { it.type == TransactionType.INCOME }
            "expense" -> _transactions.value.filter { it.type == TransactionType.EXPENSE }
            "transfer" -> _transactions.value.filter { it.type == TransactionType.TRANSFER }
            else -> _transactions.value
        }
        _filteredTransactions.value = filtered
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.insertTransaction(transaction)
        }
    }
}