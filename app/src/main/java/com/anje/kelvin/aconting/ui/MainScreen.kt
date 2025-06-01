package com.anje.kelvin.aconting.ui

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.anje.kelvin.aconting.Operacoes.Adicionar_deposito_Activity
import com.anje.kelvin.aconting.Operacoes.Adicionar_despesaActivity
import com.anje.kelvin.aconting.Operacoes.Gerir_estoque
import com.anje.kelvin.aconting.Operacoes.RelatorioActivity
import com.anje.kelvin.aconting.Operacoes.TransicoesActivity
import com.anje.kelvin.aconting.Operacoes.Venda_Activity
import com.anje.kelvin.aconting.ui.components.TransactionItem
import com.anje.kelvin.aconting.ui.navigation.BottomNavItem
import com.anje.kelvin.aconting.ui.navigation.PakitiniBottomNavigation
import com.anje.kelvin.aconting.ui.screens.AccountScreen
import com.anje.kelvin.aconting.ui.screens.HomeScreen
import com.anje.kelvin.aconting.ui.screens.SettingsScreen
import com.anje.kelvin.aconting.ui.theme.PakitiniTheme
import com.anje.kelvin.aconting.login

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // In a real app this would be hooked up to your repository/ViewModel
    val saldoConta = "10,000 MZN"
    val nomeConta = "Minha Conta"
    val despesas = "2,500 MZN"
    val depositos = "12,500 MZN"
    val context = LocalContext.current

    var currentScreen by remember { mutableStateOf(BottomNavItem.Home.name) }

    // Sample data for transactions
    val despesaItems = listOf(
        TransactionItem("Compra de materiais", "500 MZN", "01/06/2025", true),
        TransactionItem("Aluguel", "1,200 MZN", "01/06/2025", true),
        TransactionItem("Eletricidade", "300 MZN", "01/06/2025", true)
    )

    val depositoItems = listOf(
        TransactionItem("Venda de produtos", "2,000 MZN", "01/06/2025", false),
        TransactionItem("Reembolso", "500 MZN", "01/06/2025", false)
    )

    PakitiniTheme {
        Scaffold(
            bottomBar = {
                PakitiniBottomNavigation(
                    currentRoute = currentScreen,
                    onNavigate = { route ->
                        currentScreen = route
                    }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)) {
                when (currentScreen) {
                    BottomNavItem.Home.name -> {
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
                                context.startActivity(Intent(context, TransicoesActivity::class.java))
                            },
                            onNavigateToVendas = {
                                context.startActivity(Intent(context, Venda_Activity::class.java))
                            },
                            onNavigateToRelatorios = {
                                context.startActivity(Intent(context, RelatorioActivity::class.java))
                            },
                            onNavigateToEstoque = {
                                context.startActivity(Intent(context, Gerir_estoque::class.java))
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    BottomNavItem.Account.name -> {
                        AccountScreen(
                            saldoConta = saldoConta,
                            despesasMensais = "4,500 MZN",
                            vendasMensais = "15,000 MZN",
                            numVendasMensais = "23",
                            despesaList = despesaItems,
                            depositoList = depositoItems,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    BottomNavItem.Settings.name -> {
                        SettingsScreen(
                            onLogoutClick = {
                                // Log out user and navigate to login screen
                                context.startActivity(Intent(context, login::class.java))
                                // In a real app you'd need to handle clearing the logged-in state
                            },
                            onChangePinClick = {
                                // Open a dialog to change PIN
                                // This would be implemented with a Composable dialog
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
