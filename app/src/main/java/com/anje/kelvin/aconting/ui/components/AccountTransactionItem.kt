package com.anje.kelvin.aconting.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.anje.kelvin.aconting.R

data class TransactionItem(
    val description: String,
    val amount: String,
    val date: String,
    val isExpense: Boolean
)

@Composable
fun AccountTransactionItem(transaction: TransactionItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon for transaction type
            Image(
                painter = painterResource(
                    id = if (transaction.isExpense) R.drawable.dinheiro_fora 
                        else R.drawable.dinheiro_dento
                ),
                contentDescription = if (transaction.isExpense) "Expense" else "Income",
                modifier = Modifier.size(36.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Transaction details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Text(
                    text = transaction.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Amount
            Text(
                text = transaction.amount,
                style = MaterialTheme.typography.bodyLarge,
                color = if (transaction.isExpense) 
                    MaterialTheme.colorScheme.error 
                else 
                    MaterialTheme.colorScheme.tertiary
            )
        }
    }
}
