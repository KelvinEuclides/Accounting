package com.anje.kelvin.aconting.data.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "user_preferences", 
        Context.MODE_PRIVATE
    )
    
    private val _isDarkMode = MutableStateFlow(
        prefs.getBoolean(KEY_DARK_MODE, false)
    )
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()
    
    private val _language = MutableStateFlow(
        prefs.getString(KEY_LANGUAGE, "pt") ?: "pt"
    )
    val language: StateFlow<String> = _language.asStateFlow()
    
    private val _currency = MutableStateFlow(
        prefs.getString(KEY_CURRENCY, "MT") ?: "MT"
    )
    val currency: StateFlow<String> = _currency.asStateFlow()
    
    private val _notifications = MutableStateFlow(
        prefs.getBoolean(KEY_NOTIFICATIONS, true)
    )
    val notifications: StateFlow<Boolean> = _notifications.asStateFlow()
    
    private val _autoBackup = MutableStateFlow(
        prefs.getBoolean(KEY_AUTO_BACKUP, true)
    )
    val autoBackup: StateFlow<Boolean> = _autoBackup.asStateFlow()
    
    private val _lowStockAlert = MutableStateFlow(
        prefs.getBoolean(KEY_LOW_STOCK_ALERT, true)
    )
    val lowStockAlert: StateFlow<Boolean> = _lowStockAlert.asStateFlow()
    
    private val _stockThreshold = MutableStateFlow(
        prefs.getInt(KEY_STOCK_THRESHOLD, 10)
    )
    val stockThreshold: StateFlow<Int> = _stockThreshold.asStateFlow()
    
    private val _dailyReports = MutableStateFlow(
        prefs.getBoolean(KEY_DAILY_REPORTS, false)
    )
    val dailyReports: StateFlow<Boolean> = _dailyReports.asStateFlow()
    
    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
        _isDarkMode.value = enabled
    }
    
    fun setLanguage(languageCode: String) {
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
        _language.value = languageCode
    }
    
    fun setCurrency(currencyCode: String) {
        prefs.edit().putString(KEY_CURRENCY, currencyCode).apply()
        _currency.value = currencyCode
    }
    
    fun setNotifications(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply()
        _notifications.value = enabled
    }
    
    fun setAutoBackup(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_BACKUP, enabled).apply()
        _autoBackup.value = enabled
    }
    
    fun setLowStockAlert(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_LOW_STOCK_ALERT, enabled).apply()
        _lowStockAlert.value = enabled
    }
    
    fun setStockThreshold(threshold: Int) {
        prefs.edit().putInt(KEY_STOCK_THRESHOLD, threshold).apply()
        _stockThreshold.value = threshold
    }
    
    fun setDailyReports(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DAILY_REPORTS, enabled).apply()
        _dailyReports.value = enabled
    }
    
    companion object {
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_CURRENCY = "currency"
        private const val KEY_NOTIFICATIONS = "notifications"
        private const val KEY_AUTO_BACKUP = "auto_backup"
        private const val KEY_LOW_STOCK_ALERT = "low_stock_alert"
        private const val KEY_STOCK_THRESHOLD = "stock_threshold"
        private const val KEY_DAILY_REPORTS = "daily_reports"
    }
}