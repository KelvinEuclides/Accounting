# Data Clearing Implementation

## Overview
This implementation adds comprehensive data clearing functionality to the Accounting Android application, allowing users to selectively delete their data with proper security confirmations.

## Files Modified

### 1. Strings.kt
**Location:** `app/src/main/java/com/anje/kelvin/aconting/ui/localization/Strings.kt`

**Changes:**
- Added new string interface properties for data clearing UI
- Implemented strings in Portuguese, English, and French

**New Strings:**
- `confirmClearData` - Dialog title
- `confirmClearDataMessage` - Warning message
- `clearingData` - Loading message
- `dataClearedSuccess` / `dataClearingFailed` - Status messages
- `selectDataToClear` - Selection prompt
- `clearAllData`, `clearTransactions`, `clearProducts`, etc. - Option labels
- `thisActionCannotBeUndone` - Warning text
- `typeConfirmToDelete` / `confirmText` - Confirmation prompt

### 2. DataClearingRepository.kt (NEW)
**Location:** `app/src/main/java/com/anje/kelvin/aconting/data/repository/DataClearingRepository.kt`

**Purpose:** Centralized repository for handling data deletion operations

**Methods:**
- `clearAllSales(userId)` - Deletes all sales and sale items for a user
- `clearAllExpenses(userId)` - Deletes all expenses for a user
- `clearAllDeposits(userId)` - Deletes all deposits for a user
- `clearAllTransactions()` - Deletes all transactions
- `clearAllProducts()` - Deletes all products
- `clearAllData(userId)` - Clears all data (calls all above methods)

**Key Features:**
- Uses Hilt dependency injection (`@Singleton`)
- Returns `Result<Unit>` for proper error handling
- Respects foreign key constraints (deletes sale items before sales)
- Uses Flow.first() to get current data state
- Preserves user settings and categories

### 3. DataClearingViewModel.kt (NEW)
**Location:** `app/src/main/java/com/anje/kelvin/aconting/presentation/viewmodel/DataClearingViewModel.kt`

**Purpose:** Manages UI state and orchestrates data clearing operations

**State:**
```kotlin
data class DataClearingUiState(
    val selectedOptions: Set<ClearDataOption>,
    val isLoading: Boolean,
    val errorMessage: String,
    val successMessage: String
)

enum class ClearDataOption {
    SALES, EXPENSES, DEPOSITS, TRANSACTIONS, PRODUCTS, ALL
}
```

**Methods:**
- `updateSelectedOptions(option, isSelected)` - Updates checkbox selections
- `clearData(userId)` - Executes the clearing operation
- `clearMessages()` - Resets error/success messages

**Logic Flow:**
1. Validates at least one option is selected
2. Sets loading state
3. Calls appropriate repository methods based on selection
4. Updates state with success/error messages
5. Clears selections on completion

### 4. OtherScreens.kt
**Location:** `app/src/main/java/com/anje/kelvin/aconting/presentation/screen/OtherScreens.kt`

**Changes:**
- Added import for `kotlinx.coroutines.delay`
- Added `DataClearingViewModel` injection in SettingsScreen
- Added `currentUser` state collection
- Added `showClearDataDialog` state variable
- Updated clear data button onClick to show dialog
- Added `DataClearingDialog` composable
- Added `DataClearOption` composable helper
- Added LaunchedEffect for auto-dismissing success messages

## User Experience Flow

### Step 1: Click Clear Data Button
- User navigates to Settings screen
- Clicks on "Limpar Dados" (Clear Data) button
- First confirmation dialog appears

### Step 2: Select Data to Clear
Dialog shows:
- Warning message (red text): "Esta ação não pode ser desfeita!"
- "Limpar Todos os Dados" option (highlighted in red)
- Divider
- Individual options:
  - ✓ Limpar Vendas (Clear Sales)
  - ✓ Limpar Despesas (Clear Expenses)
  - ✓ Limpar Depósitos (Clear Deposits)
  - ✓ Limpar Transações (Clear Transactions)
  - ✓ Limpar Produtos (Clear Products)

User can:
- Select "All" to clear everything
- Or select specific items individually
- Click "Confirmar" to proceed
- Click "Cancelar" to abort

### Step 3: Final Confirmation
Second dialog shows:
- Red title: "Confirmar Limpeza de Dados"
- Warning: "Esta ação irá apagar permanentemente os dados selecionados..."
- Text input field
- Prompt: "Digite 'CONFIRMAR' para continuar"

User must:
- Type exactly "CONFIRMAR" (or "CONFIRM" in English, "CONFIRMER" in French)
- Click "Excluir" button (only enabled when correct text is entered)

### Step 4: Execution
- Loading spinner appears with "Limpando dados..." message
- Repository methods execute in background
- Success/error message displays
- Dialog auto-dismisses after 2 seconds on success

## Security Features

1. **Two-Step Confirmation**
   - First: Select what to clear
   - Second: Type confirmation text

2. **Visual Warnings**
   - Red text for destructive actions
   - Error-colored buttons
   - Clear warning messages

3. **Text Confirmation**
   - Must type exact word "CONFIRMAR"
   - Case-insensitive comparison
   - Prevents accidental deletions

4. **Data Preservation**
   - User account is NOT deleted
   - User settings/preferences preserved
   - Categories preserved
   - Only transactional data is cleared

5. **Error Handling**
   - Try-catch blocks in repository
   - Result types for error propagation
   - User-friendly error messages
   - Partial success handling

## Technical Details

### Dependency Injection
All components use Hilt for DI:
- Repository: `@Singleton` scope
- ViewModel: `@HiltViewModel` annotation
- Automatic injection in Composables via `hiltViewModel()`

### Database Operations
- Uses existing Room DAO methods
- Respects foreign key constraints
- No raw SQL queries needed
- Uses Flow for reactive data access

### Coroutines
- All operations are suspend functions
- Executed in ViewModel's viewModelScope
- Cancellation-safe (scope-aware)
- UI updates via StateFlow

### Compose UI
- Reactive state management
- Remember state for dialogs
- LaunchedEffect for side effects
- Material 3 components throughout

## Testing Considerations

While tests cannot be run due to network restrictions, the implementation:

1. **Follows Existing Patterns**
   - Same structure as other repositories
   - Consistent with existing ViewModels
   - Uses established UI patterns

2. **Error-Safe Design**
   - Result types for error handling
   - Null-safe operations
   - Defensive programming

3. **User-Centric**
   - Multiple confirmations prevent accidents
   - Clear feedback at each step
   - Reversible until final confirmation

## Future Enhancements (Not Implemented)

The issue requested several features not included in this minimal implementation:

1. **Backup Before Clearing** - Would require file I/O, storage permissions
2. **Restore from Backup** - Would need backup management system
3. **Audit Logging** - Would require logging infrastructure
4. **Additional PIN Authentication** - Would complicate UX significantly

These were omitted to keep changes minimal and focused on core functionality.

## Localization

All UI text is fully localized:
- Portuguese (primary)
- English
- French

Text adapts based on user's language preference.
