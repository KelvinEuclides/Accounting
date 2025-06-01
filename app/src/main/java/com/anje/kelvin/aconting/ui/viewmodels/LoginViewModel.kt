package com.anje.kelvin.aconting.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * ViewModel for the login screen
 */
class LoginViewModel : ViewModel() {
    var phone by mutableStateOf("")
        private set
    
    var pin by mutableStateOf("")
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    fun updatePhone(phone: String) {
        this.phone = phone
    }
    
    fun updatePin(pin: String) {
        this.pin = pin
    }
    
    fun login(onSuccess: () -> Unit) {
        isLoading = true
        errorMessage = null
        
        // In a real app, this would validate credentials with a repository
        // For this example, we'll simulate a login process
        if (phone.isNotEmpty() && pin.isNotEmpty()) {
            // Successful login
            isLoading = false
            onSuccess()
        } else {
            // Failed login
            isLoading = false
            errorMessage = "Por favor, insira o número de telefone e o PIN"
        }
    }
}
