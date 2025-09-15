package com.anje.kelvin.aconting.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val description: String,
    val category: String,
    val type: TransactionType,
    val date: Date,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) : Parcelable

enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}
