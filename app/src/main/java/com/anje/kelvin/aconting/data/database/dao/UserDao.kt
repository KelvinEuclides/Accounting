package com.anje.kelvin.aconting.data.database.dao

import androidx.room.*
import com.anje.kelvin.aconting.data.database.entities.User
import com.anje.kelvin.aconting.data.database.entities.UserRole
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface UserDao {

    // Create operations
    @Insert
    suspend fun insertUser(user: User): Long

    @Insert
    suspend fun insertUsers(users: List<User>): List<Long>

    // Read operations
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?

    @Query("SELECT * FROM users WHERE phone = :phone")
    suspend fun getUserByPhone(phone: String): User?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE phone = :phone AND isActive = 1")
    suspend fun getActiveUserByPhone(phone: String): User?

    @Query("SELECT * FROM users WHERE isActive = 1 ORDER BY createdAt DESC")
    suspend fun getAllActiveUsers(): List<User>

    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActiveUsersFlow(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE role = :role ORDER BY createdAt DESC")
    suspend fun getUsersByRole(role: UserRole): List<User>

    @Query("SELECT COUNT(*) FROM users WHERE isActive = 1")
    suspend fun getActiveUserCount(): Int

    @Query("SELECT COUNT(*) FROM users WHERE phone = :phone")
    suspend fun checkPhoneExists(phone: String): Int

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun checkEmailExists(email: String): Int

    @Query("SELECT COUNT(*) FROM users WHERE phone = :phone AND id != :excludeUserId")
    suspend fun checkPhoneExistsExcludingUser(phone: String, excludeUserId: Long): Int

    @Query("SELECT COUNT(*) FROM users WHERE email = :email AND id != :excludeUserId")
    suspend fun checkEmailExistsExcludingUser(email: String, excludeUserId: Long): Int

    // Update operations
    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE users SET name = :name, email = :email, updatedAt = :updatedAt WHERE id = :userId")
    suspend fun updateUserProfile(userId: Long, name: String, email: String?, updatedAt: Date)

    @Query("UPDATE users SET pinHash = :pinHash, salt = :salt, updatedAt = :updatedAt WHERE id = :userId")
    suspend fun updateUserPin(userId: Long, pinHash: String, salt: String, updatedAt: Date)

    @Query("UPDATE users SET lastLoginAt = :lastLoginAt, loginAttempts = 0, isLocked = 0, lockedUntil = NULL WHERE id = :userId")
    suspend fun updateLastLogin(userId: Long, lastLoginAt: Date)

    @Query("UPDATE users SET loginAttempts = :attempts WHERE id = :userId")
    suspend fun updateLoginAttempts(userId: Long, attempts: Int)

    @Query("UPDATE users SET isLocked = :isLocked, lockedUntil = :lockedUntil WHERE id = :userId")
    suspend fun updateUserLockStatus(userId: Long, isLocked: Boolean, lockedUntil: Date?)

    @Query("UPDATE users SET isActive = 0, updatedAt = :updatedAt WHERE id = :userId")
    suspend fun deactivateUser(userId: Long, updatedAt: Date)

    @Query("UPDATE users SET isActive = 1, updatedAt = :updatedAt WHERE id = :userId")
    suspend fun activateUser(userId: Long, updatedAt: Date)

    @Query("UPDATE users SET preferredLanguage = :language, updatedAt = :updatedAt WHERE id = :userId")
    suspend fun updateUserLanguage(userId: Long, language: String, updatedAt: Date)

    @Query("UPDATE users SET preferredCurrency = :currency, updatedAt = :updatedAt WHERE id = :userId")
    suspend fun updateUserCurrency(userId: Long, currency: String, updatedAt: Date)

    @Query("UPDATE users SET darkModeEnabled = :enabled, updatedAt = :updatedAt WHERE id = :userId")
    suspend fun updateDarkModePreference(userId: Long, enabled: Boolean, updatedAt: Date)

    @Query("UPDATE users SET notificationsEnabled = :enabled, updatedAt = :updatedAt WHERE id = :userId")
    suspend fun updateNotificationPreference(userId: Long, enabled: Boolean, updatedAt: Date)

    @Query("UPDATE users SET profileImagePath = :imagePath, updatedAt = :updatedAt WHERE id = :userId")
    suspend fun updateProfileImage(userId: Long, imagePath: String?, updatedAt: Date)

    @Query("UPDATE users SET role = :role, updatedAt = :updatedAt WHERE id = :userId")
    suspend fun updateUserRole(userId: Long, role: UserRole, updatedAt: Date)

    // Delete operations
    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUserById(userId: Long)

    @Query("DELETE FROM users WHERE isActive = 0 AND updatedAt < :cutoffDate")
    suspend fun deleteInactiveUsersOlderThan(cutoffDate: Date)

    // Security operations
    @Query("SELECT * FROM users WHERE phone = :phone AND isLocked = 0 AND (lockedUntil IS NULL OR lockedUntil < :currentTime)")
    suspend fun getUserForLogin(phone: String, currentTime: Date): User?

    @Query("UPDATE users SET loginAttempts = loginAttempts + 1 WHERE id = :userId")
    suspend fun incrementLoginAttempts(userId: Long)

    @Query("SELECT loginAttempts FROM users WHERE id = :userId")
    suspend fun getLoginAttempts(userId: Long): Int

    // Search operations
    @Query("SELECT * FROM users WHERE (name LIKE :searchQuery OR phone LIKE :searchQuery OR email LIKE :searchQuery) AND isActive = 1 ORDER BY name ASC")
    suspend fun searchActiveUsers(searchQuery: String): List<User>

    @Query("SELECT * FROM users WHERE name LIKE :nameQuery AND isActive = 1 ORDER BY name ASC")
    suspend fun searchUsersByName(nameQuery: String): List<User>

    // Statistics
    @Query("SELECT COUNT(*) FROM users WHERE createdAt >= :startDate AND createdAt < :endDate")
    suspend fun getUserRegistrationCount(startDate: Date, endDate: Date): Int

    @Query("SELECT COUNT(*) FROM users WHERE lastLoginAt >= :startDate AND lastLoginAt < :endDate")
    suspend fun getUserLoginCount(startDate: Date, endDate: Date): Int
}