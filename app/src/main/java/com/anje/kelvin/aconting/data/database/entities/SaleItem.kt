package com.anje.kelvin.aconting.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "sale_items",
    foreignKeys = [
        ForeignKey(
            entity = Sale::class,
            parentColumns = ["id"],
            childColumns = ["saleId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["saleId"]),
        Index(value = ["productId"])
    ]
)
data class SaleItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val saleId: Long,
    val productId: Long,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int,
    val subtotal: Double,
    val discountPercent: Double = 0.0,
    val discountAmount: Double = 0.0,
    val finalAmount: Double
) : Parcelable {
    constructor(
        saleId: Long,
        productId: Long,
        productName: String,
        unitPrice: Double,
        quantity: Int,
        discountPercent: Double = 0.0
    ) : this(
        id = 0,
        saleId = saleId,
        productId = productId,
        productName = productName,
        unitPrice = unitPrice,
        quantity = quantity,
        subtotal = unitPrice * quantity,
        discountPercent = discountPercent,
        discountAmount = (unitPrice * quantity) * (discountPercent / 100),
        finalAmount = (unitPrice * quantity) * (1 - discountPercent / 100)
    )
}