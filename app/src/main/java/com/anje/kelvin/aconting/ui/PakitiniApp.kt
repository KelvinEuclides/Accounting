package com.anje.kelvin.aconting.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.anje.kelvin.aconting.ui.navigation.BottomNavItem
import com.anje.kelvin.aconting.ui.navigation.PakitiniBottomNavigation
import com.anje.kelvin.aconting.ui.navigation.PakitiniNavHost
import com.anje.kelvin.aconting.ui.navigation.Screen
import com.anje.kelvin.aconting.ui.theme.PakitiniTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PakitiniApp() {
    val navController = rememberNavController()
    
    // Track current route for showing/hiding bottom navigation
    var currentRoute by remember { mutableStateOf(BottomNavItem.Home.name) }
    
    // Listen for navigation changes
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            currentRoute = backStackEntry.destination.route ?: BottomNavItem.Home.name
        }
    }
    
    // Check if the current screen should show bottom navigation
    val shouldShowBottomNav = remember(currentRoute) {
        when (currentRoute) {
            BottomNavItem.Home.name,
            BottomNavItem.Account.name,
            BottomNavItem.Settings.name -> true
            else -> false
        }
    }
    
    PakitiniTheme {
        Scaffold(
            bottomBar = {
                if (shouldShowBottomNav) {
                    PakitiniBottomNavigation(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            PakitiniNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
