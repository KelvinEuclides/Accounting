package com.anje.kelvin.aconting.util

object AppConstants {

    /**
     * IVA (Imposto sobre o Valor Acrescentado) tax rate for Mozambique.
     * Current rate: 17%
     */
    const val IVA_TAX_RATE = 0.17
    const val IVA_TAX_RATE_PERCENTAGE = 17
    const val MIN_PIN_LENGTH = 4
    const val MAX_PIN_LENGTH = 8
    const val HASH_ALGORITHM = "SHA-256"
    const val SALT_LENGTH = 32
    const val SESSION_DURATION_HOURS = 24
    const val MAX_LOGIN_ATTEMPTS = 5
    const val LOCKOUT_DURATION_MINUTES = 30
    const val ID_RANDOM_MIN = 1000
    const val ID_RANDOM_MAX = 9999
    const val ID_INCREMENT_MODULO = 1000
    const val ID_TIMESTAMP_DIVISOR = 1000
    const val SUCCESS_MESSAGE_DELAY = 3000L
    const val MILLISECONDS_IN_HOUR = 60 * 60 * 1000L

    const val MILLISECONDS_IN_MINUTE = 60 * 1000L
    const val MILLISECONDS_IN_DAY = 24 * MILLISECONDS_IN_HOUR
    const val MILLISECONDS_IN_WEEK = 7 * MILLISECONDS_IN_DAY
    object PreferenceKeys {
        const val DARK_MODE = "dark_mode"
        const val LANGUAGE = "language"
        const val CURRENCY = "currency"
        const val NOTIFICATIONS = "notifications"
        const val AUTO_BACKUP = "auto_backup"
        const val LOW_STOCK_ALERT = "low_stock_alert"
        const val STOCK_THRESHOLD = "stock_threshold"
        const val DAILY_REPORTS = "daily_reports"

        const val SESSION_TOKEN = "session_token"
        const val SESSION_EXPIRY = "session_expiry"
    }
    
    /**
     * Default values for various application settings
     */
    object Defaults {
        const val STOCK_THRESHOLD = 10
        const val CURRENCY = "MZN"
        const val LANGUAGE = "pt"
        const val QUANTITY = 1
    }
    
    // ========================================================================================
    // PAYMENT METHOD CONSTANTS
    // ========================================================================================
    
    /**
     * Available payment methods in the application
     */
    object PaymentMethods {
        const val CASH = "Cash"
        const val CARD = "Card"
        const val MOBILE_MONEY = "Mobile Money"
        const val BANK_TRANSFER = "Bank Transfer"
        const val CREDIT = "Credit"
        
        /**
         * Default payment method for new transactions
         */
        const val DEFAULT = CASH
        
        /**
         * List of all available payment methods for UI selection
         */
        val ALL = listOf(CASH, CARD, MOBILE_MONEY, BANK_TRANSFER, CREDIT)
        
        /**
         * Payment methods that require immediate settlement
         */
        val IMMEDIATE_SETTLEMENT = listOf(CASH, CARD, MOBILE_MONEY, BANK_TRANSFER)
        
        /**
         * Payment methods that allow deferred payment
         */
        val DEFERRED_PAYMENT = listOf(CREDIT)
    }
    
    // ========================================================================================
    // WEAK PIN PATTERNS (for validation)
    // ========================================================================================
    
    /**
     * Common weak PIN patterns that should be rejected
     */
    val WEAK_PINS = listOf(
        "0000", "1111", "2222", "3333", "4444", "5555", "6666", "7777", "8888", "9999",
        "1234", "4321", "0123", "9876"
    )
}