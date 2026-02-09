package org.example.geoblinker.presentation.features.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.example.geoblinker.core.session.SessionManager
import org.example.geoblinker.presentation.features.ListScreen
import org.example.geoblinker.presentation.features.NotificationsScreen
import org.example.geoblinker.presentation.features.auth.ConfirmationCodeScreen
import org.example.geoblinker.presentation.features.auth.PhoneScreen
import org.example.geoblinker.presentation.features.main_screen.MainScreen
import org.example.geoblinker.presentation.features.map_screen.MapScreen
import org.koin.compose.koinInject

/**
 * Навигация приложения
 * Start destination определяется по наличию сессии
 */
@Composable
fun Navigation(
    navController: NavHostController
) {
    val sessionManager: SessionManager = koinInject()
    val startDestination = if (sessionManager.isAuthenticated()) MainScreen else PhoneScreen
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ============================================
        // AUTH FLOW
        // ============================================
        
        composable<PhoneScreen> {
            PhoneScreen(
                onNavigateToCode = {
                    navController.navigate(ConfirmationCodeScreen)
                }
            )
        }
        
        composable<ConfirmationCodeScreen> {
            ConfirmationCodeScreen(
                onNavigateToMain = {
                    navController.navigate(MainScreen) {
                        popUpTo(PhoneScreen) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // ============================================
        // MAIN FLOW
        // ============================================
        
        composable<MainScreen> {
            MainScreen(navController = navController)
        }
        
        composable<MapScreen> {
            MapScreen()
        }
        
        composable<ListScreen> {
            ListScreen()
        }
        
        composable<NotificationsScreen> {
            NotificationsScreen()
        }
        
        // Profile, Settings и остальные экраны будут добавлены в следующих этапах
    }
}
