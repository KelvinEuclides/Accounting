package com.anje.kelvin.aconting.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.anje.kelvin.aconting.data.database.entities.UserProfile
import com.anje.kelvin.aconting.data.database.entities.UserRegistrationRequest
import com.anje.kelvin.aconting.data.database.entities.UserLoginRequest
import com.anje.kelvin.aconting.data.database.entities.UserUpdateRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository
) {
    // Use a proper coroutine scope for this repository
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    // Encrypted SharedPreferences for session data
    private val authPrefs: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
            
        EncryptedSharedPreferences.create(
            context,
            "auth_session_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    
    // Authentication state
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()
    
    // Session management
    private val _sessionExpiry = MutableStateFlow<Date?>(null)
    val sessionExpiry: StateFlow<Date?> = _sessionExpiry.asStateFlow()

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_SESSION_TOKEN = "session_token"
        private const val KEY_SESSION_EXPIRY = "session_expiry"
        private const val SESSION_DURATION_HOURS = 24
    }
    
    init {
        try {
            // Initialize auth state after all StateFlow properties are ready
            _isLoggedIn.value = checkAuthState()
            
            // Load current user if logged in (using repository scope)
            if (_isLoggedIn.value) {
                repositoryScope.launch {
                    try {
                        loadCurrentUser()
                    } catch (e: Exception) {
                        // If loading user fails, clear session and log the error
                        Log.e("AuthRepository", "Failed to load current user during init", e)
                        clearSession()
                        _isLoggedIn.value = false
                        _currentUser.value = null
                        _sessionExpiry.value = null
                    }
                }
            }
        } catch (e: Exception) {
            // Handle any initialization errors gracefully
            Log.e("AuthRepository", "Error during AuthRepository initialization", e)
            _isLoggedIn.value = false
            _currentUser.value = null
            _sessionExpiry.value = null
        }
    }
    
    // User Authentication Methods
    
    /**
     * Register a new user
     */
    suspend fun register(request: UserRegistrationRequest): AuthResult {
        return when (val result = userRepository.registerUser(request)) {
            is UserRegistrationResult.Success -> {
                // Start session for newly registered user
                startSession(result.userProfile)
                AuthResult.Success
            }
            is UserRegistrationResult.ValidationError -> {
                AuthResult.ValidationError(
                    nameError = result.validation.nameError,
                    phoneError = result.validation.phoneError,
                    emailError = result.validation.emailError,
                    pinError = result.validation.pinError,
                    confirmPinError = result.validation.confirmPinError
                )
            }
            is UserRegistrationResult.PhoneAlreadyExists -> {
                AuthResult.PhoneAlreadyExists
            }
            is UserRegistrationResult.EmailAlreadyExists -> {
                AuthResult.EmailAlreadyExists
            }
            is UserRegistrationResult.DatabaseError -> {
                AuthResult.DatabaseError(result.message)
            }
        }
    }
    
    /**
     * Login an existing user
     */
    suspend fun login(phone: String, pin: String): AuthResult {
        val request = UserLoginRequest(phone, pin)
        
        return when (val result = userRepository.loginUser(request)) {
            is UserLoginResult.Success -> {
                startSession(result.userProfile)
                AuthResult.Success
            }
            is UserLoginResult.ValidationError -> {
                AuthResult.ValidationError(
                    phoneError = result.validation.phoneError,
                    pinError = result.validation.pinError
                )
            }
            is UserLoginResult.InvalidCredentials -> {
                AuthResult.InvalidCredentials
            }
            is UserLoginResult.AccountLocked -> {
                AuthResult.AccountLocked(result.lockedUntil)
            }
            is UserLoginResult.DatabaseError -> {
                AuthResult.DatabaseError(result.message)
            }
        }
    }
    
    /**
     * Logout current user
     */
    fun logout() {
        clearSession()
        _isLoggedIn.value = false
        _currentUser.value = null
        _sessionExpiry.value = null
    }
    
    /**
     * Update current user profile
     */
    suspend fun updateProfile(request: UserUpdateRequest): AuthResult {
        val currentUserId = getCurrentUserId() ?: return AuthResult.NotLoggedIn
        
        return when (val result = userRepository.updateUserProfile(currentUserId, request)) {
            is UserUpdateResult.Success -> {
                _currentUser.value = result.userProfile
                AuthResult.Success
            }
            is UserUpdateResult.ValidationError -> {
                AuthResult.ValidationError(generalError = result.message)
            }
            is UserUpdateResult.UserNotFound -> {
                AuthResult.UserNotFound
            }
            is UserUpdateResult.EmailAlreadyExists -> {
                AuthResult.EmailAlreadyExists
            }
            is UserUpdateResult.DatabaseError -> {
                AuthResult.DatabaseError(result.message)
            }
        }
    }
    
    /**
     * Change user PIN
     */
    suspend fun changePin(currentPin: String, newPin: String): AuthResult {
        val currentUserId = getCurrentUserId() ?: return AuthResult.NotLoggedIn
        
        return when (val result = userRepository.changeUserPin(currentUserId, currentPin, newPin)) {
            is ChangePinResult.Success -> {
                AuthResult.Success
            }
            is ChangePinResult.InvalidCurrentPin -> {
                AuthResult.InvalidCredentials
            }
            is ChangePinResult.ValidationError -> {
                AuthResult.ValidationError(pinError = result.message)
            }
            is ChangePinResult.UserNotFound -> {
                AuthResult.UserNotFound
            }
            is ChangePinResult.DatabaseError -> {
                AuthResult.DatabaseError(result.message)
            }
        }
    }
    
    /**
     * Delete current user account
     */
    suspend fun deleteAccount(): AuthResult {
        val currentUserId = getCurrentUserId() ?: return AuthResult.NotLoggedIn
        
        return if (userRepository.deactivateUser(currentUserId)) {
            logout()
            AuthResult.Success
        } else {
            AuthResult.DatabaseError("Failed to delete account")
        }
    }
    
    // Session Management Methods
    
    private fun checkAuthState(): Boolean {
        val hasSession = authPrefs.contains(KEY_USER_ID) && 
                        authPrefs.contains(KEY_SESSION_TOKEN)
        val isSessionValid = checkSessionValidity()
        return hasSession && isSessionValid
    }
    
    private suspend fun loadCurrentUser() {
        val userId = getCurrentUserId()
        if (userId != null) {
            _currentUser.value = userRepository.getUserById(userId)
        }
    }
    
    private fun checkSessionValidity(): Boolean {
        val sessionExpiry = authPrefs.getLong(KEY_SESSION_EXPIRY, 0L)
        val currentTime = System.currentTimeMillis()
        
        return if (sessionExpiry > currentTime) {
            _sessionExpiry.value = Date(sessionExpiry)
            true
        } else {
            // Session expired, clear auth data
            if (sessionExpiry != 0L) {
                clearSession()
                _isLoggedIn.value = false
                _currentUser.value = null
                _sessionExpiry.value = null
            }
            false
        }
    }
    
    private fun startSession(userProfile: UserProfile) {
        val sessionToken = generateSessionToken()
        val expiryTime = System.currentTimeMillis() + (SESSION_DURATION_HOURS * 60 * 60 * 1000)
        
        authPrefs.edit()
            .putLong(KEY_USER_ID, userProfile.id)
            .putString(KEY_SESSION_TOKEN, sessionToken)
            .putLong(KEY_SESSION_EXPIRY, expiryTime)
            .apply()
        
        _isLoggedIn.value = true
        _currentUser.value = userProfile
        _sessionExpiry.value = Date(expiryTime)
    }
    
    private fun clearSession() {
        authPrefs.edit()
            .remove(KEY_USER_ID)
            .remove(KEY_SESSION_TOKEN)
            .remove(KEY_SESSION_EXPIRY)
            .apply()
    }
    
    private fun getCurrentUserId(): Long? {
        return if (authPrefs.contains(KEY_USER_ID)) {
            authPrefs.getLong(KEY_USER_ID, -1L).takeIf { it != -1L }
        } else {
            null
        }
    }
    
    private fun generateSessionToken(): String {
        return java.util.UUID.randomUUID().toString()
    }
    
    /**
     * Check if session will expire soon (within 1 hour)
     */
    fun isSessionExpiringSoon(): Boolean {
        val sessionExpiry = authPrefs.getLong(KEY_SESSION_EXPIRY, 0L)
        val currentTime = System.currentTimeMillis()
        val oneHourInMillis = 60 * 60 * 1000
        
        return (sessionExpiry - currentTime) <= oneHourInMillis
    }
    
    /**
     * Extend current session
     */
    fun extendSession() {
        if (_isLoggedIn.value) {
            val newExpiryTime = System.currentTimeMillis() + (SESSION_DURATION_HOURS * 60 * 60 * 1000)
            authPrefs.edit()
                .putLong(KEY_SESSION_EXPIRY, newExpiryTime)
                .apply()
            _sessionExpiry.value = Date(newExpiryTime)
        }
    }
}

// Enhanced AuthResult with detailed error handling
sealed class AuthResult {
    object Success : AuthResult()
    object NotLoggedIn : AuthResult()
    object UserNotFound : AuthResult()
    object InvalidCredentials : AuthResult()
    object PhoneAlreadyExists : AuthResult()
    object EmailAlreadyExists : AuthResult()
    data class AccountLocked(val lockedUntil: Date) : AuthResult()
    data class ValidationError(
        val nameError: String? = null,
        val phoneError: String? = null,
        val emailError: String? = null,
        val pinError: String? = null,
        val confirmPinError: String? = null,
        val generalError: String? = null
    ) : AuthResult()
    data class DatabaseError(val message: String) : AuthResult()
}