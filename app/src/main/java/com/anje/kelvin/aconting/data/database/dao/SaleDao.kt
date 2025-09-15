package com.anje.kelvin.aconting.data.database.dao

import androidx.room.*
import com.anje.kelvin.aconting.data.database.entities.Sale
import com.anje.kelvin.aconting.data.database.entities.SaleStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface SaleDao {

    @Query("SELECT * FROM sales ORDER BY saleDate DESC")
    fun getAllSales(): Flow<List<Sale>>

    @Query("SELECT * FROM sales WHERE userId = :userId ORDER BY saleDate DESC")
    fun getSalesByUser(userId: Long): Flow<List<Sale>>

    @Query("SELECT * FROM sales WHERE userId = :userId AND DATE(saleDate/1000, 'unixepoch') = DATE('now') ORDER BY saleDate DESC")
    fun getTodaySales(userId: Long): Flow<List<Sale>>

    @Query("SELECT * FROM sales WHERE id = :id")
    suspend fun getSaleById(id: Long): Sale?

    @Query("SELECT * FROM sales WHERE status = :status ORDER BY saleDate DESC")
    fun getSalesByStatus(status: SaleStatus): Flow<List<Sale>>

    @Query("SELECT * FROM sales WHERE saleDate BETWEEN :startDate AND :endDate ORDER BY saleDate DESC")
    fun getSalesByDateRange(startDate: Date, endDate: Date): Flow<List<Sale>>

    @Query("SELECT * FROM sales WHERE userId = :userId AND saleDate BETWEEN :startDate AND :endDate ORDER BY saleDate DESC")
    fun getSalesByUserAndDateRange(userId: Long, startDate: Date, endDate: Date): Flow<List<Sale>>

    @Query("SELECT SUM(finalAmount) FROM sales WHERE status = 'COMPLETED'")
    suspend fun getTotalSalesAmount(): Double?

    @Query("SELECT SUM(finalAmount) FROM sales WHERE userId = :userId AND status = 'COMPLETED'")
    suspend fun getTotalSalesAmountByUser(userId: Long): Double?

    @Query("SELECT SUM(finalAmount) FROM sales WHERE userId = :userId AND status = 'COMPLETED'")
    suspend fun getTotalRevenueByUser(userId: Long): Double?

    @Query("SELECT SUM(finalAmount) FROM sales WHERE status = 'COMPLETED' AND saleDate BETWEEN :startDate AND :endDate")
    suspend fun getTotalSalesAmountByDateRange(startDate: Date, endDate: Date): Double?

    @Query("SELECT COUNT(*) FROM sales WHERE status = 'COMPLETED'")
    suspend fun getCompletedSalesCount(): Int

    @Query("SELECT COUNT(*) FROM sales WHERE userId = :userId AND status = 'COMPLETED'")
    suspend fun getCompletedSalesCountByUser(userId: Long): Int

    @Insert
    suspend fun insertSale(sale: Sale): Long

    @Insert
    suspend fun insertSales(sales: List<Sale>): List<Long>

    @Update
    suspend fun updateSale(sale: Sale)

    @Delete
    suspend fun deleteSale(sale: Sale)

    @Query("UPDATE sales SET status = :status WHERE id = :saleId")
    suspend fun updateSaleStatus(saleId: Long, status: SaleStatus)

    @Query("DELETE FROM sales WHERE id = :saleId")
    suspend fun deleteSaleById(saleId: Long)

    // Analytics queries
    @Query("""
        SELECT DATE(saleDate/1000, 'unixepoch') as date, SUM(finalAmount) as total 
        FROM sales 
        WHERE status = 'COMPLETED' AND saleDate BETWEEN :startDate AND :endDate 
        GROUP BY DATE(saleDate/1000, 'unixepoch') 
        ORDER BY date
    """)
    suspend fun getDailySalesReport(startDate: Date, endDate: Date): List<DailySalesReport>

    @Query("SELECT * FROM sales WHERE customerName LIKE '%' || :customerName || '%' ORDER BY saleDate DESC")
    fun searchSalesByCustomer(customerName: String): Flow<List<Sale>>
}

data class DailySalesReport(
    val date: String,
    val total: Double
)