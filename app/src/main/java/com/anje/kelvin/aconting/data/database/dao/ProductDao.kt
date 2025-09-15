package com.anje.kelvin.aconting.data.database.dao

import androidx.room.*
import com.anje.kelvin.aconting.data.database.entities.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE quantity <= minStockLevel AND isActive = 1")
    fun getLowStockProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE quantity <= 0 AND isActive = 1")
    fun getOutOfStockProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE category = :category AND isActive = 1 ORDER BY name ASC")
    fun getProductsByCategory(category: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :searchQuery || '%' AND isActive = 1 ORDER BY name ASC")
    fun searchProducts(searchQuery: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): Product?

    @Query("SELECT * FROM products WHERE barcode = :barcode")
    suspend fun getProductByBarcode(barcode: String): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("UPDATE products SET isActive = 0 WHERE id = :id")
    suspend fun deactivateProduct(id: Long)

    @Query("UPDATE products SET quantity = quantity - :soldQuantity WHERE id = :id")
    suspend fun reduceProductQuantity(id: Long, soldQuantity: Int)

    @Query("UPDATE products SET quantity = quantity + :addedQuantity WHERE id = :id")
    suspend fun increaseProductQuantity(id: Long, addedQuantity: Int)

    @Query("SELECT DISTINCT category FROM products WHERE isActive = 1 ORDER BY category ASC")
    fun getAllCategories(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM products WHERE isActive = 1")
    suspend fun getTotalProductCount(): Int

    @Query("SELECT COUNT(*) FROM products WHERE quantity <= minStockLevel AND isActive = 1")
    suspend fun getLowStockCount(): Int
}
