package com.anje.kelvin.aconting.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen(
    onNavigateBack: () -> Unit
) {
    var selectedItems by remember { mutableStateOf(listOf<SaleItem>()) }
    var showAddItemDialog by remember { mutableStateOf(false) }
    var total by remember { mutableStateOf(0.0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova Venda") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddItemDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Adicionar Item")
                    }
                }
            )
        },
        bottomBar = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total: ${String.format("%.2f", total)} MZN",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { /* TODO: Process sale */ },
                            enabled = selectedItems.isNotEmpty()
                        ) {
                            Text("Finalizar Venda")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        if (selectedItems.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "Nenhum item adicionado",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    TextButton(
                        onClick = { showAddItemDialog = true },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Adicionar Item")
                    }
                }
            }
        } else {
            // Items list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(selectedItems) { item ->
                    SaleItemCard(
                        item = item,
                        onQuantityChange = { newQuantity ->
                            selectedItems = selectedItems.map { 
                                if (it.id == item.id) it.copy(quantity = newQuantity) else it 
                            }
                            total = selectedItems.sumOf { it.preco * it.quantity }
                        },
                        onRemove = {
                            selectedItems = selectedItems.filter { it.id != item.id }
                            total = selectedItems.sumOf { it.preco * it.quantity }
                        }
                    )
                }
            }
        }
    }

    if (showAddItemDialog) {
        AddItemDialog(
            onDismiss = { showAddItemDialog = false },
            onAddItem = { item ->
                selectedItems = selectedItems + item
                total = selectedItems.sumOf { it.preco * it.quantity }
                showAddItemDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleItemCard(
    item: SaleItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.nome,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${String.format("%.2f", item.preco)} MZN por ${item.unidade}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Remover")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) }
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Diminuir")
                    }
                    Text(
                        text = item.quantity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    IconButton(
                        onClick = { onQuantityChange(item.quantity + 1) }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar")
                    }
                }
                
                Text(
                    text = "${String.format("%.2f", item.preco * item.quantity)} MZN",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onAddItem: (SaleItem) -> Unit
) {
    var selectedItem by remember { mutableStateOf<InventoryItem?>(null) }
    var quantity by remember { mutableStateOf("1") }
    
    // Sample inventory items - in real app, this would come from ViewModel
    val inventoryItems = remember {
        listOf(
            InventoryItem(1, "Coca-Cola 500ml", 25.0, "unidade", 100),
            InventoryItem(2, "Pão", 5.0, "unidade", 50),
            InventoryItem(3, "Leite", 30.0, "litro", 20),
            InventoryItem(4, "Açúcar", 45.0, "kg", 10)
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Item") },
        text = {
            Column {
                Text(
                    text = "Selecione um item do estoque:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                LazyColumn(
                    modifier = Modifier.height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(inventoryItems) { item ->
                        Card(
                            onClick = { selectedItem = item },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedItem?.id == item.id) 
                                    MaterialTheme.colorScheme.primaryContainer 
                                else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = item.nome,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${String.format("%.2f", item.preco)} MZN por ${item.unidade}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Estoque: ${item.estoque}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
                
                if (selectedItem != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantidade") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedItem?.let { item ->
                        val qty = quantity.toIntOrNull() ?: 1
                        onAddItem(
                            SaleItem(
                                id = System.currentTimeMillis(), // Simple ID generation
                                nome = item.nome,
                                preco = item.preco,
                                unidade = item.unidade,
                                quantity = qty
                            )
                        )
                    }
                },
                enabled = selectedItem != null && quantity.toIntOrNull() != null && quantity.toIntOrNull()!! > 0
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

data class SaleItem(
    val id: Long,
    val nome: String,
    val preco: Double,
    val unidade: String,
    val quantity: Int
)

data class InventoryItem(
    val id: Long,
    val nome: String,
    val preco: Double,
    val unidade: String,
    val estoque: Int
)