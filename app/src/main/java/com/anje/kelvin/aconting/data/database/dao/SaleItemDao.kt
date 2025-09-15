package com.anje.kelvin.aconting.data.database.dao

import androidx.room.*
import com.anje.kelvin.aconting.data.database.entities.SaleItem
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleItemDao {

    @Query("SELECT * FROM sale_items WHERE saleId = :saleId")
    suspend fun getSaleItemsBySaleId(saleId: Long): List<SaleItem>

    @Query("SELECT * FROM sale_items WHERE saleId = :saleId")
    fun getSaleItemsBySaleIdFlow(saleId: Long): Flow<List<SaleItem>>

    @Query("SELECT * FROM sale_items WHERE productId = :productId")
    fun getSaleItemsByProductId(productId: Long): Flow<List<SaleItem>>

    @Query("SELECT SUM(quantity) FROM sale_items WHERE productId = :productId")
    suspend fun getTotalQuantitySoldByProduct(productId: Long): Int?

    @Query("SELECT SUM(finalAmount) FROM sale_items WHERE productId = :productId")
    suspend fun getTotalRevenueByProduct(productId: Long): Double?

    @Query("""
        SELECT productId, productName, SUM(quantity) as totalQuantity, SUM(finalAmount) as totalRevenue
        FROM sale_items 
        GROUP BY productId, productName 
        ORDER BY totalRevenue DESC
    """)
    suspend fun getProductSalesReport(): List<ProductSalesReport>

    @Query("""
        SELECT si.productId, si.productName, SUM(si.quantity) as totalQuantity, SUM(si.finalAmount) as totalRevenue
        FROM sale_items si
        INNER JOIN sales s ON si.saleId = s.id
        WHERE s.userId = :userId
        GROUP BY si.productId, si.productName 
        ORDER BY totalRevenue DESC
    """)
    suspend fun getProductSalesReportByUser(userId: Long): List<ProductSalesReport>

    @Insert
    suspend fun insertSaleItem(saleItem: SaleItem): Long

    @Insert
    suspend fun insertSaleItems(saleItems: List<SaleItem>): List<Long>

    @Update
    suspend fun updateSaleItem(saleItem: SaleItem)

    @Delete
    suspend fun deleteSaleItem(saleItem: SaleItem)

    @Query("DELETE FROM sale_items WHERE saleId = :saleId")
    suspend fun deleteSaleItemsBySaleId(saleId: Long)

    @Query("DELETE FROM sale_items WHERE id = :saleItemId")
    suspend fun deleteSaleItemById(saleItemId: Long)
}

data class ProductSalesReport(
    val productId: Long,
    val productName: String,
    val totalQuantity: Int,
    val totalRevenue: Double
)