package com.anje.kelvin.aconting.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.anje.kelvin.aconting.ui.components.TransactionList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseReportsScreen(
    onBackClick: () -> Unit
) {
    var selectedPeriod by remember { mutableStateOf("Diário") }
    val periods = listOf("Diário", "Semanal", "Mensal", "Anual")
    
    var startDate by remember { mutableStateOf(LocalDate.now().minusDays(7)) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }
    
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    
    // Sample expense categories
    val expenseCategories = listOf(
        ExpenseCategory("Materiais", 500),
        ExpenseCategory("Aluguel", 1200),
        ExpenseCategory("Eletricidade", 300),
        ExpenseCategory("Transporte", 200)
    )
    
    // Sample transactions
    val expenses = listOf(
        TransactionItem("Compra de materiais", "500 MZN", "01/06/2025", true),
        TransactionItem("Aluguel", "1,200 MZN", "01/06/2025", true),
        TransactionItem("Eletricidade", "300 MZN", "01/06/2025", true),
        TransactionItem("Transporte", "200 MZN", "31/05/2025", true)
    )
    
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text("Relatório de Despesas") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Período do Relatório",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            periods.forEach { period ->
                                FilterChip(
                                    selected = selectedPeriod == period,
                                    onClick = { selectedPeriod = period },
                                    label = { Text(period) }
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Data Inicial",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = startDate.format(formatter),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            
                            Column {
                                Text(
                                    text = "Data Final",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = endDate.format(formatter),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Total de Despesas",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "2,200 MZN",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Categorias de Despesas",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        expenseCategories.forEach { category ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = category.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                
                                Text(
                                    text = "${category.amount} MZN",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
            
            item {
                Text(
                    text = "Detalhes das Despesas",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        TransactionList(transactions = expenses)
                    }
                }
            }
            
            item {
                Button(
                    onClick = { /* Generate PDF report */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Gerar Relatório PDF")
                }
            }
        }
    }
}

data class ExpenseCategory(val name: String, val amount: Int)
