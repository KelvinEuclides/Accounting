package com.anje.kelvin.aconting.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.anje.kelvin.aconting.ui.theme.PakitiniTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePinScreen(
    onBackClick: () -> Unit,
    onPinChanged: (currentPin: String, newPin: String) -> Unit
) {
    var currentPin by remember { mutableStateOf("") }
    var newPin by remember { mutableStateOf("") }
    var confirmNewPin by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    
    PakitiniTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Alterar PIN") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Insira seu PIN atual e o novo PIN",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        OutlinedTextField(
                            value = currentPin,
                            onValueChange = { 
                                if (it.length <= 4) currentPin = it 
                            },
                            label = { Text("PIN atual") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = newPin,
                            onValueChange = { 
                                if (it.length <= 4) newPin = it 
                            },
                            label = { Text("Novo PIN") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = confirmNewPin,
                            onValueChange = { 
                                if (it.length <= 4) confirmNewPin = it 
                            },
                            label = { Text("Confirmar novo PIN") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            isError = showError && newPin != confirmNewPin,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        if (showError && newPin != confirmNewPin) {
                            Text(
                                text = "Os PINs não coincidem",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
                
                Button(
                    onClick = {
                        showError = true
                        if (newPin == confirmNewPin && newPin.isNotEmpty() && currentPin.isNotEmpty()) {
                            onPinChanged(currentPin, newPin)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    enabled = currentPin.isNotEmpty() && newPin.isNotEmpty() && confirmNewPin.isNotEmpty()
                ) {
                    Text("Salvar Alterações")
                }
            }
        }
    }
}
