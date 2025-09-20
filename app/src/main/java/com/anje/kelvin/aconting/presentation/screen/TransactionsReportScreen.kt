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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsReportScreen(
    onNavigateBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Todas") }
    var selectedType by remember { mutableStateOf("Todos") }
    
    val filterOptions = listOf("Todas", "Hoje", "Esta Semana", "Este Mês")
    val typeOptions = listOf("Todos", "Vendas", "Despesas", "Depósitos")
    
    val transactions = remember { getSampleTransactionsForReport() }
    val filteredTransactions = transactions.filter { transaction ->
        val dateFilter = when (selectedFilter) {
            "Hoje" -> {
                val today = Calendar.getInstance()
                val transactionDay = Calendar.getInstance().apply { time = transaction.date }
                today.get(Calendar.DAY_OF_YEAR) == transactionDay.get(Calendar.DAY_OF_YEAR) &&
                today.get(Calendar.YEAR) == transactionDay.get(Calendar.YEAR)
            }
            "Esta Semana" -> {
                val weekAgo = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }.time
                transaction.date.after(weekAgo)
            }
            "Este Mês" -> {
                val monthAgo = Calendar.getInstance().apply { add(Calendar.MONTH, -1) }.time
                transaction.date.after(monthAgo)
            }
            else -> true
        }
        
        val typeFilter = when (selectedType) {
            "Vendas" -> transaction.type == TransactionType.SALE
            "Despesas" -> transaction.type == TransactionType.EXPENSE
            "Depósitos" -> transaction.type == TransactionType.INCOME
            else -> true
        }
        
        dateFilter && typeFilter
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relatório de Transações") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Export functionality */ }) {
                        Icon(Icons.Default.FileDownload, contentDescription = "Exportar")
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
            // Summary Cards Row 1
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Total Income (Sales + Deposits)
                TransactionSummaryCard(
                    title = "Total Entradas",
                    value = filteredTransactions.filter { 
                        it.type == TransactionType.INCOME || it.type == TransactionType.SALE 
                    }.sumOf { it.value },
                    color = MaterialTheme.colorScheme.primary,
                    icon = Icons.Default.TrendingUp,
                    modifier = Modifier.weight(1f)
                )
                
                // Total Expenses
                TransactionSummaryCard(
                    title = "Total Saídas",
                    value = filteredTransactions.filter { 
                        it.type == TransactionType.EXPENSE 
                    }.sumOf { it.value },
                    color = MaterialTheme.colorScheme.error,
                    icon = Icons.Default.TrendingDown,
                    modifier = Modifier.weight(1f)
                )
            }

            // Summary Cards Row 2
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Net Flow
                val netFlow = filteredTransactions.filter { 
                    it.type == TransactionType.INCOME || it.type == TransactionType.SALE 
                }.sumOf { it.value } - filteredTransactions.filter { 
                    it.type == TransactionType.EXPENSE 
                }.sumOf { it.value }
                
                TransactionSummaryCard(
                    title = "Fluxo Líquido",
                    value = netFlow,
                    color = if (netFlow >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    icon = if (netFlow >= 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                    modifier = Modifier.weight(1f)
                )
                
                // Total Transactions
                TransactionSummaryCard(
                    title = "Total Transações",
                    value = filteredTransactions.size.toDouble(),
                    color = MaterialTheme.colorScheme.secondary,
                    icon = Icons.Default.CompareArrows,
                    modifier = Modifier.weight(1f),
                    isCount = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Chips - Date
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

            // Filter Chips - Type
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(typeOptions) { type ->
                    FilterChip(
                        onClick = { selectedType = type },
                        label = { Text(type) },
                        selected = selectedType == type
                    )
                }
            }

            // Transactions List
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTransactions) { transaction ->
                    TransactionReportCard(transaction = transaction)
                }
            }
        }
    }
}

@Composable
private fun TransactionSummaryCard(
    title: String,
    value: Double,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    isCount: Boolean = false
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
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = color
            )
            Text(
                text = if (isCount) "${value.toInt()}" else "MT ${String.format("%.2f", value)}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionReportCard(transaction: Transaction) {
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
            // Transaction Type Icon
            val (icon, iconColor) = when (transaction.type) {
                TransactionType.SALE -> Icons.Default.ShoppingCart to MaterialTheme.colorScheme.primary
                TransactionType.INCOME -> Icons.Default.TrendingUp to MaterialTheme.colorScheme.primary
                TransactionType.EXPENSE -> Icons.Default.TrendingDown to MaterialTheme.colorScheme.error
            }
            
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Categoria: ${transaction.category}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = dateFormatter.format(transaction.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
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

private fun getSampleTransactionsForReport(): List<Transaction> {
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
            value = 800.0,
            category = "Compras",
            date = calendar.apply { add(Calendar.HOUR, -5) }.time,
            type = TransactionType.EXPENSE
        ),
        Transaction(
            id = "3",
            description = "Venda de Óleo - 3 Unidades",
            value = 360.0,
            category = "Venda",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -1) }.time,
            type = TransactionType.SALE
        ),
        Transaction(
            id = "4",
            description = "Depósito inicial",
            value = 5000.0,
            category = "Depósito",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -2) }.time,
            type = TransactionType.INCOME
        ),
        Transaction(
            id = "5",
            description = "Despesa - Alimentação",
            value = 300.0,
            category = "Alimentação",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -3) }.time,
            type = TransactionType.EXPENSE
        ),
        Transaction(
            id = "6",
            description = "Venda de Sabão - 2 Unidades",
            value = 500.0,
            category = "Venda",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -4) }.time,
            type = TransactionType.SALE
        ),
        Transaction(
            id = "7",
            description = "Pagamento de fornecedor",
            value = 1500.0,
            category = "Fornecedores",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -5) }.time,
            type = TransactionType.EXPENSE
        ),
        Transaction(
            id = "8",
            description = "Venda de Farinha - 4 Kg",
            value = 240.0,
            category = "Venda",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -6) }.time,
            type = TransactionType.SALE
        )
    )
}