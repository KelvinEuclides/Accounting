package com.anje.kelvin.aconting.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.anje.kelvin.aconting.data.preferences.UserPreferences
import com.anje.kelvin.aconting.ui.localization.LocalStrings
import com.anje.kelvin.aconting.ui.localization.Strings
import com.anje.kelvin.aconting.presentation.viewmodel.SettingsViewModel
import com.anje.kelvin.aconting.presentation.viewmodel.AuthViewModel

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Menu", Icons.Default.Home)
    object Account : Screen("account", "Conta", Icons.Default.AccountCircle)
    object Statistics : Screen("statistics", "Estatísticas", Icons.Default.Analytics)
    
    // Additional screens accessed from menu
    object Sales : Screen("sales", "Vendas", Icons.Default.ShoppingCart)
    object Expense : Screen("expense", "Despesas", Icons.Default.TrendingDown)
    object Deposit : Screen("deposit", "Depósitos", Icons.Default.TrendingUp)
    object Inventory : Screen("inventory", "Estoque", Icons.Default.Inventory)
    object Transactions : Screen("transactions", "Transações", Icons.Default.CompareArrows)
    object Reports : Screen("reports", "Relatórios", Icons.Default.Assessment)
    
    // Report screens
    object SalesReport : Screen("sales_report", "Relatório de Vendas", Icons.Default.ShoppingCart)
    object ExpensesReport : Screen("expenses_report", "Relatório de Despesas", Icons.Default.TrendingDown)
    object TransactionsReport : Screen("transactions_report", "Relatório de Transações", Icons.Default.CompareArrows)
    object ActivitiesReport : Screen("activities_report", "Relatório de Actividades", Icons.Default.Analytics)
    object IncomeReport : Screen("income_report", "Relatório de Receitas", Icons.Default.TrendingUp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlternativeMainScreen(@Suppress("UNUSED_PARAMETER") onLogout: () -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(Screen.Home, Screen.Account, Screen.Statistics)

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToSales = { navController.navigate(Screen.Sales.route) },
                    onNavigateToExpenses = { navController.navigate(Screen.Expense.route) },
                    onNavigateToDeposits = { navController.navigate(Screen.Deposit.route) },
                    onNavigateToInventory = { navController.navigate(Screen.Inventory.route) },
                    onNavigateToTransactions = { navController.navigate(Screen.Transactions.route) },
                    onNavigateToReports = { navController.navigate(Screen.Reports.route) }
                )
            }
            composable(Screen.Account.route) {
                AccountScreen()
            }
            composable(Screen.Statistics.route) {
                StatisticsScreen()
            }
            composable(Screen.Sales.route) {
                SalesScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Expense.route) {
                ExpenseScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Deposit.route) {
                DepositScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Inventory.route) {
                InventoryScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Transactions.route) {
                TransactionScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Reports.route) {
                ReportsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToSalesReport = { navController.navigate(Screen.SalesReport.route) },
                    onNavigateToExpensesReport = { navController.navigate(Screen.ExpensesReport.route) },
                    onNavigateToTransactionsReport = { navController.navigate(Screen.TransactionsReport.route) },
                    onNavigateToActivitiesReport = { navController.navigate(Screen.ActivitiesReport.route) },
                    onNavigateToIncomeReport = { navController.navigate(Screen.IncomeReport.route) }
                )
            }
            composable(Screen.SalesReport.route) {
                SalesReportScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.ExpensesReport.route) {
                ExpensesReportScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.TransactionsReport.route) {
                TransactionsReportScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.ActivitiesReport.route) {
                ActivitiesReportScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.IncomeReport.route) {
                IncomeReportScreen(onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
fun AccountScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Conta",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Informações da Conta",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "Nome: Usuario",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "Telefone: +258 00 000 0000",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "Saldo: 15,450.00 MZN",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun StatisticsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Estatísticas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Quick stats cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                title = "Vendas Hoje",
                value = "MT 2,450",
                icon = Icons.Default.ShoppingCart,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Lucro Mensal",
                value = "MT 15,320",
                icon = Icons.Default.TrendingUp,
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Gráficos detalhados e análises financeiras aparecerão aqui",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SettingsScreen(onLogout: () -> Unit) {
    val userPreferences: UserPreferences = hiltViewModel<SettingsViewModel>().userPreferences
    val authViewModel: AuthViewModel = hiltViewModel()
    val strings = LocalStrings.current
    
    val currency by userPreferences.currency.collectAsState()
    val language by userPreferences.language.collectAsState()
    val notifications by userPreferences.notifications.collectAsState()
    val darkMode by userPreferences.isDarkMode.collectAsState()
    val autoBackup by userPreferences.autoBackup.collectAsState()
    val lowStockAlert by userPreferences.lowStockAlert.collectAsState()
    val stockThreshold by userPreferences.stockThreshold.collectAsState()
    val dailyReports by userPreferences.dailyReports.collectAsState()
    
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = strings.settings,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Configurações Gerais
        item {
            SettingsSection(title = strings.general) {
                // Moeda
                SettingsItem(
                    title = strings.currency,
                    subtitle = getCurrencyDisplayName(currency, strings),
                    icon = Icons.Default.AttachMoney,
                    onClick = { showCurrencyDialog = true }
                )
                
                // Idioma
                SettingsItem(
                    title = strings.language,
                    subtitle = getLanguageDisplayName(language),
                    icon = Icons.Default.Language,
                    onClick = { showLanguageDialog = true }
                )
                
                // Modo Escuro
                SettingsItemWithSwitch(
                    title = strings.darkMode,
                    subtitle = strings.darkModeDescription,
                    icon = Icons.Default.DarkMode,
                    checked = darkMode,
                    onCheckedChange = { userPreferences.setDarkMode(it) }
                )
            }
        }

        // Notificações
        item {
            SettingsSection(title = strings.notifications) {
                SettingsItemWithSwitch(
                    title = strings.notifications,
                    subtitle = strings.notificationsDescription,
                    icon = Icons.Default.Notifications,
                    checked = notifications,
                    onCheckedChange = { userPreferences.setNotifications(it) }
                )
                
                SettingsItemWithSwitch(
                    title = strings.lowStockAlert,
                    subtitle = strings.lowStockAlertDescription,
                    icon = Icons.Default.Warning,
                    checked = lowStockAlert,
                    onCheckedChange = { userPreferences.setLowStockAlert(it) }
                )
                
                if (lowStockAlert) {
                    SettingsItemWithTextField(
                        title = strings.stockThreshold,
                        subtitle = strings.stockThresholdDescription,
                        icon = Icons.Default.Inventory,
                        value = stockThreshold.toString(),
                        onValueChange = { 
                            it.toIntOrNull()?.let { threshold ->
                                userPreferences.setStockThreshold(threshold)
                            }
                        }
                    )
                }
                
                SettingsItemWithSwitch(
                    title = strings.dailyReports,
                    subtitle = strings.dailyReportsDescription,
                    icon = Icons.Default.Today,
                    checked = dailyReports,
                    onCheckedChange = { userPreferences.setDailyReports(it) }
                )
            }
        }

        // Dados e Backup
        item {
            SettingsSection(title = strings.dataAndBackup) {
                SettingsItemWithSwitch(
                    title = strings.autoBackup,
                    subtitle = strings.autoBackupDescription,
                    icon = Icons.Default.CloudUpload,
                    checked = autoBackup,
                    onCheckedChange = { userPreferences.setAutoBackup(it) }
                )
                
                SettingsItem(
                    title = strings.exportData,
                    subtitle = strings.exportDataDescription,
                    icon = Icons.Default.FileDownload,
                    onClick = { /* TODO: Implementar exportação */ }
                )
                
                SettingsItem(
                    title = strings.importData,
                    subtitle = strings.importDataDescription,
                    icon = Icons.Default.FileUpload,
                    onClick = { /* TODO: Implementar importação */ }
                )
            }
        }

        // Segurança
        item {
            SettingsSection(title = strings.security) {
                SettingsItem(
                    title = strings.changePin,
                    subtitle = strings.changePinDescription,
                    icon = Icons.Default.Lock,
                    onClick = { /* TODO: Implementar alteração de PIN */ }
                )
                
                SettingsItem(
                    title = strings.clearData,
                    subtitle = strings.clearDataDescription,
                    icon = Icons.Default.DeleteForever,
                    onClick = { /* TODO: Implementar limpeza de dados */ },
                    isDestructive = true
                )
            }
        }

        // Sobre
        item {
            SettingsSection(title = strings.about) {
                SettingsItem(
                    title = strings.version,
                    subtitle = "1.0.0",
                    icon = Icons.Default.Info,
                    onClick = { }
                )
                
                SettingsItem(
                    title = strings.termsOfUse,
                    subtitle = strings.termsDescription,
                    icon = Icons.Default.Description,
                    onClick = { /* TODO: Abrir termos */ }
                )
                
                SettingsItem(
                    title = strings.privacyPolicy,
                    subtitle = strings.privacyDescription,
                    icon = Icons.Default.PrivacyTip,
                    onClick = { /* TODO: Abrir política */ }
                )
            }
        }

        // Logout
        item {
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(strings.logoutAccount)
            }
        }
    }

    // Dialogs
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text(strings.confirmLogout) },
            text = { Text(strings.confirmLogoutMessage) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        authViewModel.logout()
                        onLogout()
                    }
                ) {
                    Text(strings.logout)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text(strings.cancel)
                }
            }
        )
    }

    if (showCurrencyDialog) {
        val currencies = listOf("MT", "USD", "EUR", "ZAR")
        AlertDialog(
            onDismissRequest = { showCurrencyDialog = false },
            title = { Text(strings.currency) },
            text = {
                Column {
                    currencies.forEach { curr ->
                        TextButton(
                            onClick = {
                                userPreferences.setCurrency(curr)
                                showCurrencyDialog = false
                            }
                        ) {
                            Text(
                                getCurrencyDisplayName(curr, strings), 
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showCurrencyDialog = false }) {
                    Text(strings.cancel)
                }
            }
        )
    }

    if (showLanguageDialog) {
        val languages = listOf("pt", "en", "fr")
        val languageNames = listOf("Português", "English", "Français")
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(strings.language) },
            text = {
                Column {
                    languages.forEachIndexed { index, lang ->
                        TextButton(
                            onClick = {
                                userPreferences.setLanguage(lang)
                                showLanguageDialog = false
                            }
                        ) {
                            Text(languageNames[index], modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text(strings.cancel)
                }
            }
        )
    }
}

@Composable
private fun getCurrencyDisplayName(currency: String, strings: Strings): String {
    return when (currency) {
        "MT" -> "MT - ${strings.meticais}"
        "USD" -> "USD - ${strings.dollars}"
        "EUR" -> "EUR - ${strings.euros}"
        "ZAR" -> "ZAR - ${strings.rands}"
        else -> currency
    }
}

@Composable
private fun getLanguageDisplayName(language: String): String {
    return when (language) {
        "pt" -> "Português"
        "en" -> "English"
        "fr" -> "Français"
        else -> language
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isDestructive) MaterialTheme.colorScheme.error 
                   else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(end = 16.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (isDestructive) MaterialTheme.colorScheme.error 
                       else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SettingsItemWithSwitch(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(end = 16.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingsItemWithTextField(
    title: String,
    subtitle: String,
    icon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
    }
}

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val strings = LocalStrings.current
    val registerUiState by viewModel.registerUiState.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val isFormValid by viewModel.isRegisterFormValid.collectAsState(false)
    
    // Navigate when registration is successful
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onRegisterSuccess()
        }
    }
    
    // Clear error when screen is opened
    LaunchedEffect(Unit) {
        viewModel.clearRegisterMessages()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            // Header
            Text(
                text = strings.register,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
        
        item {
            // Name Field
            OutlinedTextField(
                value = registerUiState.name,
                onValueChange = viewModel::updateRegisterName,
                label = { Text(strings.name) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                isError = registerUiState.nameError.isNotEmpty(),
                supportingText = if (registerUiState.nameError.isNotEmpty()) {
                    { Text(registerUiState.nameError, color = MaterialTheme.colorScheme.error) }
                } else null
            )
        }
        
        item {
            // Phone Number Field
            OutlinedTextField(
                value = registerUiState.phone,
                onValueChange = viewModel::updateRegisterPhone,
                label = { Text(strings.phoneNumber) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                isError = registerUiState.phoneError.isNotEmpty(),
                supportingText = if (registerUiState.phoneError.isNotEmpty()) {
                    { Text(registerUiState.phoneError, color = MaterialTheme.colorScheme.error) }
                } else null
            )
        }
        
        item {
            // Email Field (Optional)
            OutlinedTextField(
                value = registerUiState.email,
                onValueChange = viewModel::updateRegisterEmail,
                label = { Text("${strings.email} (${strings.optional})") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
        }
        
        item {
            // PIN Field
            OutlinedTextField(
                value = registerUiState.pin,
                onValueChange = viewModel::updateRegisterPin,
                label = { Text(strings.pin) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = registerUiState.pinError.isNotEmpty(),
                supportingText = if (registerUiState.pinError.isNotEmpty()) {
                    { Text(registerUiState.pinError, color = MaterialTheme.colorScheme.error) }
                } else null
            )
        }
        
        item {
            // Confirm PIN Field
            OutlinedTextField(
                value = registerUiState.confirmPin,
                onValueChange = viewModel::updateRegisterConfirmPin,
                label = { Text(strings.confirmPin) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = registerUiState.confirmPinError.isNotEmpty(),
                supportingText = if (registerUiState.confirmPinError.isNotEmpty()) {
                    { Text(registerUiState.confirmPinError, color = MaterialTheme.colorScheme.error) }
                } else null
            )
        }

        item {
            // Error message
            if (registerUiState.errorMessage.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = registerUiState.errorMessage,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        item {
            // Register Button
            Button(
                onClick = { viewModel.register() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !registerUiState.isLoading && isFormValid
            ) {
                if (registerUiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = strings.register,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Login Link
            TextButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = strings.alreadyHaveAccount,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}