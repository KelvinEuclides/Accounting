package com.anje.kelvin.aconting.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anje.kelvin.aconting.ui.screens.TransactionItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionsViewModel : ViewModel() {
    
    // State for all transactions
    private val _allTransactions = MutableStateFlow<List<TransactionItem>>(emptyList())
    val allTransactions: StateFlow<List<TransactionItem>> = _allTransactions.asStateFlow()
    
    // State for expense transactions
    private val _expenses = MutableStateFlow<List<TransactionItem>>(emptyList())
    val expenses: StateFlow<List<TransactionItem>> = _expenses.asStateFlow()
    
    // State for deposit transactions
    private val _deposits = MutableStateFlow<List<TransactionItem>>(emptyList())
    val deposits: StateFlow<List<TransactionItem>> = _deposits.asStateFlow()
    
    // State for sales transactions
    private val _sales = MutableStateFlow<List<TransactionItem>>(emptyList())
    val sales: StateFlow<List<TransactionItem>> = _sales.asStateFlow()
    
    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadTransactions()
    }
    
    private fun loadTransactions() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // In a real app, this data would come from a repository or database
            // For now, we'll use sample data
            
            // Sample expenses
            val expensesList = listOf(
                TransactionItem("Compra de materiais", "500 MZN", "01/06/2025", true),
                TransactionItem("Aluguel", "1,200 MZN", "01/06/2025", true),
                TransactionItem("Eletricidade", "300 MZN", "01/06/2025", true),
                TransactionItem("Transporte", "200 MZN", "31/05/2025", true)
            )
            
            // Sample deposits
            val depositsList = listOf(
                TransactionItem("Venda de produtos", "2,000 MZN", "01/06/2025", false),
                TransactionItem("Reembolso", "500 MZN", "01/06/2025", false),
                TransactionItem("Pagamento de cliente", "1,500 MZN", "31/05/2025", false)
            )
            
            // Sample sales
            val salesList = listOf(
                TransactionItem("Venda #1234", "2,000 MZN", "01/06/2025", false),
                TransactionItem("Venda #1235", "1,500 MZN", "01/06/2025", false),
                TransactionItem("Venda #1236", "750 MZN", "31/05/2025", false)
            )
            
            // Update state flows with sample data
            _expenses.value = expensesList
            _deposits.value = depositsList
            _sales.value = salesList
            
            // Combine all transactions
            _allTransactions.value = expensesList + depositsList + salesList
            
            _isLoading.value = false
        }
    }
    
    // In a real app, we would have methods to add new transactions like:
    
    fun addExpense(description: String, amount: String, date: String) {
        viewModelScope.launch {
            val newExpense = TransactionItem(description, amount, date, true)
            
            // In a real app, we would first add this to the database
            // Then update the state flows with new data from database
            
            // For now, we'll just update the state flows directly
            _expenses.value = _expenses.value + newExpense
            _allTransactions.value = _allTransactions.value + newExpense
        }
    }
    
    fun addDeposit(description: String, amount: String, date: String) {
        viewModelScope.launch {
            val newDeposit = TransactionItem(description, amount, date, false)
            
            // In a real app, we would first add this to the database
            // Then update the state flows with new data from database
            
            // For now, we'll just update the state flows directly
            _deposits.value = _deposits.value + newDeposit
            _allTransactions.value = _allTransactions.value + newDeposit
        }
    }
    
    fun addSale(description: String, amount: String, date: String) {
        viewModelScope.launch {
            val newSale = TransactionItem(description, amount, date, false)
            
            // In a real app, we would first add this to the database
            // Then update the state flows with new data from database
            
            // For now, we'll just update the state flows directly
            _sales.value = _sales.value + newSale
            _allTransactions.value = _allTransactions.value + newSale
        }
    }
}
