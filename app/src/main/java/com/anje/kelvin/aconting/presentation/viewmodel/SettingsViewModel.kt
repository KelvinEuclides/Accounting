package com.anje.kelvin.aconting.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.anje.kelvin.aconting.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val userPreferences: UserPreferences
) : ViewModel()