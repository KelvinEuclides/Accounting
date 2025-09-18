package com.anje.kelvin.aconting.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anje.kelvin.aconting.data.repository.SalesRepository
import com.anje.kelvin.aconting.data.repository.AuthRepository
import com.anje.kelvin.aconting.data.repository.ProductRepository
import com.anje.kelvin.aconting.data.database.entities.Product
import com.anje.kelvin.aconting.data.database.entities.SaleItem as EntitySaleItem
import com.anje.kelvin.aconting.util.TaxConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SalesUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val currentUserId: Long = 0L,
    val availableProducts: List<Product> = emptyList(),
    val selectedItems: List<UiSaleItem> = emptyList(),
    val total: Double = 0.0,
    val taxAmount: Double = 0.0,
    val finalAmount: Double = 0.0
)

data class UiSaleItem(
    val id: Long,
    val productId: Long,
    val nome: String,
    val preco: Double,
    val unidade: String,
    val quantity: Int
) {
    val totalPrice: Double get() = preco * quantity
}

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val salesRepository: SalesRepository,
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalesUiState())
    val uiState: StateFlow<SalesUiState> = _uiState.asStateFlow()

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
        
        // Load available products
        viewModelScope.launch {
            productRepository.getAllActiveProducts().collect { products ->
                _uiState.update { it.copy(availableProducts = products) }
            }
        }
    }

    fun addItem(productId: Long, productName: String, price: Double, unit: String, quantity: Int) {
        val newItem = UiSaleItem(
            id = System.currentTimeMillis(),
            productId = productId,
            nome = productName,
            preco = price,
            unidade = unit,
            quantity = quantity
        )
        
        _uiState.update { state ->
            val updatedItems = state.selectedItems + newItem
            val total = updatedItems.sumOf { it.totalPrice }
            val taxAmount = total * TaxConstants.IVA_TAX_RATE
            val finalAmount = total + taxAmount
            
            state.copy(
                selectedItems = updatedItems,
                total = total,
                taxAmount = taxAmount,
                finalAmount = finalAmount,
                error = null
            )
        }
    }

    fun updateItemQuantity(itemId: Long, newQuantity: Int) {
        _uiState.update { state ->
            val updatedItems = state.selectedItems.map { item ->
                if (item.id == itemId) item.copy(quantity = newQuantity) else item
            }
            val total = updatedItems.sumOf { it.totalPrice }
            val taxAmount = total * TaxConstants.IVA_TAX_RATE
            val finalAmount = total + taxAmount
            
            state.copy(
                selectedItems = updatedItems,
                total = total,
                taxAmount = taxAmount,
                finalAmount = finalAmount
            )
        }
    }

    fun removeItem(itemId: Long) {
        _uiState.update { state ->
            val updatedItems = state.selectedItems.filter { it.id != itemId }
            val total = updatedItems.sumOf { it.totalPrice }
            val taxAmount = total * TaxConstants.IVA_TAX_RATE
            val finalAmount = total + taxAmount
            
            state.copy(
                selectedItems = updatedItems,
                total = total,
                taxAmount = taxAmount,
                finalAmount = finalAmount
            )
        }
    }

    fun processSale() {
        viewModelScope.launch {
            val state = _uiState.value
            
            // Validation
            if (state.selectedItems.isEmpty()) {
                _uiState.update { it.copy(error = "Adicione pelo menos um item à venda") }
                return@launch
            }

            if (state.currentUserId <= 0) {
                _uiState.update { it.copy(error = "Usuário não identificado. Faça login para continuar.") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Convert UI items to database entities
                val saleItems = state.selectedItems.map { item ->
                    EntitySaleItem(
                        saleId = 0, // Will be set by the repository
                        productId = item.productId,
                        productName = item.nome,
                        unitPrice = item.preco,
                        quantity = item.quantity,
                        discountPercent = 0.0
                    )
                }

                val result = salesRepository.createSale(
                    userId = state.currentUserId,
                    items = saleItems,
                    paymentMethod = "Cash",
                    customerName = null,
                    customerPhone = null,
                    notes = null
                )

                if (result.isSuccess) {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            success = true,
                            selectedItems = emptyList(),
                            total = 0.0,
                            taxAmount = 0.0,
                            finalAmount = 0.0
                        )
                    }
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = result.exceptionOrNull()?.message ?: "Erro ao processar venda"
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Erro ao processar venda: ${e.message}"
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
        _uiState.update { 
            it.copy(
                selectedItems = emptyList(),
                total = 0.0,
                taxAmount = 0.0,
                finalAmount = 0.0,
                error = null,
                success = false
            )
        }
    }
}
