package com.anje.kelvin.aconting.data.database.dao

import androidx.room.*
import com.anje.kelvin.aconting.data.database.entities.Deposit
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface DepositDao {

    @Query("SELECT * FROM deposits ORDER BY depositDate DESC")
    fun getAllDeposits(): Flow<List<Deposit>>

    @Query("SELECT * FROM deposits WHERE userId = :userId ORDER BY depositDate DESC")
    fun getDepositsByUser(userId: Long): Flow<List<Deposit>>

    @Query("SELECT * FROM deposits WHERE id = :id")
    suspend fun getDepositById(id: Long): Deposit?

    @Query("SELECT * FROM deposits WHERE category = :category ORDER BY depositDate DESC")
    fun getDepositsByCategory(category: String): Flow<List<Deposit>>

    @Query("SELECT * FROM deposits WHERE userId = :userId AND category = :category ORDER BY depositDate DESC")
    fun getDepositsByCategory(userId: Long, category: String): Flow<List<Deposit>>

    @Query("SELECT * FROM deposits WHERE userId = :userId AND DATE(depositDate/1000, 'unixepoch') = DATE('now') ORDER BY depositDate DESC")
    fun getTodayDeposits(userId: Long): Flow<List<Deposit>>

    @Query("SELECT * FROM deposits WHERE depositDate BETWEEN :startDate AND :endDate ORDER BY depositDate DESC")
    fun getDepositsByDateRange(startDate: Date, endDate: Date): Flow<List<Deposit>>

    @Query("SELECT * FROM deposits WHERE userId = :userId AND depositDate BETWEEN :startDate AND :endDate ORDER BY depositDate DESC")
    fun getDepositsByUserAndDateRange(userId: Long, startDate: Date, endDate: Date): Flow<List<Deposit>>

    @Query("SELECT SUM(amount) FROM deposits")
    suspend fun getTotalDepositsAmount(): Double?

    @Query("SELECT SUM(amount) FROM deposits WHERE userId = :userId")
    suspend fun getTotalDepositsByUser(userId: Long): Double?

    @Query("SELECT SUM(amount) FROM deposits WHERE userId = :userId")
    suspend fun getTotalDepositsAmountByUser(userId: Long): Double?

    @Query("SELECT SUM(amount) FROM deposits WHERE depositDate BETWEEN :startDate AND :endDate")
    suspend fun getTotalDepositsAmountByDateRange(startDate: Date, endDate: Date): Double?

    @Query("SELECT SUM(amount) FROM deposits WHERE category = :category")
    suspend fun getTotalDepositsAmountByCategory(category: String): Double?

    @Query("SELECT COUNT(*) FROM deposits")
    suspend fun getDepositsCount(): Int

    @Query("SELECT COUNT(*) FROM deposits WHERE userId = :userId")
    suspend fun getDepositsCountByUser(userId: Long): Int

    @Query("SELECT DISTINCT category FROM deposits ORDER BY category")
    suspend fun getAllCategories(): List<String>

    @Query("SELECT DISTINCT category FROM deposits WHERE userId = :userId ORDER BY category")
    suspend fun getCategoriesByUser(userId: Long): List<String>

    @Insert
    suspend fun insertDeposit(deposit: Deposit): Long

    @Insert
    suspend fun insertDeposits(deposits: List<Deposit>): List<Long>

    @Update
    suspend fun updateDeposit(deposit: Deposit)

    @Delete
    suspend fun deleteDeposit(deposit: Deposit)

    @Query("DELETE FROM deposits WHERE id = :depositId")
    suspend fun deleteDepositById(depositId: Long)

    // Analytics queries
    @Query("""
        SELECT category, SUM(amount) as total 
        FROM deposits 
        WHERE depositDate BETWEEN :startDate AND :endDate 
        GROUP BY category 
        ORDER BY total DESC
    """)
    suspend fun getDepositReportByCategory(startDate: Date, endDate: Date): List<CategoryDepositReport>

    @Query("""
        SELECT DATE(depositDate/1000, 'unixepoch') as date, SUM(amount) as total 
        FROM deposits 
        WHERE depositDate BETWEEN :startDate AND :endDate 
        GROUP BY DATE(depositDate/1000, 'unixepoch') 
        ORDER BY date
    """)
    suspend fun getDailyDepositsReport(startDate: Date, endDate: Date): List<DailyDepositReport>

    @Query("SELECT * FROM deposits WHERE description LIKE '%' || :searchQuery || '%' OR source LIKE '%' || :searchQuery || '%' ORDER BY depositDate DESC")
    fun searchDeposits(searchQuery: String): Flow<List<Deposit>>
}

data class CategoryDepositReport(
    val category: String,
    val total: Double
)

data class DailyDepositReport(
    val date: String,
    val total: Double
)