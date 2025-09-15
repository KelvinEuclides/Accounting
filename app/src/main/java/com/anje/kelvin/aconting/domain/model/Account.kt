package com.anje.kelvin.aconting.domain.model

import java.util.Date

data class Account(
    val idUsuario: Int,
    val nomeConta: String,
    val saldoConta: Double,
    val loggado: Boolean,
    val telemovel: Int,
    val pin: Int,
    val notificacoes: Boolean,
    val alertas: Boolean
)