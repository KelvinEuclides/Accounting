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
    tableName = "sales",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["saleDate"]),
        Index(value = ["status"])
    ]
)
data class Sale(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val totalAmount: Double,
    val taxAmount: Double = 0.0,
    val finalAmount: Double,
    val itemCount: Int,
    val paymentMethod: String = "Cash",
    val customerName: String? = null,
    val customerPhone: String? = null,
    val status: SaleStatus = SaleStatus.COMPLETED,
    val notes: String? = null,
    val saleDate: Date = Date(),
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) : Parcelable

enum class SaleStatus {
    PENDING,
    COMPLETED,
    CANCELLED,
    REFUNDED
}