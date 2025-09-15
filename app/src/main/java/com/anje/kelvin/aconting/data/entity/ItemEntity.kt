package com.anje.kelvin.aconting.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val idUsuario: Int,
    val nomeItem: String,
    val preco: Double,
    val precoUnidade: Double,
    val unidadeDeMedida: String,
    val numItem: Int,
    val itensDisponiveis: Int
) {
    fun vender(quantidade: Int): ItemEntity {
        return copy(itensDisponiveis = maxOf(0, itensDisponiveis - quantidade))
    }
}