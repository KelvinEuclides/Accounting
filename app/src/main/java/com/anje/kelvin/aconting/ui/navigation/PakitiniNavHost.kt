package com.anje.kelvin.aconting.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import com.anje.kelvin.aconting.Operacoes.*
import com.anje.kelvin.aconting.ui.screens.*
import com.anje.kelvin.aconting.ui.components.TransactionList
import com.anje.kelvin.aconting.login

@Composable
fun PakitiniNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // Sample data - in a real app, this would come from view models
    val saldoConta = "10,000 MZN"
    val nomeConta = "Minha Conta"
    val despesas = "2,500 MZN"
    val depositos = "12,500 MZN"
    
    // Sample transaction data
    val despesaItems = listOf(
        TransactionItem("Compra de materiais", "500 MZN", "01/06/2025", true),
        TransactionItem("Aluguel", "1,200 MZN", "01/06/2025", true),
        TransactionItem("Eletricidade", "300 MZN", "01/06/2025", true)
    )
    
    val depositoItems = listOf(
        TransactionItem("Venda de produtos", "2,000 MZN", "01/06/2025", false),
        TransactionItem("Reembolso", "500 MZN", "01/06/2025", false)
    )
    
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.name,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.name) {
            HomeScreen(
                saldoConta = saldoConta,
                nomeConta = nomeConta,
                despesas = despesas,
                depositos = depositos,
                onNavigateToDepositos = {
                    context.startActivity(Intent(context, Adicionar_deposito_Activity::class.java))
                },
                onNavigateToDespesas = {
                    context.startActivity(Intent(context, Adicionar_despesaActivity::class.java))
                },
                onNavigateToTransacoes = {
                    navController.navigate(Screen.TransactionsScreen.route)
                },
                onNavigateToVendas = {
                    context.startActivity(Intent(context, Venda_Activity::class.java))
                },
                onNavigateToRelatorios = {
                    navController.navigate(Screen.ReportsScreen.route)
                },
                onNavigateToEstoque = {
                    context.startActivity(Intent(context, Gerir_estoque::class.java))
                }
            )
        }
        
        composable(BottomNavItem.Account.name) {
            AccountScreen(
                saldoConta = saldoConta,
                despesasMensais = "4,500 MZN",
                vendasMensais = "15,000 MZN",
                numVendasMensais = "23",
                despesaList = despesaItems,
                depositoList = depositoItems
            )
        }
        
        composable(BottomNavItem.Settings.name) {
            SettingsScreen(
                onLogoutClick = {
                    // Log out user and navigate to login screen
                    context.startActivity(Intent(context, login::class.java))
                    // In a real app you'd need to handle clearing the logged-in state
                },
                onChangePinClick = {
                    // In a real implementation, this would show a dialog to change PIN
                    navController.navigate(Screen.ChangePinScreen.route)
                }
            )
        }
        
        // Additional screens that aren't part of bottom navigation
        composable(Screen.ChangePinScreen.route) {
            ChangePinScreen(
                onBackClick = { navController.popBackStack() },
                onPinChanged = { currentPin, newPin ->
                    // In a real app, you would update the PIN in database here
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.TransactionsScreen.route) {
            TransactionsScreen(
                onBackClick = { navController.popBackStack() },
                onAddTransactionClick = {
                    // In a real app, this would navigate to a screen to add transactions
                    // or show a dialog to select transaction type
                    // For now, we're just going back to maintain the sample flow
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.ReportsScreen.route) {
            ReportsScreen(
                onBackClick = { navController.popBackStack() },
                onTransactionReportsClick = { navController.navigate(Screen.TransactionReportsScreen.route) },
                onExpenseReportsClick = { navController.navigate(Screen.ExpenseReportsScreen.route) },
                onIncomeReportsClick = { navController.navigate(Screen.IncomeReportsScreen.route) }
            )
        }
        
        composable(Screen.TransactionReportsScreen.route) {
            TransactionReportsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.ExpenseReportsScreen.route) {
            ExpenseReportsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.IncomeReportsScreen.route) {
            IncomeReportsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
