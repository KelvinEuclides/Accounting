package com.anje.kelvin.aconting.domain.model

import java.util.Date

data class Transaction(
    val id: Long,
    val idUsuario: Int,
    val descricao: String,
    val valor: Double,
    val categoria: String,
    val dia: Date,
    val recorrencia: String
)