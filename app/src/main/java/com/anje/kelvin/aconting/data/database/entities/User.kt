package com.anje.kelvin.aconting.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import java.util.Date

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["phone"], unique = true),
        Index(value = ["email"], unique = true)
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val name: String,
    val phone: String,
    val email: String? = null, // Optional email
    val pinHash: String, // Hashed PIN for security
    val salt: String, // Salt for PIN hashing
    val isActive: Boolean = true,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val lastLoginAt: Date? = null,
    
    // User preferences
    val preferredLanguage: String = "pt", // Default to Portuguese
    val preferredCurrency: String = "AOA", // Default to Angolan Kwanza
    val darkModeEnabled: Boolean = false,
    val notificationsEnabled: Boolean = true,
    
    // Security settings
    val loginAttempts: Int = 0,
    val isLocked: Boolean = false,
    val lockedUntil: Date? = null,
    
    // Profile image (optional)
    val profileImagePath: String? = null,
    
    // Role for potential admin features
    val role: UserRole = UserRole.USER
)

enum class UserRole {
    USER,
    ADMIN
}

// Data transfer objects for API/UI layer
data class UserRegistrationRequest(
    val name: String,
    val phone: String,
    val email: String?,
    val pin: String,
    val confirmPin: String
)

data class UserLoginRequest(
    val phone: String,
    val pin: String
)

data class UserUpdateRequest(
    val name: String? = null,
    val email: String? = null,
    val preferredLanguage: String? = null,
    val preferredCurrency: String? = null,
    val darkModeEnabled: Boolean? = null,
    val notificationsEnabled: Boolean? = null
)

data class UserProfile(
    val id: Long,
    val name: String,
    val phone: String,
    val email: String?,
    val isActive: Boolean,
    val createdAt: Date,
    val lastLoginAt: Date?,
    val preferredLanguage: String,
    val preferredCurrency: String,
    val darkModeEnabled: Boolean,
    val notificationsEnabled: Boolean,
    val profileImagePath: String?,
    val role: UserRole
)

// Extension function to convert User entity to UserProfile
fun User.toUserProfile(): UserProfile {
    return UserProfile(
        id = id,
        name = name,
        phone = phone,
        email = email,
        isActive = isActive,
        createdAt = createdAt,
        lastLoginAt = lastLoginAt,
        preferredLanguage = preferredLanguage,
        preferredCurrency = preferredCurrency,
        darkModeEnabled = darkModeEnabled,
        notificationsEnabled = notificationsEnabled,
        profileImagePath = profileImagePath,
        role = role
    )
}