package com.anje.kelvin.aconting.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val price: Double,
    val quantity: Int,
    val minStockLevel: Int = 0,
    val category: String = "General",
    val barcode: String? = null,
    val imageUrl: String? = null,
    val isActive: Boolean = true,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) : Parcelable {
    val isLowStock: Boolean
        get() = quantity <= minStockLevel

    val isOutOfStock: Boolean
        get() = quantity <= 0
}
