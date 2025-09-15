package com.anje.kelvin.aconting.util

import java.util.regex.Pattern
import java.security.MessageDigest
import java.security.SecureRandom
import android.util.Base64

object ValidationUtils {

    // Phone number validation
    private val ANGOLA_PHONE_PATTERN = Pattern.compile("^(\\+244|244)?\\s?[9][0-9]{8}$")
    private val GENERAL_PHONE_PATTERN = Pattern.compile("^[+]?[1-9]\\d{1,14}$")

    // Email validation
    private val EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    )

    // Name validation
    private val NAME_PATTERN = Pattern.compile("^[a-zA-ZÀ-ÿ\\s]{2,50}$")

    // PIN validation
    private const val MIN_PIN_LENGTH = 4
    private const val MAX_PIN_LENGTH = 8

    /**
     * Validates phone number format
     */
    fun isValidPhone(phone: String?): ValidationResult {
        if (phone.isNullOrBlank()) {
            return ValidationResult(false, "Phone number is required")
        }

        val cleanPhone = phone.trim().replace("\\s+".toRegex(), "")
        
        return when {
            cleanPhone.length < 9 -> ValidationResult(false, "Phone number is too short")
            cleanPhone.length > 15 -> ValidationResult(false, "Phone number is too long")
            !ANGOLA_PHONE_PATTERN.matcher(cleanPhone).matches() && 
            !GENERAL_PHONE_PATTERN.matcher(cleanPhone).matches() -> 
                ValidationResult(false, "Invalid phone number format")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validates email format (optional field)
     */
    fun isValidEmail(email: String?): ValidationResult {
        if (email.isNullOrBlank()) {
            return ValidationResult(true) // Email is optional
        }

        val trimmedEmail = email.trim()
        
        return when {
            trimmedEmail.length > 254 -> ValidationResult(false, "Email address is too long")
            !EMAIL_PATTERN.matcher(trimmedEmail).matches() -> 
                ValidationResult(false, "Invalid email format")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validates name format
     */
    fun isValidName(name: String?): ValidationResult {
        if (name.isNullOrBlank()) {
            return ValidationResult(false, "Name is required")
        }

        val trimmedName = name.trim()

        return when {
            trimmedName.length < 2 -> ValidationResult(false, "Name must be at least 2 characters")
            trimmedName.length > 50 -> ValidationResult(false, "Name must be less than 50 characters")
            !NAME_PATTERN.matcher(trimmedName).matches() -> 
                ValidationResult(false, "Name contains invalid characters")
            trimmedName.split("\\s+".toRegex()).size < 2 -> 
                ValidationResult(false, "Please enter your full name")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validates PIN format and strength
     */
    fun isValidPin(pin: String?): ValidationResult {
        if (pin.isNullOrBlank()) {
            return ValidationResult(false, "PIN is required")
        }

        return when {
            pin.length < MIN_PIN_LENGTH -> 
                ValidationResult(false, "PIN must be at least $MIN_PIN_LENGTH digits")
            pin.length > MAX_PIN_LENGTH -> 
                ValidationResult(false, "PIN must be less than $MAX_PIN_LENGTH digits")
            !pin.all { it.isDigit() } -> 
                ValidationResult(false, "PIN must contain only digits")
            isWeakPin(pin) -> 
                ValidationResult(false, "PIN is too weak. Avoid simple patterns like 1234 or 1111")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validates PIN confirmation
     */
    fun isValidPinConfirmation(pin: String?, confirmPin: String?): ValidationResult {
        if (confirmPin.isNullOrBlank()) {
            return ValidationResult(false, "PIN confirmation is required")
        }

        return if (pin == confirmPin) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "PINs do not match")
        }
    }

    /**
     * Checks if PIN is weak (common patterns)
     */
    private fun isWeakPin(pin: String): Boolean {
        // Check for repeated digits
        if (pin.all { it == pin[0] }) return true

        // Check for sequential patterns
        val sequential = listOf("0123", "1234", "2345", "3456", "4567", "5678", "6789", "9876", "8765", "7654", "6543", "5432", "4321", "3210")
        if (sequential.any { pin.contains(it) }) return true

        // Check for common weak PINs
        val weakPins = listOf("0000", "1111", "2222", "3333", "4444", "5555", "6666", "7777", "8888", "9999", "1234", "4321", "0123", "9876")
        return weakPins.contains(pin)
    }

    /**
     * Validates complete registration form
     */
    fun validateRegistration(
        name: String?,
        phone: String?,
        email: String?,
        pin: String?,
        confirmPin: String?
    ): RegistrationValidationResult {
        val nameValidation = isValidName(name)
        val phoneValidation = isValidPhone(phone)
        val emailValidation = isValidEmail(email)
        val pinValidation = isValidPin(pin)
        val confirmPinValidation = isValidPinConfirmation(pin, confirmPin)

        val isValid = nameValidation.isValid && 
                     phoneValidation.isValid && 
                     emailValidation.isValid && 
                     pinValidation.isValid && 
                     confirmPinValidation.isValid

        return RegistrationValidationResult(
            isValid = isValid,
            nameError = nameValidation.errorMessage,
            phoneError = phoneValidation.errorMessage,
            emailError = emailValidation.errorMessage,
            pinError = pinValidation.errorMessage,
            confirmPinError = confirmPinValidation.errorMessage
        )
    }

    /**
     * Validates login form
     */
    fun validateLogin(phone: String?, pin: String?): LoginValidationResult {
        val phoneValidation = isValidPhone(phone)
        val pinValidation = if (pin.isNullOrBlank()) {
            ValidationResult(false, "PIN is required")
        } else {
            ValidationResult(true)
        }

        return LoginValidationResult(
            isValid = phoneValidation.isValid && pinValidation.isValid,
            phoneError = phoneValidation.errorMessage,
            pinError = pinValidation.errorMessage
        )
    }

    /**
     * Normalizes phone number for storage
     */
    fun normalizePhoneNumber(phone: String): String {
        val cleaned = phone.trim().replace("\\s+".toRegex(), "")
        return when {
            cleaned.startsWith("+244") -> cleaned
            cleaned.startsWith("244") -> "+$cleaned"
            cleaned.startsWith("9") && cleaned.length == 9 -> "+244$cleaned"
            else -> cleaned
        }
    }

    /**
     * Normalizes email for storage
     */
    fun normalizeEmail(email: String?): String? {
        return email?.trim()?.lowercase()
    }

    /**
     * Normalizes name for storage
     */
    fun normalizeName(name: String): String {
        return name.trim().split("\\s+".toRegex()).joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }
}

/**
 * Security utilities for PIN hashing
 */
object SecurityUtils {

    private const val HASH_ALGORITHM = "SHA-256"
    private const val SALT_LENGTH = 32

    /**
     * Generates a random salt
     */
    fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(SALT_LENGTH)
        random.nextBytes(salt)
        return Base64.encodeToString(salt, Base64.NO_WRAP)
    }

    /**
     * Hashes PIN with salt
     */
    fun hashPin(pin: String, salt: String): String {
        val digest = MessageDigest.getInstance(HASH_ALGORITHM)
        val saltBytes = Base64.decode(salt, Base64.NO_WRAP)
        
        digest.update(saltBytes)
        val hashedBytes = digest.digest(pin.toByteArray(Charsets.UTF_8))
        
        return Base64.encodeToString(hashedBytes, Base64.NO_WRAP)
    }

    /**
     * Verifies PIN against stored hash
     */
    fun verifyPin(pin: String, storedHash: String, salt: String): Boolean {
        val computedHash = hashPin(pin, salt)
        return computedHash == storedHash
    }
}

// Data classes for validation results
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

data class RegistrationValidationResult(
    val isValid: Boolean,
    val nameError: String? = null,
    val phoneError: String? = null,
    val emailError: String? = null,
    val pinError: String? = null,
    val confirmPinError: String? = null
)

data class LoginValidationResult(
    val isValid: Boolean,
    val phoneError: String? = null,
    val pinError: String? = null
)