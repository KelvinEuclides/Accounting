package com.anje.kelvin.aconting.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey
    val idUsuario: Int,
    val nomeConta: String,
    val saldoConta: Double = 0.0,
    val loggado: Boolean = false,
    val telemovel: Int,
    val pin: Int,
    val notificacoes: Boolean = true,
    val alertas: Boolean = true
) {
    fun adicionarItem(valor: Double): AccountEntity {
        return copy(saldoConta = saldoConta - valor)
    }
    
    fun adicionarDeposito(valor: Double): AccountEntity {
        return copy(saldoConta = saldoConta + valor)
    }
    
    fun adicionarDespesa(valor: Double): AccountEntity {
        return copy(saldoConta = saldoConta - valor)
    }
}