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

data class ActivityReportData(
    val id: String,
    val activityType: ActivityType,
    val description: String,
    val details: String,
    val user: String,
    val timestamp: Date,
    val status: String
)

enum class ActivityType {
    LOGIN, SALE, EXPENSE, INVENTORY_UPDATE, REPORT_GENERATED, BACKUP, SETTINGS_CHANGE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesReportScreen(
    onNavigateBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Todas") }
    var selectedActivity by remember { mutableStateOf("Todas") }
    
    val filterOptions = listOf("Todas", "Hoje", "Esta Semana", "Este Mês")
    val activityOptions = listOf("Todas", "Vendas", "Estoque", "Relatórios", "Sistema", "Login")
    
    val activities = remember { getSampleActivitiesData() }
    val filteredActivities = activities.filter { activity ->
        val dateFilter = when (selectedFilter) {
            "Hoje" -> {
                val today = Calendar.getInstance()
                val activityDay = Calendar.getInstance().apply { time = activity.timestamp }
                today.get(Calendar.DAY_OF_YEAR) == activityDay.get(Calendar.DAY_OF_YEAR) &&
                today.get(Calendar.YEAR) == activityDay.get(Calendar.YEAR)
            }
            "Esta Semana" -> {
                val weekAgo = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }.time
                activity.timestamp.after(weekAgo)
            }
            "Este Mês" -> {
                val monthAgo = Calendar.getInstance().apply { add(Calendar.MONTH, -1) }.time
                activity.timestamp.after(monthAgo)
            }
            else -> true
        }
        
        val activityFilter = when (selectedActivity) {
            "Vendas" -> activity.activityType == ActivityType.SALE
            "Estoque" -> activity.activityType == ActivityType.INVENTORY_UPDATE
            "Relatórios" -> activity.activityType == ActivityType.REPORT_GENERATED
            "Sistema" -> activity.activityType in listOf(ActivityType.BACKUP, ActivityType.SETTINGS_CHANGE)
            "Login" -> activity.activityType == ActivityType.LOGIN
            else -> true
        }
        
        dateFilter && activityFilter
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relatório de Actividades") },
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
                // Total Activities
                ActivitySummaryCard(
                    title = "Total Actividades",
                    value = filteredActivities.size.toDouble(),
                    color = MaterialTheme.colorScheme.primary,
                    icon = Icons.Default.Analytics,
                    modifier = Modifier.weight(1f),
                    isCount = true
                )
                
                // Most Active User
                val mostActiveUser = filteredActivities.groupBy { it.user }
                    .maxByOrNull { it.value.size }?.key ?: "N/A"
                    
                ActivitySummaryCard(
                    title = "Utilizador Mais Activo",
                    value = 0.0,
                    color = MaterialTheme.colorScheme.secondary,
                    icon = Icons.Default.Person,
                    modifier = Modifier.weight(1f),
                    customText = mostActiveUser
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Sales Activities
                val salesCount = filteredActivities.count { it.activityType == ActivityType.SALE }
                ActivitySummaryCard(
                    title = "Vendas Realizadas",
                    value = salesCount.toDouble(),
                    color = MaterialTheme.colorScheme.tertiary,
                    icon = Icons.Default.ShoppingCart,
                    modifier = Modifier.weight(1f),
                    isCount = true
                )
                
                // System Activities
                val systemCount = filteredActivities.count { 
                    it.activityType in listOf(ActivityType.BACKUP, ActivityType.SETTINGS_CHANGE, ActivityType.LOGIN)
                }
                ActivitySummaryCard(
                    title = "Actividades Sistema",
                    value = systemCount.toDouble(),
                    color = MaterialTheme.colorScheme.error,
                    icon = Icons.Default.Settings,
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

            // Filter Chips - Activity Type
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(activityOptions) { activity ->
                    FilterChip(
                        onClick = { selectedActivity = activity },
                        label = { Text(activity) },
                        selected = selectedActivity == activity
                    )
                }
            }

            // Activities List
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredActivities) { activity ->
                    ActivityReportCard(activity = activity)
                }
            }
        }
    }
}

@Composable
private fun ActivitySummaryCard(
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
private fun ActivityReportCard(activity: ActivityReportData) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    
    val (icon, iconColor) = when (activity.activityType) {
        ActivityType.LOGIN -> Icons.Default.Login to MaterialTheme.colorScheme.primary
        ActivityType.SALE -> Icons.Default.ShoppingCart to MaterialTheme.colorScheme.primary
        ActivityType.EXPENSE -> Icons.Default.TrendingDown to MaterialTheme.colorScheme.error
        ActivityType.INVENTORY_UPDATE -> Icons.Default.Inventory to MaterialTheme.colorScheme.tertiary
        ActivityType.REPORT_GENERATED -> Icons.Default.Assessment to MaterialTheme.colorScheme.secondary
        ActivityType.BACKUP -> Icons.Default.CloudUpload to MaterialTheme.colorScheme.secondary
        ActivityType.SETTINGS_CHANGE -> Icons.Default.Settings to MaterialTheme.colorScheme.error
    }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                    text = activity.description,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = activity.details,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Utilizador: ${activity.user}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = dateFormatter.format(activity.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = when (activity.status) {
                        "Sucesso" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        "Erro" -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                        else -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                    }
                )
            ) {
                Text(
                    text = activity.status,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = when (activity.status) {
                        "Sucesso" -> MaterialTheme.colorScheme.primary
                        "Erro" -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.secondary
                    },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

private fun getSampleActivitiesData(): List<ActivityReportData> {
    val calendar = Calendar.getInstance()
    
    return listOf(
        ActivityReportData(
            id = "1",
            activityType = ActivityType.SALE,
            description = "Venda realizada",
            details = "Arroz - 5 Kg para João Silva",
            user = "Admin",
            timestamp = calendar.apply { add(Calendar.MINUTE, -30) }.time,
            status = "Sucesso"
        ),
        ActivityReportData(
            id = "2",
            activityType = ActivityType.INVENTORY_UPDATE,
            description = "Actualização de estoque",
            details = "Adicionado 50 unidades de Açúcar",
            user = "Admin",
            timestamp = calendar.apply { add(Calendar.HOUR, -1) }.time,
            status = "Sucesso"
        ),
        ActivityReportData(
            id = "3",
            activityType = ActivityType.LOGIN,
            description = "Início de sessão",
            details = "Login realizado com sucesso",
            user = "Admin",
            timestamp = calendar.apply { add(Calendar.HOUR, -2) }.time,
            status = "Sucesso"
        ),
        ActivityReportData(
            id = "4",
            activityType = ActivityType.EXPENSE,
            description = "Despesa registada",
            details = "Compra de material de escritório",
            user = "Admin",
            timestamp = calendar.apply { add(Calendar.HOUR, -3) }.time,
            status = "Sucesso"
        ),
        ActivityReportData(
            id = "5",
            activityType = ActivityType.REPORT_GENERATED,
            description = "Relatório gerado",
            details = "Relatório de vendas mensal",
            user = "Admin",
            timestamp = calendar.apply { add(Calendar.DAY_OF_MONTH, -1) }.time,
            status = "Sucesso"
        ),
        ActivityReportData(
            id = "6",
            activityType = ActivityType.BACKUP,
            description = "Backup realizado",
            details = "Backup automático dos dados",
            user = "Sistema",
            timestamp = calendar.apply { add(Calendar.DAY_OF_MONTH, -2) }.time,
            status = "Sucesso"
        ),
        ActivityReportData(
            id = "7",
            activityType = ActivityType.SETTINGS_CHANGE,
            description = "Configuração alterada",
            details = "Limite de estoque actualizado",
            user = "Admin",
            timestamp = calendar.apply { add(Calendar.DAY_OF_MONTH, -3) }.time,
            status = "Sucesso"
        ),
        ActivityReportData(
            id = "8",
            activityType = ActivityType.SALE,
            description = "Tentativa de venda",
            details = "Falha na venda - produto indisponível",
            user = "Admin",
            timestamp = calendar.apply { add(Calendar.DAY_OF_MONTH, -4) }.time,
            status = "Erro"
        )
    )
}