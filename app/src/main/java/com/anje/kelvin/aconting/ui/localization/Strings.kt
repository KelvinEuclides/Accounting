package com.anje.kelvin.aconting.ui.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember

val LocalStrings = compositionLocalOf<Strings> { 
    error("No strings provided") 
}

@Composable
fun ProvideStrings(
    language: String,
    content: @Composable () -> Unit
) {
    val strings = remember(language) {
        when (language) {
            "en" -> EnglishStrings()
            "fr" -> FrenchStrings()
            else -> PortugueseStrings()
        }
    }
    
    CompositionLocalProvider(LocalStrings provides strings) {
        content()
    }
}

interface Strings {
    // Navigation
    val home: String
    val sales: String
    val expenses: String
    val deposits: String
    val inventory: String
    val transactions: String
    val reports: String
    val settings: String
    
    // Login
    val login: String
    val register: String
    val phoneNumber: String
    val pin: String
    val enterPhone: String
    val enterPin: String
    val invalidCredentials: String
    val fillAllFields: String
    val loginError: String
    val loginSuccess: String
    val logout: String
    val confirmLogout: String
    val confirmLogoutMessage: String
    val noAccountRegister: String
    val currentBalance: String
    val email: String
    val optional: String
    val confirmPin: String
    val alreadyHaveAccount: String
    
    // Settings
    val general: String
    val currency: String
    val language: String
    val darkMode: String
    val darkModeDescription: String
    val notifications: String
    val notificationsDescription: String
    val lowStockAlert: String
    val lowStockAlertDescription: String
    val stockThreshold: String
    val stockThresholdDescription: String
    val dailyReports: String
    val dailyReportsDescription: String
    val dataAndBackup: String
    val autoBackup: String
    val autoBackupDescription: String
    val exportData: String
    val exportDataDescription: String
    val importData: String
    val importDataDescription: String
    val security: String
    val changePin: String
    val changePinDescription: String
    val clearData: String
    val clearDataDescription: String
    val about: String
    val version: String
    val termsOfUse: String
    val termsDescription: String
    val privacyPolicy: String
    val privacyDescription: String
    val logoutAccount: String
    
    // Common
    val cancel: String
    val confirm: String
    val save: String
    val delete: String
    val edit: String
    val add: String
    val search: String
    val filter: String
    val total: String
    val date: String
    val amount: String
    val description: String
    val category: String
    val quantity: String
    val price: String
    val name: String
    val yes: String
    val no: String
    val ok: String
    val error: String
    val success: String
    val loading: String
    
    // Financial
    val balance: String
    val income: String
    val expense: String
    val profit: String
    val loss: String
    val revenue: String
    val cost: String
    val tax: String
    val discount: String
    val subtotal: String
    val grandTotal: String
    
    // Currency names
    val meticais: String
    val dollars: String
    val euros: String
    val rands: String
    
    // Messages
    val noDataAvailable: String
    val itemAddedSuccess: String
    val itemUpdatedSuccess: String
    val itemDeletedSuccess: String
    val operationFailed: String
    
    // Data Clearing
    val confirmClearData: String
    val confirmClearDataMessage: String
    val clearingData: String
    val dataClearedSuccess: String
    val dataClearingFailed: String
    val selectDataToClear: String
    val clearAllData: String
    val clearTransactions: String
    val clearProducts: String
    val clearSales: String
    val clearExpenses: String
    val clearDeposits: String
    val thisActionCannotBeUndone: String
    val typeConfirmToDelete: String
    val confirmText: String
}

class PortugueseStrings : Strings {
    override val home = "Início"
    override val sales = "Vendas"
    override val expenses = "Despesas"
    override val deposits = "Depósitos"
    override val inventory = "Estoque"
    override val transactions = "Transações"
    override val reports = "Relatórios"
    override val settings = "Configurações"
    
    override val login = "Entrar"
    override val register = "Registrar"
    override val phoneNumber = "Telefone"
    override val pin = "PIN"
    override val enterPhone = "Digite o telefone"
    override val enterPin = "Digite o PIN"
    override val invalidCredentials = "Credenciais inválidas"
    override val fillAllFields = "Preencha todos os campos"
    override val loginError = "Erro no login"
    override val loginSuccess = "Login realizado com sucesso"
    override val logout = "Sair"
    override val confirmLogout = "Confirmar Saída"
    override val confirmLogoutMessage = "Tem certeza que deseja sair da sua conta?"
    override val noAccountRegister = "Não tem conta? Registre-se"
    override val currentBalance = "Saldo Atual"
    override val email = "E-mail"
    override val optional = "opcional"
    override val confirmPin = "Confirmar PIN"
    override val alreadyHaveAccount = "Já tem conta? Entre"
    
    override val general = "Geral"
    override val currency = "Moeda"
    override val language = "Idioma"
    override val darkMode = "Modo Escuro"
    override val darkModeDescription = "Tema escuro para a aplicação"
    override val notifications = "Notificações"
    override val notificationsDescription = "Receber notificações do app"
    override val lowStockAlert = "Alerta de Estoque Baixo"
    override val lowStockAlertDescription = "Notificar quando estoque estiver baixo"
    override val stockThreshold = "Limite de Estoque"
    override val stockThresholdDescription = "Quantidade mínima para alerta"
    override val dailyReports = "Relatórios Diários"
    override val dailyReportsDescription = "Receber resumo diário por notificação"
    override val dataAndBackup = "Dados e Backup"
    override val autoBackup = "Backup Automático"
    override val autoBackupDescription = "Backup automático dos dados"
    override val exportData = "Exportar Dados"
    override val exportDataDescription = "Exportar dados para arquivo CSV"
    override val importData = "Importar Dados"
    override val importDataDescription = "Importar dados de arquivo CSV"
    override val security = "Segurança"
    override val changePin = "Alterar PIN"
    override val changePinDescription = "Alterar PIN de acesso"
    override val clearData = "Limpar Dados"
    override val clearDataDescription = "Remover todos os dados da aplicação"
    override val about = "Sobre"
    override val version = "Versão"
    override val termsOfUse = "Termos de Uso"
    override val termsDescription = "Ler termos e condições"
    override val privacyPolicy = "Política de Privacidade"
    override val privacyDescription = "Ler política de privacidade"
    override val logoutAccount = "Sair da Conta"
    
    override val cancel = "Cancelar"
    override val confirm = "Confirmar"
    override val save = "Salvar"
    override val delete = "Excluir"
    override val edit = "Editar"
    override val add = "Adicionar"
    override val search = "Pesquisar"
    override val filter = "Filtrar"
    override val total = "Total"
    override val date = "Data"
    override val amount = "Valor"
    override val description = "Descrição"
    override val category = "Categoria"
    override val quantity = "Quantidade"
    override val price = "Preço"
    override val name = "Nome"
    override val yes = "Sim"
    override val no = "Não"
    override val ok = "OK"
    override val error = "Erro"
    override val success = "Sucesso"
    override val loading = "Carregando..."
    
    override val balance = "Saldo"
    override val income = "Receita"
    override val expense = "Despesa"
    override val profit = "Lucro"
    override val loss = "Prejuízo"
    override val revenue = "Receita"
    override val cost = "Custo"
    override val tax = "Imposto"
    override val discount = "Desconto"
    override val subtotal = "Subtotal"
    override val grandTotal = "Total Geral"
    
    override val meticais = "Meticais"
    override val dollars = "Dólares"
    override val euros = "Euros"
    override val rands = "Rands"
    
    override val noDataAvailable = "Nenhum dado disponível"
    override val itemAddedSuccess = "Item adicionado com sucesso"
    override val itemUpdatedSuccess = "Item atualizado com sucesso"
    override val itemDeletedSuccess = "Item excluído com sucesso"
    override val operationFailed = "Operação falhou"
    
    override val confirmClearData = "Confirmar Limpeza de Dados"
    override val confirmClearDataMessage = "Esta ação irá apagar permanentemente os dados selecionados. Esta operação NÃO pode ser desfeita!"
    override val clearingData = "Limpando dados..."
    override val dataClearedSuccess = "Dados limpos com sucesso"
    override val dataClearingFailed = "Falha ao limpar dados"
    override val selectDataToClear = "Selecione os dados para limpar"
    override val clearAllData = "Limpar Todos os Dados"
    override val clearTransactions = "Limpar Transações"
    override val clearProducts = "Limpar Produtos"
    override val clearSales = "Limpar Vendas"
    override val clearExpenses = "Limpar Despesas"
    override val clearDeposits = "Limpar Depósitos"
    override val thisActionCannotBeUndone = "Esta ação não pode ser desfeita!"
    override val typeConfirmToDelete = "Digite 'CONFIRMAR' para continuar"
    override val confirmText = "CONFIRMAR"
}

class EnglishStrings : Strings {
    override val home = "Home"
    override val sales = "Sales"
    override val expenses = "Expenses"
    override val deposits = "Deposits"
    override val inventory = "Inventory"
    override val transactions = "Transactions"
    override val reports = "Reports"
    override val settings = "Settings"
    
    override val login = "Login"
    override val register = "Register"
    override val phoneNumber = "Phone Number"
    override val pin = "PIN"
    override val enterPhone = "Enter phone number"
    override val enterPin = "Enter PIN"
    override val invalidCredentials = "Invalid credentials"
    override val fillAllFields = "Please fill all fields"
    override val loginError = "Login error"
    override val loginSuccess = "Login successful"
    override val logout = "Logout"
    override val confirmLogout = "Confirm Logout"
    override val confirmLogoutMessage = "Are you sure you want to logout?"
    override val noAccountRegister = "Don't have an account? Register"
    override val currentBalance = "Current Balance"
    override val email = "Email"
    override val optional = "optional"
    override val confirmPin = "Confirm PIN"
    override val alreadyHaveAccount = "Already have an account? Login"
    
    override val general = "General"
    override val currency = "Currency"
    override val language = "Language"
    override val darkMode = "Dark Mode"
    override val darkModeDescription = "Dark theme for the application"
    override val notifications = "Notifications"
    override val notificationsDescription = "Receive app notifications"
    override val lowStockAlert = "Low Stock Alert"
    override val lowStockAlertDescription = "Notify when stock is low"
    override val stockThreshold = "Stock Threshold"
    override val stockThresholdDescription = "Minimum quantity for alert"
    override val dailyReports = "Daily Reports"
    override val dailyReportsDescription = "Receive daily summary notifications"
    override val dataAndBackup = "Data & Backup"
    override val autoBackup = "Auto Backup"
    override val autoBackupDescription = "Automatic data backup"
    override val exportData = "Export Data"
    override val exportDataDescription = "Export data to CSV file"
    override val importData = "Import Data"
    override val importDataDescription = "Import data from CSV file"
    override val security = "Security"
    override val changePin = "Change PIN"
    override val changePinDescription = "Change access PIN"
    override val clearData = "Clear Data"
    override val clearDataDescription = "Remove all application data"
    override val about = "About"
    override val version = "Version"
    override val termsOfUse = "Terms of Use"
    override val termsDescription = "Read terms and conditions"
    override val privacyPolicy = "Privacy Policy"
    override val privacyDescription = "Read privacy policy"
    override val logoutAccount = "Logout Account"
    
    override val cancel = "Cancel"
    override val confirm = "Confirm"
    override val save = "Save"
    override val delete = "Delete"
    override val edit = "Edit"
    override val add = "Add"
    override val search = "Search"
    override val filter = "Filter"
    override val total = "Total"
    override val date = "Date"
    override val amount = "Amount"
    override val description = "Description"
    override val category = "Category"
    override val quantity = "Quantity"
    override val price = "Price"
    override val name = "Name"
    override val yes = "Yes"
    override val no = "No"
    override val ok = "OK"
    override val error = "Error"
    override val success = "Success"
    override val loading = "Loading..."
    
    override val balance = "Balance"
    override val income = "Income"
    override val expense = "Expense"
    override val profit = "Profit"
    override val loss = "Loss"
    override val revenue = "Revenue"
    override val cost = "Cost"
    override val tax = "Tax"
    override val discount = "Discount"
    override val subtotal = "Subtotal"
    override val grandTotal = "Grand Total"
    
    override val meticais = "Meticais"
    override val dollars = "Dollars"
    override val euros = "Euros"
    override val rands = "Rands"
    
    override val noDataAvailable = "No data available"
    override val itemAddedSuccess = "Item added successfully"
    override val itemUpdatedSuccess = "Item updated successfully"
    override val itemDeletedSuccess = "Item deleted successfully"
    override val operationFailed = "Operation failed"
    
    override val confirmClearData = "Confirm Clear Data"
    override val confirmClearDataMessage = "This action will permanently delete the selected data. This operation CANNOT be undone!"
    override val clearingData = "Clearing data..."
    override val dataClearedSuccess = "Data cleared successfully"
    override val dataClearingFailed = "Failed to clear data"
    override val selectDataToClear = "Select data to clear"
    override val clearAllData = "Clear All Data"
    override val clearTransactions = "Clear Transactions"
    override val clearProducts = "Clear Products"
    override val clearSales = "Clear Sales"
    override val clearExpenses = "Clear Expenses"
    override val clearDeposits = "Clear Deposits"
    override val thisActionCannotBeUndone = "This action cannot be undone!"
    override val typeConfirmToDelete = "Type 'CONFIRM' to continue"
    override val confirmText = "CONFIRM"
}

class FrenchStrings : Strings {
    override val home = "Accueil"
    override val sales = "Ventes"
    override val expenses = "Dépenses"
    override val deposits = "Dépôts"
    override val inventory = "Inventaire"
    override val transactions = "Transactions"
    override val reports = "Rapports"
    override val settings = "Paramètres"
    
    override val login = "Connexion"
    override val register = "S'inscrire"
    override val phoneNumber = "Téléphone"
    override val pin = "PIN"
    override val enterPhone = "Entrez le téléphone"
    override val enterPin = "Entrez le PIN"
    override val invalidCredentials = "Identifiants invalides"
    override val fillAllFields = "Veuillez remplir tous les champs"
    override val loginError = "Erreur de connexion"
    override val loginSuccess = "Connexion réussie"
    override val logout = "Déconnexion"
    override val confirmLogout = "Confirmer la déconnexion"
    override val confirmLogoutMessage = "Êtes-vous sûr de vouloir vous déconnecter?"
    override val noAccountRegister = "Pas de compte? S'inscrire"
    override val currentBalance = "Solde Actuel"
    override val email = "E-mail"
    override val optional = "optionnel"
    override val confirmPin = "Confirmer PIN"
    override val alreadyHaveAccount = "Vous avez déjà un compte? Connexion"
    
    override val general = "Général"
    override val currency = "Devise"
    override val language = "Langue"
    override val darkMode = "Mode Sombre"
    override val darkModeDescription = "Thème sombre pour l'application"
    override val notifications = "Notifications"
    override val notificationsDescription = "Recevoir les notifications de l'app"
    override val lowStockAlert = "Alerte Stock Bas"
    override val lowStockAlertDescription = "Notifier quand le stock est bas"
    override val stockThreshold = "Seuil de Stock"
    override val stockThresholdDescription = "Quantité minimale pour l'alerte"
    override val dailyReports = "Rapports Quotidiens"
    override val dailyReportsDescription = "Recevoir un résumé quotidien par notification"
    override val dataAndBackup = "Données et Sauvegarde"
    override val autoBackup = "Sauvegarde Automatique"
    override val autoBackupDescription = "Sauvegarde automatique des données"
    override val exportData = "Exporter les Données"
    override val exportDataDescription = "Exporter les données vers un fichier CSV"
    override val importData = "Importer les Données"
    override val importDataDescription = "Importer les données depuis un fichier CSV"
    override val security = "Sécurité"
    override val changePin = "Changer le PIN"
    override val changePinDescription = "Changer le PIN d'accès"
    override val clearData = "Effacer les Données"
    override val clearDataDescription = "Supprimer toutes les données de l'application"
    override val about = "À Propos"
    override val version = "Version"
    override val termsOfUse = "Conditions d'Utilisation"
    override val termsDescription = "Lire les termes et conditions"
    override val privacyPolicy = "Politique de Confidentialité"
    override val privacyDescription = "Lire la politique de confidentialité"
    override val logoutAccount = "Déconnecter le Compte"
    
    override val cancel = "Annuler"
    override val confirm = "Confirmer"
    override val save = "Enregistrer"
    override val delete = "Supprimer"
    override val edit = "Modifier"
    override val add = "Ajouter"
    override val search = "Rechercher"
    override val filter = "Filtrer"
    override val total = "Total"
    override val date = "Date"
    override val amount = "Montant"
    override val description = "Description"
    override val category = "Catégorie"
    override val quantity = "Quantité"
    override val price = "Prix"
    override val name = "Nom"
    override val yes = "Oui"
    override val no = "Non"
    override val ok = "OK"
    override val error = "Erreur"
    override val success = "Succès"
    override val loading = "Chargement..."
    
    override val balance = "Solde"
    override val income = "Revenu"
    override val expense = "Dépense"
    override val profit = "Profit"
    override val loss = "Perte"
    override val revenue = "Chiffre d'affaires"
    override val cost = "Coût"
    override val tax = "Taxe"
    override val discount = "Remise"
    override val subtotal = "Sous-total"
    override val grandTotal = "Total Général"
    
    override val meticais = "Meticais"
    override val dollars = "Dollars"
    override val euros = "Euros"
    override val rands = "Rands"
    
    override val noDataAvailable = "Aucune donnée disponible"
    override val itemAddedSuccess = "Article ajouté avec succès"
    override val itemUpdatedSuccess = "Article mis à jour avec succès"
    override val itemDeletedSuccess = "Article supprimé avec succès"
    override val operationFailed = "Opération échouée"
    
    override val confirmClearData = "Confirmer l'effacement des données"
    override val confirmClearDataMessage = "Cette action supprimera définitivement les données sélectionnées. Cette opération NE PEUT PAS être annulée!"
    override val clearingData = "Effacement des données..."
    override val dataClearedSuccess = "Données effacées avec succès"
    override val dataClearingFailed = "Échec de l'effacement des données"
    override val selectDataToClear = "Sélectionner les données à effacer"
    override val clearAllData = "Effacer toutes les données"
    override val clearTransactions = "Effacer les transactions"
    override val clearProducts = "Effacer les produits"
    override val clearSales = "Effacer les ventes"
    override val clearExpenses = "Effacer les dépenses"
    override val clearDeposits = "Effacer les dépôts"
    override val thisActionCannotBeUndone = "Cette action ne peut pas être annulée!"
    override val typeConfirmToDelete = "Tapez 'CONFIRMER' pour continuer"
    override val confirmText = "CONFIRMER"
}