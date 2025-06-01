package com.anje.kelvin.aconting.ui.navigation

// Define additional screens that are not part of bottom navigation
sealed class Screen(val route: String) {
    object ChangePinScreen : Screen("change_pin")
    object RegisterScreen : Screen("register")
    object TransactionsScreen : Screen("transactions")
    object ReportsScreen : Screen("reports")
    object StockScreen : Screen("stock")
    object TransactionReportsScreen : Screen("transaction_reports")
    object ExpenseReportsScreen : Screen("expense_reports")
    object IncomeReportsScreen : Screen("income_reports")
}
