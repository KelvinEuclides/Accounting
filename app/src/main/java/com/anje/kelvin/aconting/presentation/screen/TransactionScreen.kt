package com.anje.kelvin.aconting.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class Transaction(
    val id: String,
    val description: String,
    val value: Double,
    val category: String,
    val date: Date,
    val type: TransactionType
)

enum class TransactionType {
    INCOME, EXPENSE, SALE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    onNavigateBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Todas") }
    val filterOptions = listOf("Todas", "Vendas", "Despesas", "Depósitos")
    
    val transactions = remember { getSampleTransactions() }
    val filteredTransactions = transactions.filter { transaction ->
        when (selectedFilter) {
            "Vendas" -> transaction.type == TransactionType.SALE
            "Despesas" -> transaction.type == TransactionType.EXPENSE
            "Depósitos" -> transaction.type == TransactionType.INCOME
            else -> true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transações") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Summary Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Total Income
                SummaryCard(
                    title = "Entradas",
                    value = transactions.filter { it.type == TransactionType.INCOME || it.type == TransactionType.SALE }
                        .sumOf { it.value },
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                
                // Total Expenses
                SummaryCard(
                    title = "Saídas",
                    value = transactions.filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.value },
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
            }

            // Filter Chips
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { filter ->
                    FilterChip(
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) },
                        selected = selectedFilter == filter
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Transactions List
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTransactions) { transaction ->
                    TransactionItem(transaction = transaction)
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: Double,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = color
            )
            Text(
                text = "MT ${String.format("%.2f", value)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun TransactionItem(
    transaction: Transaction
) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon based on transaction type
            val (icon, iconColor) = when (transaction.type) {
                TransactionType.INCOME -> Icons.Default.TrendingUp to MaterialTheme.colorScheme.primary
                TransactionType.EXPENSE -> Icons.Default.TrendingDown to MaterialTheme.colorScheme.error
                TransactionType.SALE -> Icons.Default.ShoppingCart to MaterialTheme.colorScheme.secondary
            }
            
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Transaction details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = transaction.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Text(
                        text = dateFormatter.format(transaction.date),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Value
            val (valueColor, valuePrefix) = when (transaction.type) {
                TransactionType.INCOME, TransactionType.SALE -> 
                    MaterialTheme.colorScheme.primary to "+"
                TransactionType.EXPENSE -> 
                    MaterialTheme.colorScheme.error to "-"
            }
            
            Text(
                text = "${valuePrefix}MT ${String.format("%.2f", transaction.value)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}

private fun getSampleTransactions(): List<Transaction> {
    val calendar = Calendar.getInstance()
    
    return listOf(
        Transaction(
            id = "1",
            description = "Venda de Arroz - 5 Kg",
            value = 600.0,
            category = "Venda",
            date = calendar.apply { add(Calendar.HOUR, -2) }.time,
            type = TransactionType.SALE
        ),
        Transaction(
            id = "2",
            description = "Compra de Açúcar - 20 Kg",
            value = 1300.0,
            category = "Compra",
            date = calendar.apply { add(Calendar.HOUR, -5) }.time,
            type = TransactionType.EXPENSE
        ),
        Transaction(
            id = "3",
            description = "Depósito - Pagamento de cliente",
            value = 2500.0,
            category = "Depósito",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -1) }.time,
            type = TransactionType.INCOME
        ),
        Transaction(
            id = "4",
            description = "Venda de Óleo - 3 Litros",
            value = 450.0,
            category = "Venda",
            date = calendar.apply { add(Calendar.HOUR, -8) }.time,
            type = TransactionType.SALE
        ),
        Transaction(
            id = "5",
            description = "Despesa - Transporte",
            value = 150.0,
            category = "Transporte",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -2) }.time,
            type = TransactionType.EXPENSE
        ),
        Transaction(
            id = "6",
            description = "Venda de Sabão - 2 Unidades",
            value = 500.0,
            category = "Venda",
            date = calendar.apply { add(Calendar.HOUR, -10) }.time,
            type = TransactionType.SALE
        ),
        Transaction(
            id = "7",
            description = "Despesa - Alimentação",
            value = 300.0,
            category = "Alimentação",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -3) }.time,
            type = TransactionType.EXPENSE
        ),
        Transaction(
            id = "8",
            description = "Depósito inicial",
            value = 5000.0,
            category = "Depósito",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -7) }.time,
            type = TransactionType.INCOME
        )
    ).sortedByDescending { it.date }
}