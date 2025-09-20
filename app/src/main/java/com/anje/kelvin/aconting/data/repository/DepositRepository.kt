package com.anje.kelvin.aconting.data.repository

import com.anje.kelvin.aconting.data.database.dao.DepositDao
import com.anje.kelvin.aconting.data.database.dao.CategoryDao
import com.anje.kelvin.aconting.data.database.entities.Deposit
import com.anje.kelvin.aconting.data.database.entities.Category
import com.anje.kelvin.aconting.data.database.entities.CategoryType
import com.anje.kelvin.aconting.util.AppConstants
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DepositRepository @Inject constructor(
    private val depositDao: DepositDao,
    private val categoryDao: CategoryDao
) {

    // Deposit operations
    fun getAllDeposits(): Flow<List<Deposit>> = depositDao.getAllDeposits()
    
    fun getDepositsByUser(userId: Long): Flow<List<Deposit>> = depositDao.getDepositsByUser(userId)
    
    fun getTodayDeposits(userId: Long): Flow<List<Deposit>> = depositDao.getTodayDeposits(userId)
    
    fun getDepositsByCategory(userId: Long, category: String): Flow<List<Deposit>> = 
        depositDao.getDepositsByCategory(userId, category)
    
    suspend fun getDepositById(id: Long): Deposit? = depositDao.getDepositById(id)

    // Category operations
    fun getDepositCategories(userId: Long): Flow<List<Category>> = 
        categoryDao.getCategoriesByUserAndType(userId, CategoryType.INCOME)

    suspend fun createDeposit(
        userId: Long,
        amount: Double,
        description: String,
        category: String,
        paymentMethod: String = AppConstants.PaymentMethods.DEFAULT,
        source: String? = null,
        referenceNumber: String? = null,
        notes: String? = null,
        depositDate: Date = Date()
    ): Result<Deposit> {
        return try {
            val deposit = Deposit(
                userId = userId,
                amount = amount,
                description = description,
                category = category,
                paymentMethod = paymentMethod,
                source = source,
                referenceNumber = referenceNumber,
                notes = notes,
                depositDate = depositDate,
                createdAt = Date(),
                updatedAt = Date()
            )

            val depositId = depositDao.insertDeposit(deposit)
            Result.success(deposit.copy(id = depositId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateDeposit(deposit: Deposit): Result<Unit> {
        return try {
            val updatedDeposit = deposit.copy(updatedAt = Date())
            depositDao.updateDeposit(updatedDeposit)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteDeposit(depositId: Long): Result<Unit> {
        return try {
            depositDao.deleteDepositById(depositId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Analytics
    suspend fun getTotalDeposits(userId: Long): Double {
        return depositDao.getTotalDepositsByUser(userId) ?: 0.0
    }

    suspend fun getDepositsByCategory(userId: Long): List<com.anje.kelvin.aconting.data.database.dao.CategoryDepositReport> {
        val now = Date()
        val startOfMonth = Date(now.year, now.month, 1)
        return depositDao.getDepositReportByCategory(startOfMonth, now)
    }

    // Default categories
    suspend fun createDefaultDepositCategories(userId: Long) {
        val defaultCategories = listOf(
            "Salário", "Vendas", "Investimentos", "Outros"
        )

        defaultCategories.forEach { categoryName ->
            val category = Category(
                userId = userId,
                name = categoryName,
                type = CategoryType.INCOME,
                description = "Categoria padrão para $categoryName",
                isDefault = true,
                createdAt = Date(),
                updatedAt = Date()
            )
            categoryDao.insertCategory(category)
        }
    }
}