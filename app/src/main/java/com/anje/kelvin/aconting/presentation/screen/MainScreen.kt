package com.anje.kelvin.aconting.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        MainNavHost(
            navController = navController,
            modifier = Modifier.padding(paddingValues),
            onLogout = onLogout
        )
    }
}

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToSales = { navController.navigate("vendas") },
                onNavigateToExpenses = { navController.navigate("despesas") },
                onNavigateToDeposits = { navController.navigate("depositos") },
                onNavigateToInventory = { navController.navigate("estoque") },
                onNavigateToTransactions = { navController.navigate("transacoes") },
                onNavigateToReports = { navController.navigate("relatorios") }
            )
        }
        
        composable("vendas") {
            SalesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("despesas") {
            ExpenseScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("depositos") {
            DepositScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("estoque") {
            InventoryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("transacoes") {
            TransactionScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("relatorios") {
            ReportsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSalesReport = { navController.navigate("relatorio_vendas") },
                onNavigateToExpensesReport = { navController.navigate("relatorio_despesas") },
                onNavigateToTransactionsReport = { navController.navigate("relatorio_transacoes") },
                onNavigateToActivitiesReport = { navController.navigate("relatorio_actividades") },
                onNavigateToIncomeReport = { navController.navigate("relatorio_receitas") }
            )
        }
        
        composable("relatorio_vendas") {
            SalesReportScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("relatorio_despesas") {
            ExpensesReportScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("relatorio_transacoes") {
            TransactionsReportScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("relatorio_actividades") {
            ActivitiesReportScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("relatorio_receitas") {
            IncomeReportScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("settings") {
            SettingsScreen(onLogout = onLogout)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("home", "Início", Icons.Default.Home),
        BottomNavItem("transacoes", "Transações", Icons.Default.SwapHoriz),
        BottomNavItem("estoque", "Estoque", Icons.Default.Inventory),
        BottomNavItem("relatorios", "Relatórios", Icons.Default.Assessment),
        BottomNavItem("settings", "Configurações", Icons.Default.Settings)
    )
    
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)