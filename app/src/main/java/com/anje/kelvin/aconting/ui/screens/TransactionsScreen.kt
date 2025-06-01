package com.anje.kelvin.aconting.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anje.kelvin.aconting.ui.components.TransactionList
import com.anje.kelvin.aconting.ui.viewmodels.TransactionsViewModel
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    onBackClick: () -> Unit,
    onAddTransactionClick: () -> Unit,
    viewModel: TransactionsViewModel = viewModel()
) {
    val tabItems = listOf("Todas", "Despesas", "Depósitos", "Vendas")
    var selectedTabIndex by remember { mutableStateOf(0) }
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text("Transações") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onAddTransactionClick) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Add,
                            contentDescription = "Adicionar Transação"
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
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex
            ) {
                tabItems.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                when (selectedTabIndex) {
                    0 -> AllTransactionsTab(viewModel.allTransactions)
                    1 -> ExpensesTab(viewModel.expenses)
                    2 -> DepositsTab(viewModel.deposits)
                    3 -> SalesTab(viewModel.sales)
                }
            }
        }
    }
}

@Composable
fun AllTransactionsTab(transactionsFlow: StateFlow<List<TransactionItem>>) {
    val transactions by transactionsFlow.collectAsState()
    TransactionList(transactions = transactions)
}

@Composable
fun ExpensesTab(expensesFlow: StateFlow<List<TransactionItem>>) {
    val expenses by expensesFlow.collectAsState()
    TransactionList(
        transactions = expenses,
        emptyMessage = "Não há despesas registradas"
    )
}

@Composable
fun DepositsTab(depositsFlow: StateFlow<List<TransactionItem>>) {
    val deposits by depositsFlow.collectAsState()
    TransactionList(
        transactions = deposits,
        emptyMessage = "Não há depósitos registrados"
    )
}

@Composable
fun SalesTab(salesFlow: StateFlow<List<TransactionItem>>) {
    val sales by salesFlow.collectAsState()
    TransactionList(
        transactions = sales,
        emptyMessage = "Não há vendas registradas"
    )
}
