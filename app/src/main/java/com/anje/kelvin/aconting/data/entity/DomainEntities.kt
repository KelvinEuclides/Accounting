package com.anje.kelvin.aconting.data.entity

import java.util.*

data class Item(
    val id: String,
    val name: String,
    val totalQuantity: Int,
    val availableQuantity: Int,
    val purchasePrice: Double,
    val salePrice: Double,
    val unit: String,
    val userId: String,
    val createdAt: Date
)

data class Transaction(
    val id: String,
    val description: String,
    val amount: Double,
    val type: String, // SALE, EXPENSE, DEPOSIT
    val category: String,
    val date: Date,
    val userId: String
)

data class Expense(
    val id: String,
    val description: String,
    val amount: Double,
    val category: String,
    val recurrence: String, // Nenhuma, Fixa
    val startDate: Date,
    val endDate: Date?,
    val userId: String,
    val createdAt: Date
)