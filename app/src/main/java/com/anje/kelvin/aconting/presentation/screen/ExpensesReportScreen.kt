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

data class ExpenseReportData(
    val id: String,
    val description: String,
    val amount: Double,
    val category: String,
    val supplier: String,
    val date: Date,
    val paymentMethod: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesReportScreen(
    onNavigateBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Todos") }
    var selectedCategory by remember { mutableStateOf("Todas") }
    
    val filterOptions = listOf("Todos", "Hoje", "Esta Semana", "Este Mês")
    val categoryOptions = listOf("Todas", "Alimentação", "Transporte", "Fornecedores", "Salários", "Outros")
    
    val expensesData = remember { getSampleExpensesData() }
    val filteredExpenses = expensesData.filter { expense ->
        val dateFilter = when (selectedFilter) {
            "Hoje" -> {
                val today = Calendar.getInstance()
                val expenseDay = Calendar.getInstance().apply { time = expense.date }
                today.get(Calendar.DAY_OF_YEAR) == expenseDay.get(Calendar.DAY_OF_YEAR) &&
                today.get(Calendar.YEAR) == expenseDay.get(Calendar.YEAR)
            }
            "Esta Semana" -> {
                val weekAgo = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }.time
                expense.date.after(weekAgo)
            }
            "Este Mês" -> {
                val monthAgo = Calendar.getInstance().apply { add(Calendar.MONTH, -1) }.time
                expense.date.after(monthAgo)
            }
            else -> true
        }
        
        val categoryFilter = selectedCategory == "Todas" || expense.category == selectedCategory
        
        dateFilter && categoryFilter
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relatório de Despesas") },
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
                // Total Expenses
                ExpenseSummaryCard(
                    title = "Total Despesas",
                    value = filteredExpenses.sumOf { it.amount },
                    color = MaterialTheme.colorScheme.error,
                    icon = Icons.Default.TrendingDown,
                    modifier = Modifier.weight(1f)
                )
                
                // Number of Expenses
                ExpenseSummaryCard(
                    title = "Nº Despesas",
                    value = filteredExpenses.size.toDouble(),
                    color = MaterialTheme.colorScheme.secondary,
                    icon = Icons.Default.Receipt,
                    modifier = Modifier.weight(1f),
                    isCount = true
                )
            }

            // Category breakdown
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val topCategory = filteredExpenses.groupBy { it.category }
                    .maxByOrNull { it.value.sumOf { expense -> expense.amount } }?.key ?: "N/A"
                    
                ExpenseSummaryCard(
                    title = "Categoria Principal",
                    value = 0.0,
                    color = MaterialTheme.colorScheme.tertiary,
                    icon = Icons.Default.Category,
                    modifier = Modifier.weight(1f),
                    customText = topCategory
                )
                
                val avgExpense = if (filteredExpenses.isNotEmpty()) 
                    filteredExpenses.sumOf { it.amount } / filteredExpenses.size 
                else 0.0
                    
                ExpenseSummaryCard(
                    title = "Média por Despesa",
                    value = avgExpense,
                    color = MaterialTheme.colorScheme.primary,
                    icon = Icons.Default.Analytics,
                    modifier = Modifier.weight(1f)
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

            // Filter Chips - Category
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categoryOptions) { category ->
                    FilterChip(
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        selected = selectedCategory == category
                    )
                }
            }

            // Expenses List
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredExpenses) { expense ->
                    ExpenseReportCard(expense = expense)
                }
            }
        }
    }
}

@Composable
private fun ExpenseSummaryCard(
    title: String,
    value: Double,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    isCount: Boolean = false,
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
                text = customText ?: if (isCount) "${value.toInt()}" else "MT ${String.format("%.2f", value)}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpenseReportCard(expense: ExpenseReportData) {
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
                        text = expense.description,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Categoria: ${expense.category}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Fornecedor: ${expense.supplier}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Pagamento: ${expense.paymentMethod}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = dateFormatter.format(expense.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Text(
                    text = "MT ${String.format("%.2f", expense.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun getSampleExpensesData(): List<ExpenseReportData> {
    val calendar = Calendar.getInstance()
    
    return listOf(
        ExpenseReportData(
            id = "1",
            description = "Compra de produtos para revenda",
            amount = 1500.0,
            category = "Fornecedores",
            supplier = "Fornecedor ABC",
            date = calendar.apply { add(Calendar.HOUR, -1) }.time,
            paymentMethod = "Transferência"
        ),
        ExpenseReportData(
            id = "2",
            description = "Almoço de equipe",
            amount = 300.0,
            category = "Alimentação",
            supplier = "Restaurante XYZ",
            date = calendar.apply { add(Calendar.HOUR, -3) }.time,
            paymentMethod = "Dinheiro"
        ),
        ExpenseReportData(
            id = "3",
            description = "Combustível para transporte",
            amount = 800.0,
            category = "Transporte",
            supplier = "Posto de Combustível",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -1) }.time,
            paymentMethod = "Cartão"
        ),
        ExpenseReportData(
            id = "4",
            description = "Salário funcionário",
            amount = 5000.0,
            category = "Salários",
            supplier = "João da Silva",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -5) }.time,
            paymentMethod = "Transferência"
        ),
        ExpenseReportData(
            id = "5",
            description = "Material de escritório",
            amount = 200.0,
            category = "Outros",
            supplier = "Papelaria Central",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -7) }.time,
            paymentMethod = "Dinheiro"
        ),
        ExpenseReportData(
            id = "6",
            description = "Internet e telefone",
            amount = 450.0,
            category = "Outros",
            supplier = "Operadora Telecom",
            date = calendar.apply { add(Calendar.DAY_OF_MONTH, -10) }.time,
            paymentMethod = "Débito Automático"
        )
    )
}