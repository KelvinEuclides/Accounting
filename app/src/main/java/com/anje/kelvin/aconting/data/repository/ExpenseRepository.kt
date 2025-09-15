package com.anje.kelvin.aconting.data.repository

import com.anje.kelvin.aconting.data.database.dao.ExpenseDao
import com.anje.kelvin.aconting.data.database.dao.CategoryDao
import com.anje.kelvin.aconting.data.database.entities.Expense
import com.anje.kelvin.aconting.data.database.entities.Category
import com.anje.kelvin.aconting.data.database.entities.CategoryType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao
) {

    // CRUD Operations
    suspend fun createExpense(
        userId: Long,
        amount: Double,
        description: String,
        category: String,
        expenseDate: Date = Date(),
        notes: String? = null
    ): Result<Long> {
        return try {
            val expense = Expense(
                userId = userId,
                amount = amount,
                description = description,
                category = category,
                expenseDate = expenseDate,
                notes = notes
            )
            val expenseId = expenseDao.insertExpense(expense)
            Result.success(expenseId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateExpense(expense: Expense): Result<Unit> {
        return try {
            expenseDao.updateExpense(expense)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteExpense(expense: Expense): Result<Unit> {
        return try {
            expenseDao.deleteExpense(expense)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getExpenseById(id: Long): Expense? = expenseDao.getExpenseById(id)

    // Query Operations
    fun getAllExpenses(): Flow<List<Expense>> = expenseDao.getAllExpenses()

    fun getExpensesByUser(userId: Long): Flow<List<Expense>> = 
        expenseDao.getExpensesByUser(userId)

    fun getExpensesByCategory(category: String): Flow<List<Expense>> = 
        expenseDao.getExpensesByCategory(category)

    fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>> =
        expenseDao.getExpensesByDateRange(startDate, endDate)

    fun getExpensesByUserAndDateRange(userId: Long, startDate: Date, endDate: Date): Flow<List<Expense>> =
        expenseDao.getExpensesByUserAndDateRange(userId, startDate, endDate)

    // Analytics Operations
    suspend fun getTotalExpensesByUser(userId: Long): Double = 
        expenseDao.getTotalExpensesByUser(userId) ?: 0.0

    suspend fun getTotalExpensesByUserAndDateRange(userId: Long, startDate: Date, endDate: Date): Double =
        expenseDao.getTotalExpensesByUserAndDateRange(userId, startDate, endDate) ?: 0.0

    suspend fun getTotalExpensesByCategory(category: String): Double =
        expenseDao.getTotalExpensesByCategory(category) ?: 0.0

    suspend fun getTotalExpensesByCategoryAndDateRange(category: String, startDate: Date, endDate: Date): Double =
        expenseDao.getTotalExpensesByCategoryAndDateRange(category, startDate, endDate) ?: 0.0

    suspend fun getExpenseCountByUser(userId: Long): Int = 
        expenseDao.getExpenseCountByUser(userId)

    // Category Management
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun createDefaultCategories(userId: Long) {
        val existingCategories = categoryDao.getAllCategories().first()
        
        val defaultCategories = listOf(
            Category(userId = userId, name = "Despesa Geral", description = "Despesas gerais e variadas", type = CategoryType.EXPENSE),
            Category(userId = userId, name = "Alimentação", description = "Gastos com comida e bebida", type = CategoryType.EXPENSE),
            Category(userId = userId, name = "Transporte", description = "Gastos com transporte", type = CategoryType.EXPENSE),
            Category(userId = userId, name = "Saúde", description = "Gastos médicos e farmácia", type = CategoryType.EXPENSE),
            Category(userId = userId, name = "Educação", description = "Gastos com educação e formação", type = CategoryType.EXPENSE),
            Category(userId = userId, name = "Lazer", description = "Entretenimento e lazer", type = CategoryType.EXPENSE),
            Category(userId = userId, name = "Utilities", description = "Água, luz, gás", type = CategoryType.EXPENSE),
            Category(userId = userId, name = "Aluguel", description = "Pagamento de aluguel", type = CategoryType.EXPENSE)
        )

        defaultCategories.forEach { defaultCategory ->
            if (existingCategories.none { it.name == defaultCategory.name }) {
                categoryDao.insertCategory(defaultCategory)
            }
        }
    }

    suspend fun createCategory(userId: Long, name: String, description: String? = null): Result<Long> {
        return try {
            val category = Category(userId = userId, name = name, description = description, type = CategoryType.EXPENSE)
            val categoryId = categoryDao.insertCategory(category)
            Result.success(categoryId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCategoryById(id: Long): Category? = categoryDao.getCategoryById(id)
}