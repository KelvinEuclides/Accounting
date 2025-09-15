package com.anje.kelvin.aconting.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "sales")
data class SaleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val idUsuario: Int,
    val valor: Double,
    val valorIva: Double,
    val venda: String = "Venda",
    val itensVendidos: Int,
    val data: Date
)