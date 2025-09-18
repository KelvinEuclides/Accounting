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

data class IncomeReportData(
    val id: String,
    val description: String,
    val amount: Double,
    val source: String,
    val category: String,
    val date: Date,
    val incomeType: IncomeType
)

enum class IncomeType {
    SALES, DEPOSITS, SERVICES, INVESTMENTS, OTHER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeReportScreen(
    onNavigateBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Todos") }
    var selectedType by remember { mutableStateOf("Todos") }
    
    val filterOptions = listOf("Todos", "Hoje", "Esta Semana", "Este Mês")
    val typeOptions = listOf("Todos", "Vendas", "Depósitos", "Serviços", "Investimentos", "Outros")
    
    val incomeData = remember { getSampleIncomeData() }
    val filteredIncome = incomeData.filter { income ->
        val dateFilter = when (selectedFilter) {
            "Hoje" -> {
                val today = Calendar.getInstance()
                val incomeDay = Calendar.getInstance().apply { time = income.date }
                today.get(Calendar.DAY_OF_YEAR) == incomeDay.get(Calendar.DAY_OF_YEAR) &&
                today.get(Calendar.YEAR) == incomeDay.get(Calendar.YEAR)
            }
            "Esta Semana" -> {
                val weekAgo = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }.time
                income.date.after(weekAgo)
            }
            "Este Mês" -> {
                val monthAgo = Calendar.getInstance().apply { add(Calendar.MONTH, -1) }.time
                income.date.after(monthAgo)
            }
            else -> true
        }
        
        val typeFilter = when (selectedType) {
            "Vendas" -> income.incomeType == IncomeType.SALES
            "Depósitos" -> income.incomeType == IncomeType.DEPOSITS
            "Serviços" -> income.incomeType == IncomeType.SERVICES
            "Investimentos" -> income.incomeType == IncomeType.INVESTMENTS
            "Outros" -> income.incomeType == IncomeType.OTHER
            else -> true
        }
        
        dateFilter && typeFilter
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relatório de Receitas") },
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
                // Total Income
                IncomeSummaryCard(
                    title = "Total Receitas",
                    value = filteredIncome.sumOf { it.amount },
                    color = MaterialTheme.colorScheme.primary,
                    icon = Icons.Default.TrendingUp,
                    modifier = Modifier.weight(1f)
                )
                
                // Average Income
                val avgIncome = if (filteredIncome.isNotEmpty()) 
                    filteredIncome.sumOf { it.amount } / filteredIncome.size 
                else 0.0
                
                IncomeSummaryCard(
                    title = "Receita Média",
                    value = avgIncome,
                    color = MaterialTheme.colorScheme.secondary,
                    icon = Icons.Default.Analytics,
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
                // Sales Income
                val salesIncome = filteredIncome.filter { it.incomeType == IncomeType.SALES }
                    .sumOf { it.amount }
                
                IncomeSummaryCard(
                    title = "Receitas Vendas",
                    value = salesIncome,
                    color = MaterialTheme.colorScheme.tertiary,
                    icon = Icons.Default.ShoppingCart,
                    modifier = Modifier.weight(1f)
                )
                
                // Main Source
                val mainSource = filteredIncome.groupBy { it.source }
                    .maxByOrNull { it.value.sumOf { income -> income.amount } }?.key ?: "N/A"
                
                IncomeSummaryCard(
                    title = "Fonte Principal",
                    value = 0.0,
                    color = MaterialTheme.colorScheme.error,
                    icon = Icons.Default.AccountBalance,
                    modifier = Modifier.weight(1f),
                    customText = mainSource
                )
            }

            // Profit Analysis
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Monthly Growth (Mock calculation)
                val monthlyGrowth = 12.5 // Mock percentage
                
                IncomeSummaryCard(
                    title = "Crescimento Mensal",
                    value = monthlyGrowth,
                    color = MaterialTheme.colorScheme.primary,
                    icon = Icons.Default.TrendingUp,
                    modifier = Modifier.weight(1f),
                    isPercentage = true
                )
                
                // Number of Income Sources
                IncomeSummaryCard(
                    title = "Fontes de Receita",
                    value = filteredIncome.distinctBy { it.source }.size.toDouble(),
                    color = MaterialTheme.colorScheme.secondary,
                    icon = Icons.Default.AccountTree,
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

            // Income List
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredIncome) { income ->
                    IncomeReportCard(income = income)
                }
            }
        }
    }
}

@Composable
private fun IncomeSummaryCard(
    title: String,
    value: Double,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    isCount: Boolean = false,
    isPercentage: Boolean = false,
    customText: String? = null
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
                text = customText ?: when {
                    isCount -> "${value.toInt()}"
                    isPercentage -> "${String.format("%.1f", value)}%"
                    else -> "MT ${String.format("%.2f", value)}"
                },
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IncomeReportCard(income: IncomeReportData) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    
    val (icon, iconColor) = when (income.incomeType) {
        IncomeType.SALES -> Icons.Default.ShoppingCart to MaterialTheme.colorScheme.primary
        IncomeType.DEPOSITS -> Icons.Default.AccountBalance to MaterialTheme.colorScheme.primary
        IncomeType.SERVICES -> Icons.Default.Build to MaterialTheme.colorScheme.secondary
        IncomeType.INVESTMENTS -> Icons.Default.TrendingUp to MaterialTheme.colorScheme.tertiary
        IncomeType.OTHER -> Icons.Default.Payments to MaterialTheme.colorScheme.error
    }
    
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(32.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = income.description,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Fonte: ${income.source}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Categoria: ${income.category}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = dateFormatter.format(income.date),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Text(
                    text = "MT ${String.format("%.2f", income.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

private fun getSampleIncomeData(): List<IncomeReportData> {
    val calendar = Calendar.getInstance()
    
    return listOf(
        IncomeReportData(
            id = "1",
            description = "Vendas do dia",
            amount = 2400.0,
            source = "Loja Física",
            category = "Vendas Diárias",
            date = calendar.apply { add(Calendar.HOUR, -2) }.time,
            incomeType = IncomeType.SALES
        ),
        IncomeReportData(
            id = "2",
            description = "Depósito inicial do mês",
            amount = 10000.0,
            source = "Capital Próprio",
            category = "Investimento",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -1) }.time,
            incomeType = IncomeType.DEPOSITS
        ),
        IncomeReportData(
            id = "3",
            description = "Serviço de delivery",
            amount = 150.0,
            source = "Taxa de Entrega",
            category = "Serviços",
            date = calendar.apply { add(Calendar.HOUR, -4) }.time,
            incomeType = IncomeType.SERVICES
        ),
        IncomeReportData(
            id = "4",
            description = "Venda por encomenda",
            amount = 850.0,
            source = "Cliente VIP",
            category = "Vendas Especiais",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -2) }.time,
            incomeType = IncomeType.SALES
        ),
        IncomeReportData(
            id = "5",
            description = "Rendimento de aplicação",
            amount = 200.0,
            source = "Banco Central",
            category = "Investimentos",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -5) }.time,
            incomeType = IncomeType.INVESTMENTS
        ),
        IncomeReportData(
            id = "6",
            description = "Vendas online",
            amount = 680.0,
            source = "Plataforma Digital",
            category = "E-commerce",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -3) }.time,
            incomeType = IncomeType.SALES
        ),
        IncomeReportData(
            id = "7",
            description = "Consulta de gestão",
            amount = 500.0,
            source = "Cliente Empresarial",
            category = "Consultoria",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -7) }.time,
            incomeType = IncomeType.SERVICES
        ),
        IncomeReportData(
            id = "8",
            description = "Bonificação de fornecedor",
            amount = 300.0,
            source = "Fornecedor ABC",
            category = "Bonificações",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -10) }.time,
            incomeType = IncomeType.OTHER
        )
    )
}