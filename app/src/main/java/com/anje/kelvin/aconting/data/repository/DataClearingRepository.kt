package com.anje.kelvin.aconting.data.repository

import com.anje.kelvin.aconting.data.database.dao.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataClearingRepository @Inject constructor(
    private val saleDao: SaleDao,
    private val saleItemDao: SaleItemDao,
    private val expenseDao: ExpenseDao,
    private val depositDao: DepositDao,
    private val transactionDao: TransactionDao,
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao
) {

    suspend fun clearAllSales(userId: Long): Result<Unit> {
        return try {
            val saleList = saleDao.getSalesByUser(userId).first()
            saleList.forEach { sale ->
                saleItemDao.deleteSaleItemsBySaleId(sale.id)
                saleDao.deleteSaleById(sale.id)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clearAllExpenses(userId: Long): Result<Unit> {
        return try {
            val expenseList = expenseDao.getExpensesByUser(userId).first()
            expenseList.forEach { expense ->
                expenseDao.deleteExpenseById(expense.id)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clearAllDeposits(userId: Long): Result<Unit> {
        return try {
            val depositList = depositDao.getDepositsByUser(userId).first()
            depositList.forEach { deposit ->
                depositDao.deleteDepositById(deposit.id)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clearAllTransactions(): Result<Unit> {
        return try {
            val transactionList = transactionDao.getAllTransactions().first()
            transactionList.forEach { transaction ->
                transactionDao.deleteTransactionById(transaction.id)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clearAllProducts(): Result<Unit> {
        return try {
            val productList = productDao.getAllProducts().first()
            productList.forEach { product ->
                productDao.deleteProduct(product)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clearAllData(userId: Long): Result<Unit> {
        return try {
            // Clear in order respecting foreign key constraints
            clearAllSales(userId)
            clearAllExpenses(userId)
            clearAllDeposits(userId)
            clearAllTransactions()
            clearAllProducts()
            // Keep categories and user settings
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
