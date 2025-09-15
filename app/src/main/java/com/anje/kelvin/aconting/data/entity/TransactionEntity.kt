package com.anje.kelvin.aconting.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val idUsuario: Int,
    val descricao: String,
    val valor: Double,
    val categoria: String,
    val dia: Date,
    val recorrencia: String = ""
)