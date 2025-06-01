package com.anje.kelvin.aconting.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anje.kelvin.aconting.R
import com.anje.kelvin.aconting.ui.components.ReportCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onBackClick: () -> Unit,
    onTransactionReportsClick: () -> Unit,
    onExpenseReportsClick: () -> Unit,
    onIncomeReportsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text("Relatórios") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Escolha um tipo de relatório para visualizar",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            ReportCard(
                title = "Relatório de Transações",
                description = "Visualize todas as transações realizadas em períodos específicos",
                icon = R.drawable.transacoes,
                onClick = onTransactionReportsClick
            )
            
            ReportCard(
                title = "Relatório de Despesas",
                description = "Analise suas despesas diárias, mensais e anuais",
                icon = R.drawable.despesa,
                onClick = onExpenseReportsClick
            )
            
            ReportCard(
                title = "Relatório de Rendas",
                description = "Veja detalhes sobre suas receitas e vendas",
                icon = R.drawable.depositos_dinheiro,
                onClick = onIncomeReportsClick
            )
        }
    }
}
