package com.anje.kelvin.aconting.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.anje.kelvin.aconting.util.AppConstants
import java.util.Date

@Parcelize
@Entity(
    tableName = "deposits",
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
        Index(value = ["category"]),
        Index(value = ["depositDate"])
    ]
)
data class Deposit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val amount: Double,
    val description: String,
    val category: String,
    val paymentMethod: String = AppConstants.PaymentMethods.DEFAULT,
    val source: String? = null,
    val referenceNumber: String? = null,
    val notes: String? = null,
    val depositDate: Date = Date(),
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) : Parcelable