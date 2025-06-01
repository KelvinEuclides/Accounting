package com.anje.kelvin.aconting.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anje.kelvin.aconting.R
import com.anje.kelvin.aconting.ui.components.DashboardCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    saldoConta: String,
    nomeConta: String,
    despesas: String,
    depositos: String,
    onNavigateToDepositos: () -> Unit,
    onNavigateToDespesas: () -> Unit,
    onNavigateToTransacoes: () -> Unit,
    onNavigateToVendas: () -> Unit,
    onNavigateToRelatorios: () -> Unit,
    onNavigateToEstoque: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Header with user info
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
                    text = nomeConta,
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Saldo da Conta",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Text(
                    text = saldoConta,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Despesas", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = despesas,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Depósitos", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = depositos,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Main menu options in a grid layout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DashboardCard(
                title = "Depósitos",
                icon = R.drawable.depositos_dinheiro,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToDepositos
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            DashboardCard(
                title = "Despesas",
                icon = R.drawable.despesa,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToDespesas
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DashboardCard(
                title = "Transações",
                icon = R.drawable.transacoes,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToTransacoes
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            DashboardCard(
                title = "Vendas",
                icon = R.drawable.venda,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToVendas
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DashboardCard(
                title = "Relatórios",
                icon = R.drawable.relatorios,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToRelatorios
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            DashboardCard(
                title = "Estoque",
                icon = R.drawable.estoque,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToEstoque
            )
        }
    }
}
