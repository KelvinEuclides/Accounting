package com.anje.kelvin.aconting.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val idUsuario: Int,
    val notificacoes: Boolean = true,
    val alertas: Boolean = true,
    val registarReceitas: Boolean = true,
    val requisitarPin: Boolean = false
)