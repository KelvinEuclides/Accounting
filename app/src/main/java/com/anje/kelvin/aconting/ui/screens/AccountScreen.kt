package com.anje.kelvin.aconting.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anje.kelvin.aconting.ui.components.AccountTransactionItem
import com.anje.kelvin.aconting.ui.components.TransactionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    saldoConta: String,
    despesasMensais: String,
    vendasMensais: String,
    numVendasMensais: String,
    despesaList: List<TransactionItem>,
    depositoList: List<TransactionItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Account Summary Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Saldo da Conta",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Text(
                    text = saldoConta,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Divider()
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Despesas Mensais", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            despesasMensais,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Vendas Mensais", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            vendasMensais,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
                
                Divider()
                
                Text(
                    text = "Número de Vendas Mensais: $numVendasMensais",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        // Transactions
        TabRow(
            modifier = Modifier.padding(top = 16.dp),
            contentColor = MaterialTheme.colorScheme.primary,
            selectedTabIndex = 0
        ) {
            Tab(selected = true, onClick = { /* Show despesas */ }) {
                Text("DESPESAS", modifier = Modifier.padding(vertical = 12.dp))
            }
            Tab(selected = false, onClick = { /* Show depositos */ }) {
                Text("DEPÓSITOS", modifier = Modifier.padding(vertical = 12.dp))
            }
        }
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            items(despesaList) { transaction ->
                AccountTransactionItem(transaction = transaction)
            }
        }
    }
}
