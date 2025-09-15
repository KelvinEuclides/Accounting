package com.anje.kelvin.aconting.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anje.kelvin.aconting.data.repository.AuthRepository
import com.anje.kelvin.aconting.data.repository.AuthResult
import com.anje.kelvin.aconting.data.database.entities.UserProfile
import com.anje.kelvin.aconting.data.database.entities.UserRegistrationRequest
import com.anje.kelvin.aconting.data.database.entities.UserUpdateRequest
import com.anje.kelvin.aconting.util.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    // Auth state from repository
    val isLoggedIn: StateFlow<Boolean> = authRepository.isLoggedIn
    val currentUser: StateFlow<UserProfile?> = authRepository.currentUser
    val sessionExpiry = authRepository.sessionExpiry
    
    // Login UI State
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()
    
    // Register UI State
    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState.asStateFlow()
    
    // Profile UI State
    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState.asStateFlow()
    
    // Form validation
    val isLoginFormValid = combine(_loginUiState) { state ->
        val phoneValid = ValidationUtils.isValidPhone(state[0].phone).isValid
        val pinValid = state[0].pin.isNotBlank()
        phoneValid && pinValid
    }
    
    val isRegisterFormValid = combine(_registerUiState) { state ->
        val validation = ValidationUtils.validateRegistration(
            state[0].name,
            state[0].phone,
            state[0].email,
            state[0].pin,
            state[0].confirmPin
        )
        validation.isValid
    }
    
    // Login Actions
    fun updateLoginPhone(phone: String) {
        _loginUiState.value = _loginUiState.value.copy(
            phone = phone,
            phoneError = if (phone.isEmpty()) "" else {
                val validation = ValidationUtils.isValidPhone(phone)
                if (validation.isValid) "" else validation.errorMessage ?: ""
            }
        )
    }
    
    fun updateLoginPin(pin: String) {
        _loginUiState.value = _loginUiState.value.copy(
            pin = pin,
            pinError = ""
        )
    }
    
    fun login() {
        val currentState = _loginUiState.value
        
        // Clear previous errors
        _loginUiState.value = currentState.copy(
            isLoading = true,
            errorMessage = "",
            phoneError = "",
            pinError = ""
        )
        
        viewModelScope.launch {
            val result = authRepository.login(currentState.phone, currentState.pin)
            
            _loginUiState.value = when (result) {
                is AuthResult.Success -> {
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "",
                        phone = "",
                        pin = ""
                    )
                }
                is AuthResult.ValidationError -> {
                    currentState.copy(
                        isLoading = false,
                        phoneError = result.phoneError ?: "",
                        pinError = result.pinError ?: ""
                    )
                }
                is AuthResult.InvalidCredentials -> {
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Invalid phone or PIN"
                    )
                }
                is AuthResult.AccountLocked -> {
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Account locked until ${result.lockedUntil}"
                    )
                }
                is AuthResult.DatabaseError -> {
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Database error: ${result.message}"
                    )
                }
                else -> {
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Unknown error occurred"
                    )
                }
            }
        }
    }
    
    // Register Actions
    fun updateRegisterName(name: String) {
        _registerUiState.value = _registerUiState.value.copy(
            name = name,
            nameError = if (name.isEmpty()) "" else {
                val validation = ValidationUtils.isValidName(name)
                if (validation.isValid) "" else validation.errorMessage ?: ""
            }
        )
    }
    
    fun updateRegisterPhone(phone: String) {
        _registerUiState.value = _registerUiState.value.copy(
            phone = phone,
            phoneError = if (phone.isEmpty()) "" else {
                val validation = ValidationUtils.isValidPhone(phone)
                if (validation.isValid) "" else validation.errorMessage ?: ""
            }
        )
    }
    
    fun updateRegisterEmail(email: String) {
        _registerUiState.value = _registerUiState.value.copy(
            email = email,
            emailError = if (email.isEmpty()) "" else {
                val validation = ValidationUtils.isValidEmail(email)
                if (validation.isValid) "" else validation.errorMessage ?: ""
            }
        )
    }
    
    fun updateRegisterPin(pin: String) {
        _registerUiState.value = _registerUiState.value.copy(
            pin = pin,
            pinError = if (pin.isEmpty()) "" else {
                val validation = ValidationUtils.isValidPin(pin)
                if (validation.isValid) "" else validation.errorMessage ?: ""
            }
        )
    }
    
    fun updateRegisterConfirmPin(confirmPin: String) {
        val currentState = _registerUiState.value
        _registerUiState.value = currentState.copy(
            confirmPin = confirmPin,
            confirmPinError = if (confirmPin.isEmpty()) "" else {
                val validation = ValidationUtils.isValidPinConfirmation(currentState.pin, confirmPin)
                if (validation.isValid) "" else validation.errorMessage ?: ""
            }
        )
    }
    
    fun register() {
        val currentState = _registerUiState.value
        
        // Clear previous errors
        _registerUiState.value = currentState.copy(
            isLoading = true,
            errorMessage = "",
            nameError = "",
            phoneError = "",
            emailError = "",
            pinError = "",
            confirmPinError = ""
        )
        
        viewModelScope.launch {
            val request = UserRegistrationRequest(
                name = currentState.name,
                phone = currentState.phone,
                email = currentState.email.takeIf { it.isNotBlank() },
                pin = currentState.pin,
                confirmPin = currentState.confirmPin
            )
            
            val result = authRepository.register(request)
            
            _registerUiState.value = when (result) {
                is AuthResult.Success -> {
                    RegisterUiState() // Reset form
                }
                is AuthResult.ValidationError -> {
                    currentState.copy(
                        isLoading = false,
                        nameError = result.nameError ?: "",
                        phoneError = result.phoneError ?: "",
                        emailError = result.emailError ?: "",
                        pinError = result.pinError ?: "",
                        confirmPinError = result.confirmPinError ?: ""
                    )
                }
                is AuthResult.PhoneAlreadyExists -> {
                    currentState.copy(
                        isLoading = false,
                        phoneError = "Phone number already registered"
                    )
                }
                is AuthResult.EmailAlreadyExists -> {
                    currentState.copy(
                        isLoading = false,
                        emailError = "Email already registered"
                    )
                }
                is AuthResult.DatabaseError -> {
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Database error: ${result.message}"
                    )
                }
                else -> {
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Unknown error occurred"
                    )
                }
            }
        }
    }
    
    // Profile Actions
    fun updateProfileName(name: String) {
        _profileUiState.value = _profileUiState.value.copy(
            name = name,
            nameError = if (name.isEmpty()) "" else {
                val validation = ValidationUtils.isValidName(name)
                if (validation.isValid) "" else validation.errorMessage ?: ""
            }
        )
    }
    
    fun updateProfileEmail(email: String) {
        _profileUiState.value = _profileUiState.value.copy(
            email = email,
            emailError = if (email.isEmpty()) "" else {
                val validation = ValidationUtils.isValidEmail(email)
                if (validation.isValid) "" else validation.errorMessage ?: ""
            }
        )
    }
    
    fun updateProfile() {
        val currentState = _profileUiState.value
        
        _profileUiState.value = currentState.copy(
            isLoading = true,
            errorMessage = "",
            successMessage = "",
            nameError = "",
            emailError = ""
        )
        
        viewModelScope.launch {
            val request = UserUpdateRequest(
                name = currentState.name.takeIf { it.isNotBlank() },
                email = currentState.email.takeIf { it.isNotBlank() }
            )
            
            val result = authRepository.updateProfile(request)
            
            _profileUiState.value = when (result) {
                is AuthResult.Success -> {
                    currentState.copy(
                        isLoading = false,
                        successMessage = "Profile updated successfully"
                    )
                }
                is AuthResult.ValidationError -> {
                    currentState.copy(
                        isLoading = false,
                        nameError = result.nameError ?: "",
                        emailError = result.emailError ?: "",
                        errorMessage = result.generalError ?: ""
                    )
                }
                is AuthResult.EmailAlreadyExists -> {
                    currentState.copy(
                        isLoading = false,
                        emailError = "Email already registered"
                    )
                }
                is AuthResult.NotLoggedIn -> {
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Please login first"
                    )
                }
                is AuthResult.DatabaseError -> {
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Database error: ${result.message}"
                    )
                }
                else -> {
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Unknown error occurred"
                    )
                }
            }
        }
    }
    
    fun changePin(currentPin: String, newPin: String) {
        viewModelScope.launch {
            val result = authRepository.changePin(currentPin, newPin)
            
            _profileUiState.value = when (result) {
                is AuthResult.Success -> {
                    _profileUiState.value.copy(
                        successMessage = "PIN changed successfully"
                    )
                }
                is AuthResult.InvalidCredentials -> {
                    _profileUiState.value.copy(
                        errorMessage = "Current PIN is incorrect"
                    )
                }
                is AuthResult.ValidationError -> {
                    _profileUiState.value.copy(
                        errorMessage = result.pinError ?: "Invalid PIN"
                    )
                }
                else -> {
                    _profileUiState.value.copy(
                        errorMessage = "Error changing PIN"
                    )
                }
            }
        }
    }
    
    fun logout() {
        authRepository.logout()
        // Reset all UI states
        _loginUiState.value = LoginUiState()
        _registerUiState.value = RegisterUiState()
        _profileUiState.value = ProfileUiState()
    }
    
    fun deleteAccount() {
        viewModelScope.launch {
            val result = authRepository.deleteAccount()
            
            _profileUiState.value = when (result) {
                is AuthResult.Success -> {
                    ProfileUiState() // Reset state as user is deleted
                }
                else -> {
                    _profileUiState.value.copy(
                        errorMessage = "Error deleting account"
                    )
                }
            }
        }
    }
    
    // Session management
    fun extendSession() {
        authRepository.extendSession()
    }
    
    fun isSessionExpiringSoon(): Boolean {
        return authRepository.isSessionExpiringSoon()
    }
    
    // Clear error messages
    fun clearLoginMessages() {
        _loginUiState.value = _loginUiState.value.copy(
            errorMessage = "",
            phoneError = "",
            pinError = ""
        )
    }
    
    fun clearRegisterMessages() {
        _registerUiState.value = _registerUiState.value.copy(
            errorMessage = "",
            nameError = "",
            phoneError = "",
            emailError = "",
            pinError = "",
            confirmPinError = ""
        )
    }
    
    fun clearProfileMessages() {
        _profileUiState.value = _profileUiState.value.copy(
            errorMessage = "",
            successMessage = "",
            nameError = "",
            emailError = ""
        )
    }
}

// UI State Data Classes
data class LoginUiState(
    val phone: String = "",
    val pin: String = "",
    val phoneError: String = "",
    val pinError: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

data class RegisterUiState(
    val phone: String = "",
    val pin: String = "",
    val confirmPin: String = "",
    val name: String = "",
    val email: String = "",
    val phoneError: String = "",
    val pinError: String = "",
    val confirmPinError: String = "",
    val nameError: String = "",
    val emailError: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val nameError: String = "",
    val emailError: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = ""
)