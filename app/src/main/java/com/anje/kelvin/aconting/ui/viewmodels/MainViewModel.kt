package com.anje.kelvin.aconting.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.anje.kelvin.aconting.ui.components.TransactionItem

/**
 * ViewModel for the main screens in the application
 * In a real app, this would fetch data from repositories and handle business logic
 */
class MainViewModel : ViewModel() {
    // Account info
    var saldoConta by mutableStateOf("10,000 MZN")
        private set
    
    var nomeConta by mutableStateOf("Minha Conta")
        private set
    
    var despesasMensais by mutableStateOf("4,500 MZN")
        private set
    
    var depositosMensais by mutableStateOf("15,000 MZN")
        private set
        
    var numVendasMensais by mutableStateOf("23")
        private set
    
    // Transactions lists
    var despesaList by mutableStateOf(listOf(
        TransactionItem("Compra de materiais", "500 MZN", "01/06/2025", true),
        TransactionItem("Aluguel", "1,200 MZN", "01/06/2025", true),
        TransactionItem("Eletricidade", "300 MZN", "01/06/2025", true)
    ))
        private set
        
    var depositoList by mutableStateOf(listOf(
        TransactionItem("Venda de produtos", "2,000 MZN", "01/06/2025", false),
        TransactionItem("Reembolso", "500 MZN", "01/06/2025", false)
    ))
        private set
        
    // Methods to refresh data
    fun refreshAccountData() {
        // In a real app, this would fetch data from a repository
        // For now, we're just using hardcoded data
    }
    
    fun updatePin(currentPin: String, newPin: String): Boolean {
        // In a real app, this would validate and update the PIN in the database
        // Return true if successful, false otherwise
        return true
    }
    
    fun logout() {
        // Clear user session data
    }
}
