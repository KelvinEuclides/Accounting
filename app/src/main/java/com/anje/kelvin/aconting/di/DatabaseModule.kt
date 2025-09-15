package com.anje.kelvin.aconting.di

import android.content.Context
import androidx.room.Room
import com.anje.kelvin.aconting.data.database.AccountingDatabase
import com.anje.kelvin.aconting.data.database.dao.ProductDao
import com.anje.kelvin.aconting.data.database.dao.TransactionDao
import com.anje.kelvin.aconting.data.database.dao.UserDao
import com.anje.kelvin.aconting.data.database.dao.DepositDao
import com.anje.kelvin.aconting.data.database.dao.ExpenseDao
import com.anje.kelvin.aconting.data.database.dao.CategoryDao
import com.anje.kelvin.aconting.data.database.dao.SaleDao
import com.anje.kelvin.aconting.data.database.dao.SaleItemDao
import com.anje.kelvin.aconting.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideAccountingDatabase(@ApplicationContext context: Context): AccountingDatabase {
        return AccountingDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideTransactionDao(database: AccountingDatabase): TransactionDao {
        return database.transactionDao()
    }
    
    @Provides
    fun provideProductDao(database: AccountingDatabase): ProductDao {
        return database.productDao()
    }
    
    @Provides
    fun provideUserDao(database: AccountingDatabase): UserDao {
        return database.userDao()
    }
    
    @Provides
    fun provideDepositDao(database: AccountingDatabase): DepositDao {
        return database.depositDao()
    }
    
    @Provides
    fun provideExpenseDao(database: AccountingDatabase): ExpenseDao {
        return database.expenseDao()
    }
    
    @Provides
    fun provideCategoryDao(database: AccountingDatabase): CategoryDao {
        return database.categoryDao()
    }
    
    @Provides
    fun provideSaleDao(database: AccountingDatabase): SaleDao {
        return database.saleDao()
    }
    
    @Provides
    fun provideSaleItemDao(database: AccountingDatabase): SaleItemDao {
        return database.saleItemDao()
    }
    
    @Provides
    @Singleton
    fun provideUserRepository(
        @ApplicationContext context: Context,
        database: AccountingDatabase
    ): UserRepository {
        return UserRepository(context, database)
    }
}
