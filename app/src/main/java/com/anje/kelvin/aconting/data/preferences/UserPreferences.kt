package com.anje.kelvin.aconting.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.anje.kelvin.aconting.util.AppConstants
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
        prefs.getBoolean(AppConstants.PreferenceKeys.DARK_MODE, false)
    )
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()
    
    private val _language = MutableStateFlow(
        prefs.getString(AppConstants.PreferenceKeys.LANGUAGE, "pt") ?: "pt"
    )
    val language: StateFlow<String> = _language.asStateFlow()
    
    private val _currency = MutableStateFlow(
        prefs.getString(AppConstants.PreferenceKeys.CURRENCY, "MT") ?: "MT"
    )
    val currency: StateFlow<String> = _currency.asStateFlow()
    
    private val _notifications = MutableStateFlow(
        prefs.getBoolean(AppConstants.PreferenceKeys.NOTIFICATIONS, true)
    )
    val notifications: StateFlow<Boolean> = _notifications.asStateFlow()
    
    private val _autoBackup = MutableStateFlow(
        prefs.getBoolean(AppConstants.PreferenceKeys.AUTO_BACKUP, true)
    )
    val autoBackup: StateFlow<Boolean> = _autoBackup.asStateFlow()
    
    private val _lowStockAlert = MutableStateFlow(
        prefs.getBoolean(AppConstants.PreferenceKeys.LOW_STOCK_ALERT, true)
    )
    val lowStockAlert: StateFlow<Boolean> = _lowStockAlert.asStateFlow()
    
    private val _stockThreshold = MutableStateFlow(
        prefs.getInt(AppConstants.PreferenceKeys.STOCK_THRESHOLD, AppConstants.Defaults.STOCK_THRESHOLD)
    )
    val stockThreshold: StateFlow<Int> = _stockThreshold.asStateFlow()
    
    private val _dailyReports = MutableStateFlow(
        prefs.getBoolean(AppConstants.PreferenceKeys.DAILY_REPORTS, false)
    )
    val dailyReports: StateFlow<Boolean> = _dailyReports.asStateFlow()
    
    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(AppConstants.PreferenceKeys.DARK_MODE, enabled).apply()
        _isDarkMode.value = enabled
    }
    
    fun setLanguage(languageCode: String) {
        prefs.edit().putString(AppConstants.PreferenceKeys.LANGUAGE, languageCode).apply()
        _language.value = languageCode
    }
    
    fun setCurrency(currencyCode: String) {
        prefs.edit().putString(AppConstants.PreferenceKeys.CURRENCY, currencyCode).apply()
        _currency.value = currencyCode
    }
    
    fun setNotifications(enabled: Boolean) {
        prefs.edit().putBoolean(AppConstants.PreferenceKeys.NOTIFICATIONS, enabled).apply()
        _notifications.value = enabled
    }
    
    fun setAutoBackup(enabled: Boolean) {
        prefs.edit().putBoolean(AppConstants.PreferenceKeys.AUTO_BACKUP, enabled).apply()
        _autoBackup.value = enabled
    }
    
    fun setLowStockAlert(enabled: Boolean) {
        prefs.edit().putBoolean(AppConstants.PreferenceKeys.LOW_STOCK_ALERT, enabled).apply()
        _lowStockAlert.value = enabled
    }
    
    fun setStockThreshold(threshold: Int) {
        prefs.edit().putInt(AppConstants.PreferenceKeys.STOCK_THRESHOLD, threshold).apply()
        _stockThreshold.value = threshold
    }
    
    fun setDailyReports(enabled: Boolean) {
        prefs.edit().putBoolean(AppConstants.PreferenceKeys.DAILY_REPORTS, enabled).apply()
        _dailyReports.value = enabled
    }
    
    companion object {
        // Constants now moved to AppConstants.PreferenceKeys
    }
}