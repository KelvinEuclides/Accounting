# Data Clearing Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        USER INTERFACE                            │
│                        (OtherScreens.kt)                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  Settings Screen                                                 │
│    └─> Clear Data Button (onClick: showClearDataDialog = true)  │
│                                                                   │
│  DataClearingDialog (Two-Step Process)                          │
│    ┌─────────────────────────────────────────────────┐          │
│    │ Step 1: Selection Dialog                        │          │
│    │  ☑ Clear All Data                               │          │
│    │  ─────────────────────                          │          │
│    │  ☐ Clear Sales                                  │          │
│    │  ☐ Clear Expenses                               │          │
│    │  ☐ Clear Deposits                               │          │
│    │  ☐ Clear Transactions                           │          │
│    │  ☐ Clear Products                               │          │
│    │                                                  │          │
│    │  [Cancel]  [Confirm] ───────────────────────┐   │          │
│    └──────────────────────────────────────────────│───┘          │
│                                                   │              │
│    ┌──────────────────────────────────────────────▼──┐          │
│    │ Step 2: Confirmation Dialog                     │          │
│    │  ⚠️ WARNING: This cannot be undone!            │          │
│    │                                                  │          │
│    │  Type 'CONFIRMAR' to continue:                  │          │
│    │  [________________]                             │          │
│    │                                                  │          │
│    │  [Cancel]  [Delete] (enabled when text matches) │          │
│    └──────────────────────────────────────────────────┘          │
│                                                                   │
└───────────────────────────┬───────────────────────────────────────┘
                            │
                            │ User confirms
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                      VIEW MODEL LAYER                            │
│                  (DataClearingViewModel.kt)                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  DataClearingUiState:                                            │
│    • selectedOptions: Set<ClearDataOption>                       │
│    • isLoading: Boolean                                          │
│    • errorMessage: String                                        │
│    • successMessage: String                                      │
│                                                                   │
│  Methods:                                                        │
│    • updateSelectedOptions(option, isSelected)                   │
│    • clearData(userId) ────────────────────────┐                 │
│    • clearMessages()                           │                 │
│                                                │                 │
└────────────────────────────────────────────────│─────────────────┘
                                                 │
                                                 │ Calls repository
                                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                    REPOSITORY LAYER                              │
│                 (DataClearingRepository.kt)                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  Methods (all return Result<Unit>):                              │
│    • clearAllSales(userId)         ─────────┐                    │
│    • clearAllExpenses(userId)       ────────┤                    │
│    • clearAllDeposits(userId)        ───────┤                    │
│    • clearAllTransactions()           ──────┤                    │
│    • clearAllProducts()                ─────┤                    │
│    • clearAllData(userId)               ────┤                    │
│                                             │                    │
└─────────────────────────────────────────────│────────────────────┘
                                              │
                                              │ Uses DAOs
                                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        DATA LAYER                                │
│                    (Room DAOs)                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  SaleDao                                                         │
│    • getSalesByUser(userId): Flow<List<Sale>>                    │
│    • deleteSaleById(saleId)                                      │
│                                                                   │
│  SaleItemDao                                                     │
│    • deleteSaleItemsBySaleId(saleId)                            │
│                                                                   │
│  ExpenseDao                                                      │
│    • getExpensesByUser(userId): Flow<List<Expense>>              │
│    • deleteExpenseById(expenseId)                                │
│                                                                   │
│  DepositDao                                                      │
│    • getDepositsByUser(userId): Flow<List<Deposit>>              │
│    • deleteDepositById(depositId)                                │
│                                                                   │
│  TransactionDao                                                  │
│    • getAllTransactions(): Flow<List<Transaction>>               │
│    • deleteTransactionById(transactionId)                        │
│                                                                   │
│  ProductDao                                                      │
│    • getAllProducts(): Flow<List<Product>>                       │
│    • deleteProduct(product)                                      │
│                                                                   │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                                │ SQL Operations
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                     ROOM DATABASE                                │
│                (AccountingDatabase.kt)                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  Tables:                                                         │
│    • sales                                                       │
│    • sale_items (FK: saleId)                                    │
│    • expenses                                                    │
│    • deposits                                                    │
│    • transactions                                                │
│    • products                                                    │
│    • categories (PRESERVED)                                      │
│    • users (PRESERVED)                                           │
│                                                                   │
│  Foreign Key Constraints:                                        │
│    • sale_items.saleId → sales.id (CASCADE)                     │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘

═══════════════════════════════════════════════════════════════════
                        DATA FLOW
═══════════════════════════════════════════════════════════════════

┌──────────────────────────────────────────────────────────────────┐
│ 1. User clicks "Limpar Dados" button                             │
│ 2. Selection dialog shows with checkboxes                        │
│ 3. User selects data types to clear                              │
│ 4. ViewModel updates selectedOptions state                       │
│ 5. User clicks "Confirmar"                                       │
│ 6. Confirmation dialog shows                                     │
│ 7. User types "CONFIRMAR" and clicks "Excluir"                  │
│ 8. ViewModel.clearData(userId) called                            │
│ 9. Repository methods execute based on selections:               │
│    a. Get data via Flow.first()                                  │
│    b. Delete each item (respecting FK constraints)               │
│    c. Return Result<Unit> (success or failure)                   │
│ 10. ViewModel updates state with success/error message           │
│ 11. UI shows result to user                                      │
│ 12. Success message auto-dismisses after 2 seconds               │
└──────────────────────────────────────────────────────────────────┘

═══════════════════════════════════════════════════════════════════
                    DEPENDENCY INJECTION (Hilt)
═══════════════════════════════════════════════════════════════════

Application
    │
    ├──> @Singleton DataClearingRepository
    │        ├─> SaleDao
    │        ├─> SaleItemDao
    │        ├─> ExpenseDao
    │        ├─> DepositDao
    │        ├─> TransactionDao
    │        ├─> ProductDao
    │        └─> CategoryDao
    │
    └──> @HiltViewModel DataClearingViewModel
             └─> DataClearingRepository

═══════════════════════════════════════════════════════════════════
                        ERROR HANDLING
═══════════════════════════════════════════════════════════════════

Repository Layer:
    try {
        // Delete operations
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

ViewModel Layer:
    if (result.isFailure) {
        uiState.errorMessage = "Error message"
    } else {
        uiState.successMessage = "Success message"
    }

UI Layer:
    • Loading indicator while isLoading = true
    • Error message in red text
    • Success message in primary color
    • Auto-dismiss on success
```
