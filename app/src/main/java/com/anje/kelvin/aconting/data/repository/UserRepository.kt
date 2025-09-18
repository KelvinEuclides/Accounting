package com.anje.kelvin.aconting.data.repository

import android.content.Context
import com.anje.kelvin.aconting.data.database.AccountingDatabase
import com.anje.kelvin.aconting.data.database.dao.UserDao
import com.anje.kelvin.aconting.data.database.entities.User
import com.anje.kelvin.aconting.data.database.entities.UserProfile
import com.anje.kelvin.aconting.data.database.entities.UserRegistrationRequest
import com.anje.kelvin.aconting.data.database.entities.UserLoginRequest
import com.anje.kelvin.aconting.data.database.entities.UserUpdateRequest
import com.anje.kelvin.aconting.data.database.entities.UserRole
import com.anje.kelvin.aconting.data.database.entities.toUserProfile
import com.anje.kelvin.aconting.util.ValidationUtils
import com.anje.kelvin.aconting.util.SecurityUtils
import com.anje.kelvin.aconting.util.ValidationResult
import com.anje.kelvin.aconting.util.AppConstants
import com.anje.kelvin.aconting.util.RegistrationValidationResult
import com.anje.kelvin.aconting.util.LoginValidationResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: AccountingDatabase
) {
    private val userDao: UserDao = database.userDao()

    // Constants
    companion object {
    }

    // User Registration
    suspend fun registerUser(request: UserRegistrationRequest): UserRegistrationResult {
        try {
            // Validate input
            val validation = ValidationUtils.validateRegistration(
                request.name,
                request.phone,
                request.email,
                request.pin,
                request.confirmPin
            )

            if (!validation.isValid) {
                return UserRegistrationResult.ValidationError(validation)
            }

            // Normalize input data
            val normalizedPhone = ValidationUtils.normalizePhoneNumber(request.phone)
            val normalizedEmail = ValidationUtils.normalizeEmail(request.email)
            val normalizedName = ValidationUtils.normalizeName(request.name)

            // Check if phone already exists
            if (userDao.checkPhoneExists(normalizedPhone) > 0) {
                return UserRegistrationResult.PhoneAlreadyExists
            }

            // Check if email already exists (if provided)
            if (!normalizedEmail.isNullOrBlank() && userDao.checkEmailExists(normalizedEmail) > 0) {
                return UserRegistrationResult.EmailAlreadyExists
            }

            // Generate salt and hash PIN
            val salt = SecurityUtils.generateSalt()
            val pinHash = SecurityUtils.hashPin(request.pin, salt)

            // Create user entity
            val user = User(
                name = normalizedName,
                phone = normalizedPhone,
                email = normalizedEmail,
                pinHash = pinHash,
                salt = salt,
                createdAt = Date(),
                updatedAt = Date()
            )

            // Insert user into database
            val userId = userDao.insertUser(user)
            val createdUser = userDao.getUserById(userId)

            return if (createdUser != null) {
                UserRegistrationResult.Success(createdUser.toUserProfile())
            } else {
                UserRegistrationResult.DatabaseError("Failed to create user")
            }

        } catch (e: Exception) {
            return UserRegistrationResult.DatabaseError(e.message ?: "Unknown error occurred")
        }
    }

    // User Login
    suspend fun loginUser(request: UserLoginRequest): UserLoginResult {
        try {
            // Validate input
            val validation = ValidationUtils.validateLogin(request.phone, request.pin)
            if (!validation.isValid) {
                return UserLoginResult.ValidationError(validation)
            }

            val normalizedPhone = ValidationUtils.normalizePhoneNumber(request.phone)
            val currentTime = Date()

            // Get user for login (excludes locked users)
            val user = userDao.getUserForLogin(normalizedPhone, currentTime)
                ?: return UserLoginResult.InvalidCredentials

            // Check if account is locked
            if (user.isLocked && user.lockedUntil != null && user.lockedUntil > currentTime) {
                return UserLoginResult.AccountLocked(user.lockedUntil)
            }

            // Verify PIN
            val isPinValid = SecurityUtils.verifyPin(request.pin, user.pinHash, user.salt)

            if (!isPinValid) {
                // Increment login attempts
                val newAttempts = user.loginAttempts + 1
                userDao.updateLoginAttempts(user.id, newAttempts)

                // Lock account if too many attempts
                if (newAttempts >= AppConstants.MAX_LOGIN_ATTEMPTS) {
                    val lockUntil = Date(currentTime.time + (AppConstants.LOCKOUT_DURATION_MINUTES * 60 * 1000))
                    userDao.updateUserLockStatus(user.id, true, lockUntil)
                    return UserLoginResult.AccountLocked(lockUntil)
                }

                return UserLoginResult.InvalidCredentials
            }

            // Successful login - update last login time and reset attempts
            userDao.updateLastLogin(user.id, currentTime)

            // Get updated user profile
            val updatedUser = userDao.getUserById(user.id)
            return if (updatedUser != null) {
                UserLoginResult.Success(updatedUser.toUserProfile())
            } else {
                UserLoginResult.DatabaseError("Failed to retrieve user profile")
            }

        } catch (e: Exception) {
            return UserLoginResult.DatabaseError(e.message ?: "Unknown error occurred")
        }
    }

    // Get user by ID
    suspend fun getUserById(userId: Long): UserProfile? {
        return try {
            userDao.getUserById(userId)?.toUserProfile()
        } catch (e: Exception) {
            null
        }
    }

    // Get user by phone
    suspend fun getUserByPhone(phone: String): UserProfile? {
        return try {
            val normalizedPhone = ValidationUtils.normalizePhoneNumber(phone)
            userDao.getActiveUserByPhone(normalizedPhone)?.toUserProfile()
        } catch (e: Exception) {
            null
        }
    }

    // Update user profile
    suspend fun updateUserProfile(userId: Long, request: UserUpdateRequest): UserUpdateResult {
        try {
            val user = userDao.getUserById(userId) ?: return UserUpdateResult.UserNotFound

            // Validate updates
            request.name?.let { name ->
                val nameValidation = ValidationUtils.isValidName(name)
                if (!nameValidation.isValid) {
                    return UserUpdateResult.ValidationError(nameValidation.errorMessage ?: "Invalid name")
                }
            }

            request.email?.let { email ->
                val emailValidation = ValidationUtils.isValidEmail(email)
                if (!emailValidation.isValid) {
                    return UserUpdateResult.ValidationError(emailValidation.errorMessage ?: "Invalid email")
                }

                // Check if email is already taken by another user
                val normalizedEmail = ValidationUtils.normalizeEmail(email)
                if (!normalizedEmail.isNullOrBlank() && 
                    userDao.checkEmailExistsExcludingUser(normalizedEmail, userId) > 0) {
                    return UserUpdateResult.EmailAlreadyExists
                }
            }

            // Create updated user
            val updatedUser = user.copy(
                name = request.name?.let { ValidationUtils.normalizeName(it) } ?: user.name,
                email = request.email?.let { ValidationUtils.normalizeEmail(it) } ?: user.email,
                preferredLanguage = request.preferredLanguage ?: user.preferredLanguage,
                preferredCurrency = request.preferredCurrency ?: user.preferredCurrency,
                darkModeEnabled = request.darkModeEnabled ?: user.darkModeEnabled,
                notificationsEnabled = request.notificationsEnabled ?: user.notificationsEnabled,
                updatedAt = Date()
            )

            userDao.updateUser(updatedUser)
            return UserUpdateResult.Success(updatedUser.toUserProfile())

        } catch (e: Exception) {
            return UserUpdateResult.DatabaseError(e.message ?: "Unknown error occurred")
        }
    }

    // Change user PIN
    suspend fun changeUserPin(userId: Long, currentPin: String, newPin: String): ChangePinResult {
        try {
            val user = userDao.getUserById(userId) ?: return ChangePinResult.UserNotFound

            // Verify current PIN
            if (!SecurityUtils.verifyPin(currentPin, user.pinHash, user.salt)) {
                return ChangePinResult.InvalidCurrentPin
            }

            // Validate new PIN
            val pinValidation = ValidationUtils.isValidPin(newPin)
            if (!pinValidation.isValid) {
                return ChangePinResult.ValidationError(pinValidation.errorMessage ?: "Invalid PIN")
            }

            // Generate new salt and hash new PIN
            val newSalt = SecurityUtils.generateSalt()
            val newPinHash = SecurityUtils.hashPin(newPin, newSalt)

            // Update PIN in database
            userDao.updateUserPin(userId, newPinHash, newSalt, Date())

            return ChangePinResult.Success

        } catch (e: Exception) {
            return ChangePinResult.DatabaseError(e.message ?: "Unknown error occurred")
        }
    }

    // Deactivate user account
    suspend fun deactivateUser(userId: Long): Boolean {
        return try {
            userDao.deactivateUser(userId, Date())
            true
        } catch (e: Exception) {
            false
        }
    }

    // Get all active users (for admin)
    suspend fun getAllActiveUsers(): List<UserProfile> {
        return try {
            userDao.getAllActiveUsers().map { it.toUserProfile() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Get all active users as Flow (for reactive UI)
    fun getAllActiveUsersFlow(): Flow<List<UserProfile>> {
        return userDao.getAllActiveUsersFlow().map { users ->
            users.map { it.toUserProfile() }
        }
    }

    // Search users
    suspend fun searchUsers(query: String): List<UserProfile> {
        return try {
            val searchQuery = "%$query%"
            userDao.searchActiveUsers(searchQuery).map { it.toUserProfile() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Get user statistics
    suspend fun getUserStats(): UserStats {
        return try {
            val totalUsers = userDao.getActiveUserCount()
            val currentTime = Date()
            val oneDayAgo = Date(currentTime.time - AppConstants.MILLISECONDS_IN_DAY)
            val oneWeekAgo = Date(currentTime.time - AppConstants.MILLISECONDS_IN_WEEK)
            
            val newUsersToday = userDao.getUserRegistrationCount(oneDayAgo, currentTime)
            val newUsersThisWeek = userDao.getUserRegistrationCount(oneWeekAgo, currentTime)
            val activeLoginsToday = userDao.getUserLoginCount(oneDayAgo, currentTime)

            UserStats(
                totalActiveUsers = totalUsers,
                newUsersToday = newUsersToday,
                newUsersThisWeek = newUsersThisWeek,
                activeLoginsToday = activeLoginsToday
            )
        } catch (e: Exception) {
            UserStats(0, 0, 0, 0)
        }
    }
}

// Result sealed classes for better error handling
sealed class UserRegistrationResult {
    data class Success(val userProfile: UserProfile) : UserRegistrationResult()
    data class ValidationError(val validation: RegistrationValidationResult) : UserRegistrationResult()
    object PhoneAlreadyExists : UserRegistrationResult()
    object EmailAlreadyExists : UserRegistrationResult()
    data class DatabaseError(val message: String) : UserRegistrationResult()
}

sealed class UserLoginResult {
    data class Success(val userProfile: UserProfile) : UserLoginResult()
    data class ValidationError(val validation: LoginValidationResult) : UserLoginResult()
    object InvalidCredentials : UserLoginResult()
    data class AccountLocked(val lockedUntil: Date) : UserLoginResult()
    data class DatabaseError(val message: String) : UserLoginResult()
}

sealed class UserUpdateResult {
    data class Success(val userProfile: UserProfile) : UserUpdateResult()
    data class ValidationError(val message: String) : UserUpdateResult()
    object UserNotFound : UserUpdateResult()
    object EmailAlreadyExists : UserUpdateResult()
    data class DatabaseError(val message: String) : UserUpdateResult()
}

sealed class ChangePinResult {
    object Success : ChangePinResult()
    object UserNotFound : ChangePinResult()
    object InvalidCurrentPin : ChangePinResult()
    data class ValidationError(val message: String) : ChangePinResult()
    data class DatabaseError(val message: String) : ChangePinResult()
}

data class UserStats(
    val totalActiveUsers: Int,
    val newUsersToday: Int,
    val newUsersThisWeek: Int,
    val activeLoginsToday: Int
)