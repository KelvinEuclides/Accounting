package com.anje.kelvin.aconting.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(
    tableName = "categories",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["type"]),
        Index(value = ["name"])
    ]
)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val name: String,
    val type: CategoryType,
    val description: String? = null,
    val color: String? = null, // Hex color code
    val icon: String? = null, // Icon name or unicode
    val isActive: Boolean = true,
    val isDefault: Boolean = false, // System default categories
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) : Parcelable

enum class CategoryType {
    INCOME,
    EXPENSE,
    PRODUCT
}