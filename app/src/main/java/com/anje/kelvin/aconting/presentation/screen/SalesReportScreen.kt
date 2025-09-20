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

data class SalesReportData(
    val id: String,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalValue: Double,
    val customer: String,
    val date: Date
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesReportScreen(
    onNavigateBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Todos") }
    val filterOptions = listOf("Todos", "Hoje", "Esta Semana", "Este Mês")
    
    val salesData = remember { getSampleSalesData() }
    val filteredSales = salesData.filter { sale ->
        when (selectedFilter) {
            "Hoje" -> {
                val today = Calendar.getInstance()
                val saleDay = Calendar.getInstance().apply { time = sale.date }
                today.get(Calendar.DAY_OF_YEAR) == saleDay.get(Calendar.DAY_OF_YEAR) &&
                today.get(Calendar.YEAR) == saleDay.get(Calendar.YEAR)
            }
            "Esta Semana" -> {
                val weekAgo = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }.time
                sale.date.after(weekAgo)
            }
            "Este Mês" -> {
                val monthAgo = Calendar.getInstance().apply { add(Calendar.MONTH, -1) }.time
                sale.date.after(monthAgo)
            }
            else -> true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relatório de Vendas") },
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
            // Summary Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Total Sales
                SummaryCard(
                    title = "Total Vendas",
                    value = filteredSales.sumOf { it.totalValue },
                    color = MaterialTheme.colorScheme.primary,
                    icon = Icons.Default.ShoppingCart,
                    modifier = Modifier.weight(1f)
                )
                
                // Items Sold
                SummaryCard(
                    title = "Itens Vendidos",
                    value = filteredSales.sumOf { it.quantity }.toDouble(),
                    color = MaterialTheme.colorScheme.secondary,
                    icon = Icons.Default.Inventory,
                    modifier = Modifier.weight(1f),
                    isQuantity = true
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

            // Sales List
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredSales) { sale ->
                    SalesReportCard(sale = sale)
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
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    isQuantity: Boolean = false
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
                text = if (isQuantity) "${value.toInt()}" else "MT ${String.format("%.2f", value)}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SalesReportCard(sale: SalesReportData) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = sale.productName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Cliente: ${sale.customer}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Quantidade: ${sale.quantity} × MT ${String.format("%.2f", sale.unitPrice)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = dateFormatter.format(sale.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Text(
                    text = "MT ${String.format("%.2f", sale.totalValue)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

private fun getSampleSalesData(): List<SalesReportData> {
    val calendar = Calendar.getInstance()
    
    return listOf(
        SalesReportData(
            id = "1",
            productName = "Arroz - 5 Kg",
            quantity = 2,
            unitPrice = 300.0,
            totalValue = 600.0,
            customer = "João Silva",
            date = calendar.apply { add(Calendar.HOUR, -2) }.time
        ),
        SalesReportData(
            id = "2",
            productName = "Açúcar - 1 Kg",
            quantity = 5,
            unitPrice = 80.0,
            totalValue = 400.0,
            customer = "Maria Santos",
            date = calendar.apply { add(Calendar.HOUR, -4) }.time
        ),
        SalesReportData(
            id = "3",
            productName = "Óleo de Cozinha - 900ml",
            quantity = 3,
            unitPrice = 120.0,
            totalValue = 360.0,
            customer = "António Pereira",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -1) }.time
        ),
        SalesReportData(
            id = "4",
            productName = "Sabão em Pó - 2 Kg",
            quantity = 1,
            unitPrice = 250.0,
            totalValue = 250.0,
            customer = "Ana Costa",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -2) }.time
        ),
        SalesReportData(
            id = "5",
            productName = "Farinha de Trigo - 1 Kg",
            quantity = 4,
            unitPrice = 60.0,
            totalValue = 240.0,
            customer = "Carlos Mendes",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -3) }.time
        )
    )
}