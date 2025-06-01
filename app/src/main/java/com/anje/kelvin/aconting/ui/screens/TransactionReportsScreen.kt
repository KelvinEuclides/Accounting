package com.anje.kelvin.aconting.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionReportsScreen(
    onBackClick: () -> Unit
) {
    var selectedPeriod by remember { mutableStateOf("Diário") }
    val periods = listOf("Diário", "Semanal", "Mensal", "Anual")
    
    var startDate by remember { mutableStateOf(LocalDate.now().minusDays(7)) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }
    
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text("Relatório de Transações") },
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
                            text = "Resumo de Transações",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Total de Despesas",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "2,200 MZN",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            
                            Column {
                                Text(
                                    text = "Total de Receitas",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "4,000 MZN",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Column {
                            Text(
                                text = "Saldo",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "1,800 MZN",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
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
