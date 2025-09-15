package com.anje.kelvin.aconting.data.repository

import com.anje.kelvin.aconting.data.database.dao.ProductDao
import com.anje.kelvin.aconting.data.database.entities.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {

    fun getAllActiveProducts(): Flow<List<Product>> = productDao.getAllActiveProducts()

    fun getAllProducts(): Flow<List<Product>> = productDao.getAllProducts()

    fun getLowStockProducts(): Flow<List<Product>> = productDao.getLowStockProducts()

    fun getOutOfStockProducts(): Flow<List<Product>> = productDao.getOutOfStockProducts()

    fun getProductsByCategory(category: String): Flow<List<Product>> =
        productDao.getProductsByCategory(category)

    fun searchProducts(searchQuery: String): Flow<List<Product>> =
        productDao.searchProducts(searchQuery)

    suspend fun getProductById(id: Long): Product? = productDao.getProductById(id)

    suspend fun getProductByBarcode(barcode: String): Product? =
        productDao.getProductByBarcode(barcode)

    suspend fun insertProduct(product: Product): Long = productDao.insertProduct(product)

    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)

    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)

    suspend fun deactivateProduct(id: Long) = productDao.deactivateProduct(id)

    suspend fun reduceProductQuantity(id: Long, soldQuantity: Int) =
        productDao.reduceProductQuantity(id, soldQuantity)

    suspend fun increaseProductQuantity(id: Long, addedQuantity: Int) =
        productDao.increaseProductQuantity(id, addedQuantity)

    fun getAllCategories(): Flow<List<String>> = productDao.getAllCategories()

    suspend fun getTotalProductCount(): Int = productDao.getTotalProductCount()

    suspend fun getLowStockCount(): Int = productDao.getLowStockCount()
}
