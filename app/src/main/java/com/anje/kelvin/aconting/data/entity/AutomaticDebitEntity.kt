package com.anje.kelvin.aconting.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "automatic_debits")
data class AutomaticDebitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val idUsuario: Int,
    val descricao: String,
    val valor: Double,
    val dataFim: Date,
    val mensal: Boolean = false
)