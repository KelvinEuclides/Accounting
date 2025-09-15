package com.anje.kelvin.aconting.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import com.anje.kelvin.aconting.data.database.converters.Converters
import com.anje.kelvin.aconting.data.database.dao.ProductDao
import com.anje.kelvin.aconting.data.database.dao.TransactionDao
import com.anje.kelvin.aconting.data.database.dao.UserDao
import com.anje.kelvin.aconting.data.database.dao.SaleDao
import com.anje.kelvin.aconting.data.database.dao.SaleItemDao
import com.anje.kelvin.aconting.data.database.dao.ExpenseDao
import com.anje.kelvin.aconting.data.database.dao.DepositDao
import com.anje.kelvin.aconting.data.database.dao.CategoryDao
import com.anje.kelvin.aconting.data.database.entities.Product
import com.anje.kelvin.aconting.data.database.entities.Transaction
import com.anje.kelvin.aconting.data.database.entities.User
import com.anje.kelvin.aconting.data.database.entities.Sale
import com.anje.kelvin.aconting.data.database.entities.SaleItem
import com.anje.kelvin.aconting.data.database.entities.Expense
import com.anje.kelvin.aconting.data.database.entities.Deposit
import com.anje.kelvin.aconting.data.database.entities.Category

@Database(
    entities = [
        Transaction::class, 
        Product::class, 
        User::class, 
        Sale::class, 
        SaleItem::class, 
        Expense::class, 
        Deposit::class, 
        Category::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AccountingDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
    abstract fun saleDao(): SaleDao
    abstract fun saleItemDao(): SaleItemDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun depositDao(): DepositDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: AccountingDatabase? = null

        fun getDatabase(context: Context): AccountingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AccountingDatabase::class.java,
                    "accounting_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Migration from version 1 to 2 - Add User table
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create users table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `users` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `name` TEXT NOT NULL,
                        `phone` TEXT NOT NULL,
                        `email` TEXT,
                        `pinHash` TEXT NOT NULL,
                        `salt` TEXT NOT NULL,
                        `isActive` INTEGER NOT NULL DEFAULT 1,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        `lastLoginAt` INTEGER,
                        `preferredLanguage` TEXT NOT NULL DEFAULT 'pt',
                        `preferredCurrency` TEXT NOT NULL DEFAULT 'AOA',
                        `darkModeEnabled` INTEGER NOT NULL DEFAULT 0,
                        `notificationsEnabled` INTEGER NOT NULL DEFAULT 1,
                        `loginAttempts` INTEGER NOT NULL DEFAULT 0,
                        `isLocked` INTEGER NOT NULL DEFAULT 0,
                        `lockedUntil` INTEGER,
                        `profileImagePath` TEXT,
                        `role` TEXT NOT NULL DEFAULT 'USER'
                    )
                """.trimIndent())

                // Create unique index on phone
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_users_phone` ON `users` (`phone`)")
                
                // Create unique index on email
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_users_email` ON `users` (`email`)")
            }
        }

        // Migration from version 2 to 3 - Add Sales, Expenses, Deposits, and Categories tables
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create sales table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `sales` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `userId` INTEGER NOT NULL,
                        `totalAmount` REAL NOT NULL,
                        `taxAmount` REAL NOT NULL DEFAULT 0.0,
                        `finalAmount` REAL NOT NULL,
                        `itemCount` INTEGER NOT NULL,
                        `paymentMethod` TEXT NOT NULL DEFAULT 'Cash',
                        `customerName` TEXT,
                        `customerPhone` TEXT,
                        `status` TEXT NOT NULL DEFAULT 'COMPLETED',
                        `notes` TEXT,
                        `saleDate` INTEGER NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON DELETE RESTRICT
                    )
                """.trimIndent())

                // Create sale_items table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `sale_items` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `saleId` INTEGER NOT NULL,
                        `productId` INTEGER NOT NULL,
                        `productName` TEXT NOT NULL,
                        `unitPrice` REAL NOT NULL,
                        `quantity` INTEGER NOT NULL,
                        `subtotal` REAL NOT NULL,
                        `discountPercent` REAL NOT NULL DEFAULT 0.0,
                        `discountAmount` REAL NOT NULL DEFAULT 0.0,
                        `finalAmount` REAL NOT NULL,
                        FOREIGN KEY(`saleId`) REFERENCES `sales`(`id`) ON DELETE CASCADE,
                        FOREIGN KEY(`productId`) REFERENCES `products`(`id`) ON DELETE RESTRICT
                    )
                """.trimIndent())

                // Create expenses table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `expenses` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `userId` INTEGER NOT NULL,
                        `amount` REAL NOT NULL,
                        `description` TEXT NOT NULL,
                        `category` TEXT NOT NULL,
                        `paymentMethod` TEXT NOT NULL DEFAULT 'Cash',
                        `vendor` TEXT,
                        `receiptNumber` TEXT,
                        `notes` TEXT,
                        `expenseDate` INTEGER NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON DELETE RESTRICT
                    )
                """.trimIndent())

                // Create deposits table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `deposits` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `userId` INTEGER NOT NULL,
                        `amount` REAL NOT NULL,
                        `description` TEXT NOT NULL,
                        `category` TEXT NOT NULL,
                        `paymentMethod` TEXT NOT NULL DEFAULT 'Cash',
                        `source` TEXT,
                        `referenceNumber` TEXT,
                        `notes` TEXT,
                        `depositDate` INTEGER NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON DELETE RESTRICT
                    )
                """.trimIndent())

                // Create categories table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `categories` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `userId` INTEGER NOT NULL,
                        `name` TEXT NOT NULL,
                        `type` TEXT NOT NULL,
                        `description` TEXT,
                        `color` TEXT,
                        `icon` TEXT,
                        `isActive` INTEGER NOT NULL DEFAULT 1,
                        `isDefault` INTEGER NOT NULL DEFAULT 0,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON DELETE CASCADE
                    )
                """.trimIndent())

                // Create indices for sales table
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_sales_userId` ON `sales` (`userId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_sales_saleDate` ON `sales` (`saleDate`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_sales_status` ON `sales` (`status`)")

                // Create indices for sale_items table
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_sale_items_saleId` ON `sale_items` (`saleId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_sale_items_productId` ON `sale_items` (`productId`)")

                // Create indices for expenses table
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_expenses_userId` ON `expenses` (`userId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_expenses_category` ON `expenses` (`category`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_expenses_expenseDate` ON `expenses` (`expenseDate`)")

                // Create indices for deposits table
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_deposits_userId` ON `deposits` (`userId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_deposits_category` ON `deposits` (`category`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_deposits_depositDate` ON `deposits` (`depositDate`)")

                // Create indices for categories table
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_categories_userId` ON `categories` (`userId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_categories_type` ON `categories` (`type`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_categories_name` ON `categories` (`name`)")
            }
        }
    }
}
