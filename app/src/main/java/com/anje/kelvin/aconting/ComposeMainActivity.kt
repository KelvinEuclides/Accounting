package com.anje.kelvin.aconting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.anje.kelvin.aconting.data.preferences.UserPreferences
import com.anje.kelvin.aconting.presentation.navigation.AccountingNavigation
import com.anje.kelvin.aconting.ui.localization.ProvideStrings
import com.anje.kelvin.aconting.ui.theme.AccountingTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ComposeMainActivity : ComponentActivity() {
    
    @Inject
    lateinit var userPreferences: UserPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkMode by userPreferences.isDarkMode.collectAsState()
            val language by userPreferences.language.collectAsState()
            
            AccountingTheme(darkTheme = isDarkMode) {
                ProvideStrings(language = language) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AccountingNavigation()
                    }
                }
            }
        }
    }
}