package com.anje.kelvin.aconting.data.database.dao

import androidx.room.*
import com.anje.kelvin.aconting.data.database.entities.Expense
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses ORDER BY expenseDate DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE userId = :userId ORDER BY expenseDate DESC")
    fun getExpensesByUser(userId: Long): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Long): Expense?

    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY expenseDate DESC")
    fun getExpensesByCategory(category: String): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE expenseDate BETWEEN :startDate AND :endDate ORDER BY expenseDate DESC")
    fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE userId = :userId AND expenseDate BETWEEN :startDate AND :endDate ORDER BY expenseDate DESC")
    fun getExpensesByUserAndDateRange(userId: Long, startDate: Date, endDate: Date): Flow<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses")
    suspend fun getTotalExpensesAmount(): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE userId = :userId")
    suspend fun getTotalExpensesAmountByUser(userId: Long): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE userId = :userId")
    suspend fun getTotalExpensesByUser(userId: Long): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE userId = :userId AND expenseDate BETWEEN :startDate AND :endDate")
    suspend fun getTotalExpensesByUserAndDateRange(userId: Long, startDate: Date, endDate: Date): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE expenseDate BETWEEN :startDate AND :endDate")
    suspend fun getTotalExpensesAmountByDateRange(startDate: Date, endDate: Date): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE category = :category")
    suspend fun getTotalExpensesAmountByCategory(category: String): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE category = :category")
    suspend fun getTotalExpensesByCategory(category: String): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE category = :category AND expenseDate BETWEEN :startDate AND :endDate")
    suspend fun getTotalExpensesByCategoryAndDateRange(category: String, startDate: Date, endDate: Date): Double?

    @Query("SELECT COUNT(*) FROM expenses")
    suspend fun getExpensesCount(): Int

    @Query("SELECT COUNT(*) FROM expenses WHERE userId = :userId")
    suspend fun getExpensesCountByUser(userId: Long): Int

    @Query("SELECT COUNT(*) FROM expenses WHERE userId = :userId")
    suspend fun getExpenseCountByUser(userId: Long): Int

    @Query("SELECT DISTINCT category FROM expenses ORDER BY category")
    suspend fun getAllCategories(): List<String>

    @Query("SELECT DISTINCT category FROM expenses WHERE userId = :userId ORDER BY category")
    suspend fun getCategoriesByUser(userId: Long): List<String>

    @Insert
    suspend fun insertExpense(expense: Expense): Long

    @Insert
    suspend fun insertExpenses(expenses: List<Expense>): List<Long>

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpenseById(expenseId: Long)

    // Analytics queries
    @Query("""
        SELECT category, SUM(amount) as total 
        FROM expenses 
        WHERE expenseDate BETWEEN :startDate AND :endDate 
        GROUP BY category 
        ORDER BY total DESC
    """)
    suspend fun getExpensesByCategory(startDate: Date, endDate: Date): List<CategoryExpenseReport>

    @Query("""
        SELECT DATE(expenseDate/1000, 'unixepoch') as date, SUM(amount) as total 
        FROM expenses 
        WHERE expenseDate BETWEEN :startDate AND :endDate 
        GROUP BY DATE(expenseDate/1000, 'unixepoch') 
        ORDER BY date
    """)
    suspend fun getDailyExpensesReport(startDate: Date, endDate: Date): List<DailyExpenseReport>

    @Query("SELECT * FROM expenses WHERE description LIKE '%' || :searchQuery || '%' OR vendor LIKE '%' || :searchQuery || '%' ORDER BY expenseDate DESC")
    fun searchExpenses(searchQuery: String): Flow<List<Expense>>
}

data class CategoryExpenseReport(
    val category: String,
    val total: Double
)

data class DailyExpenseReport(
    val date: String,
    val total: Double
)