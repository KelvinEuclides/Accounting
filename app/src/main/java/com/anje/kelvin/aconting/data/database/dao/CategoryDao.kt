package com.anje.kelvin.aconting.data.database.dao

import androidx.room.*
import com.anje.kelvin.aconting.data.database.entities.Category
import com.anje.kelvin.aconting.data.database.entities.CategoryType
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveCategories(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE isActive = 1 ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE userId = :userId AND isActive = 1 ORDER BY name ASC")
    fun getActiveCategoriesByUser(userId: Long): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE type = :type AND isActive = 1 ORDER BY name ASC")
    fun getActiveCategoriesByType(type: CategoryType): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE userId = :userId AND type = :type AND isActive = 1 ORDER BY name ASC")
    fun getCategoriesByUserAndType(userId: Long, type: CategoryType): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE userId = :userId AND type = :type AND isActive = 1 ORDER BY name ASC")
    fun getActiveCategoriesByUserAndType(userId: Long, type: CategoryType): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE userId = :userId AND type = :type AND isActive = 1 ORDER BY name ASC")
    fun getCategoriesByUserAndType(userId: Long, type: String): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Long): Category?

    @Query("SELECT * FROM categories WHERE name = :name AND userId = :userId")
    suspend fun getCategoryByNameAndUser(name: String, userId: Long): Category?

    @Query("SELECT * FROM categories WHERE isDefault = 1 ORDER BY name ASC")
    suspend fun getDefaultCategories(): List<Category>

    @Query("SELECT * FROM categories WHERE isDefault = 1 AND type = :type ORDER BY name ASC")
    suspend fun getDefaultCategoriesByType(type: CategoryType): List<Category>

    @Insert
    suspend fun insertCategory(category: Category): Long

    @Insert
    suspend fun insertCategories(categories: List<Category>): List<Long>

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("UPDATE categories SET isActive = 0 WHERE id = :categoryId")
    suspend fun deactivateCategory(categoryId: Long)

    @Query("UPDATE categories SET isActive = 1 WHERE id = :categoryId")
    suspend fun activateCategory(categoryId: Long)

    @Query("DELETE FROM categories WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: Long)

    @Query("SELECT * FROM categories WHERE name LIKE '%' || :searchQuery || '%' AND isActive = 1 ORDER BY name ASC")
    fun searchCategories(searchQuery: String): Flow<List<Category>>

    @Query("SELECT COUNT(*) FROM categories WHERE name = :name AND userId = :userId AND isActive = 1")
    suspend fun checkCategoryNameExists(name: String, userId: Long): Int
}