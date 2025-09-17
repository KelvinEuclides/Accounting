package com.anje.kelvin.aconting.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

data class StockItem(
    val id: String,
    val name: String,
    val totalQuantity: Int,
    val availableQuantity: Int,
    val unitPrice: Double,
    val purchasePrice: Double,
    val unit: String = "Unidades"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    onNavigateBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedItemForEdit by remember { mutableStateOf<StockItem?>(null) }
    var stockItems by remember { mutableStateOf(getSampleStockItems()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gerir Estoque") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Item")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header Info
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Inventory,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Estoque",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${stockItems.size} itens no estoque",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Stock List
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(stockItems) { item ->
                    StockItemCard(
                        item = item,
                        onEdit = { 
                            selectedItemForEdit = item
                            showEditDialog = true
                        },
                        onDelete = { 
                            stockItems = stockItems.filter { it.id != item.id }
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddStockItemDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { newItem ->
                stockItems = stockItems + newItem
                showAddDialog = false
            }
        )
    }

    if (showEditDialog && selectedItemForEdit != null) {
        EditStockItemDialog(
            item = selectedItemForEdit!!,
            onDismiss = { 
                showEditDialog = false
                selectedItemForEdit = null
            },
            onSave = { updatedItem ->
                stockItems = stockItems.map { item ->
                    if (item.id == updatedItem.id) updatedItem else item
                }
                showEditDialog = false
                selectedItemForEdit = null
            }
        )
    }
}

@Composable
private fun StockItemCard(
    item: StockItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (item.availableQuantity < 5) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "Disponível",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${item.availableQuantity} ${item.unit}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Column {
                            Text(
                                text = "Total",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${item.totalQuantity} ${item.unit}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "Preço Compra",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "MT ${String.format("%.2f", item.purchasePrice)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        
                        Column {
                            Text(
                                text = "Preço Venda",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "MT ${String.format("%.2f", item.unitPrice)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                Column {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete, 
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            // Low stock warning
            if (item.availableQuantity < 5) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Estoque baixo - menos de 5 unidades",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddStockItemDialog(
    onDismiss: () -> Unit,
    onAdd: (StockItem) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var purchasePrice by remember { mutableStateOf("") }
    var salePrice by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf("Unidades") }

    val units = listOf("Unidades", "Kg", "Litros")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Item ao Estoque") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome do Item") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantidade") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = purchasePrice,
                    onValueChange = { purchasePrice = it },
                    label = { Text("Preço de Compra (MZN)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = salePrice,
                    onValueChange = { salePrice = it },
                    label = { Text("Preço de Venda (MZN)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )

                Text(
                    text = "Unidade de Medida",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Column(Modifier.selectableGroup()) {
                    units.forEach { unit ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .selectable(
                                    selected = (unit == selectedUnit),
                                    onClick = { selectedUnit = unit },
                                    role = Role.RadioButton
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (unit == selectedUnit),
                                onClick = null
                            )
                            Text(
                                text = unit,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && 
                        quantity.isNotBlank() && 
                        purchasePrice.isNotBlank() && 
                        salePrice.isNotBlank()) {
                        
                        val newItem = StockItem(
                            id = System.currentTimeMillis().toString(),
                            name = name,
                            totalQuantity = quantity.toIntOrNull() ?: 0,
                            availableQuantity = quantity.toIntOrNull() ?: 0,
                            unitPrice = salePrice.toDoubleOrNull() ?: 0.0,
                            purchasePrice = purchasePrice.toDoubleOrNull() ?: 0.0,
                            unit = selectedUnit
                        )
                        onAdd(newItem)
                    }
                },
                enabled = name.isNotBlank() && 
                         quantity.isNotBlank() && 
                         purchasePrice.isNotBlank() && 
                         salePrice.isNotBlank()
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditStockItemDialog(
    item: StockItem,
    onDismiss: () -> Unit,
    onSave: (StockItem) -> Unit
) {
    var name by remember { mutableStateOf(item.name) }
    var quantity by remember { mutableStateOf(item.totalQuantity.toString()) }
    var availableQuantity by remember { mutableStateOf(item.availableQuantity.toString()) }
    var purchasePrice by remember { mutableStateOf(item.purchasePrice.toString()) }
    var salePrice by remember { mutableStateOf(item.unitPrice.toString()) }
    var selectedUnit by remember { mutableStateOf(item.unit) }

    val units = listOf("Unidades", "Kg", "Litros")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Item do Estoque") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome do Item") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantidade Total") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = availableQuantity,
                    onValueChange = { availableQuantity = it },
                    label = { Text("Quantidade Disponível") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = purchasePrice,
                    onValueChange = { purchasePrice = it },
                    label = { Text("Preço de Compra (MZN)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = salePrice,
                    onValueChange = { salePrice = it },
                    label = { Text("Preço de Venda (MZN)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )

                Text(
                    text = "Unidade de Medida",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Column(Modifier.selectableGroup()) {
                    units.forEach { unit ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .selectable(
                                    selected = (unit == selectedUnit),
                                    onClick = { selectedUnit = unit },
                                    role = Role.RadioButton
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (unit == selectedUnit),
                                onClick = null
                            )
                            Text(
                                text = unit,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && 
                        quantity.isNotBlank() && 
                        availableQuantity.isNotBlank() &&
                        purchasePrice.isNotBlank() && 
                        salePrice.isNotBlank()) {
                        
                        val updatedItem = item.copy(
                            name = name,
                            totalQuantity = quantity.toIntOrNull() ?: item.totalQuantity,
                            availableQuantity = availableQuantity.toIntOrNull() ?: item.availableQuantity,
                            unitPrice = salePrice.toDoubleOrNull() ?: item.unitPrice,
                            purchasePrice = purchasePrice.toDoubleOrNull() ?: item.purchasePrice,
                            unit = selectedUnit
                        )
                        onSave(updatedItem)
                    }
                },
                enabled = name.isNotBlank() && 
                         quantity.isNotBlank() && 
                         availableQuantity.isNotBlank() &&
                         purchasePrice.isNotBlank() && 
                         salePrice.isNotBlank()
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

private fun getSampleStockItems(): List<StockItem> {
    return listOf(
        StockItem(
            id = "1",
            name = "Arroz",
            totalQuantity = 50,
            availableQuantity = 45,
            unitPrice = 120.0,
            purchasePrice = 100.0,
            unit = "Kg"
        ),
        StockItem(
            id = "2",
            name = "Açúcar",
            totalQuantity = 30,
            availableQuantity = 3,
            unitPrice = 80.0,
            purchasePrice = 65.0,
            unit = "Kg"
        ),
        StockItem(
            id = "3",
            name = "Óleo de Cozinha",
            totalQuantity = 24,
            availableQuantity = 20,
            unitPrice = 150.0,
            purchasePrice = 120.0,
            unit = "Litros"
        ),
        StockItem(
            id = "4",
            name = "Sabão em Pó",
            totalQuantity = 15,
            availableQuantity = 12,
            unitPrice = 250.0,
            purchasePrice = 200.0,
            unit = "Unidades"
        )
    )
}