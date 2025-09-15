package com.anje.kelvin.aconting.data.repository

import com.anje.kelvin.aconting.data.database.dao.SaleDao
import com.anje.kelvin.aconting.data.database.dao.SaleItemDao
import com.anje.kelvin.aconting.data.database.dao.ProductDao
import com.anje.kelvin.aconting.data.database.entities.Sale
import com.anje.kelvin.aconting.data.database.entities.SaleItem
import com.anje.kelvin.aconting.data.database.entities.Product
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SalesRepository @Inject constructor(
    private val saleDao: SaleDao,
    private val saleItemDao: SaleItemDao,
    private val productDao: ProductDao
) {

    // Sale operations
    fun getAllSales(): Flow<List<Sale>> = saleDao.getAllSales()
    
    fun getSalesByUser(userId: Long): Flow<List<Sale>> = saleDao.getSalesByUser(userId)
    
    fun getTodaySales(userId: Long): Flow<List<Sale>> = saleDao.getTodaySales(userId)
    
    suspend fun getSaleById(id: Long): Sale? = saleDao.getSaleById(id)

    suspend fun getSaleItems(saleId: Long): List<SaleItem> = saleItemDao.getSaleItemsBySaleId(saleId)

    // Product operations
    fun getAllActiveProducts(): Flow<List<Product>> = productDao.getAllActiveProducts()
    
    fun searchProducts(query: String): Flow<List<Product>> = productDao.searchProducts(query)
    
    suspend fun getProductById(id: Long): Product? = productDao.getProductById(id)

    // Create sale transaction
    suspend fun createSale(
        userId: Long,
        items: List<SaleItem>,
        paymentMethod: String = "Cash",
        customerName: String? = null,
        customerPhone: String? = null,
        notes: String? = null
    ): Result<Sale> {
        return try {
            val subtotal = items.sumOf { it.finalAmount }
            val taxAmount = subtotal * 0.17 // 17% tax
            val finalAmount = subtotal + taxAmount
            val itemCount = items.sumOf { it.quantity }

            val sale = Sale(
                userId = userId,
                totalAmount = subtotal,
                taxAmount = taxAmount,
                finalAmount = finalAmount,
                itemCount = itemCount,
                paymentMethod = paymentMethod,
                customerName = customerName,
                customerPhone = customerPhone,
                notes = notes,
                saleDate = Date(),
                createdAt = Date(),
                updatedAt = Date()
            )

            val saleId = saleDao.insertSale(sale)
            
            // Insert sale items with the generated sale ID
            val saleItemsWithSaleId = items.map { it.copy(saleId = saleId) }
            saleItemDao.insertSaleItems(saleItemsWithSaleId)

            // Update product quantities
            items.forEach { item ->
                productDao.reduceProductQuantity(item.productId, item.quantity)
            }

            Result.success(sale.copy(id = saleId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Analytics
    suspend fun getTotalSalesRevenue(userId: Long): Double {
        return saleDao.getTotalRevenueByUser(userId) ?: 0.0
    }

    suspend fun getTotalSalesCount(userId: Long): Int {
        return saleDao.getCompletedSalesCountByUser(userId)
    }

    suspend fun getTopSellingProducts(userId: Long): List<com.anje.kelvin.aconting.data.database.dao.ProductSalesReport> {
        return saleItemDao.getProductSalesReportByUser(userId)
    }

    // Delete operations
    suspend fun deleteSale(saleId: Long): Result<Unit> {
        return try {
            // Get sale items to restore quantities
            val saleItems = saleItemDao.getSaleItemsBySaleId(saleId)
            
            // Restore product quantities
            saleItems.forEach { item ->
                productDao.increaseProductQuantity(item.productId, item.quantity)
            }
            
            // Delete sale items first (due to foreign key)
            saleItemDao.deleteSaleItemsBySaleId(saleId)
            
            // Delete sale
            saleDao.deleteSaleById(saleId)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}