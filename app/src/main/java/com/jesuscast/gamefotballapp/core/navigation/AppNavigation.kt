package com.jesuscast.gamefotballapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jesuscast.gamefotballapp.core.session.SessionManager
import com.jesuscast.gamefotballapp.features.auth.presentation.LoginScreen
import com.jesuscast.gamefotballapp.features.chat.presentation.RetaDetailScreen
import com.jesuscast.gamefotballapp.features.lobby.presentation.LobbyScreen

/** Sealed class defining the main routes. */
sealed class Route(val route: String) {
    object Login : Route("login")
    object Lobby : Route("lobby")
    object RetaDetail : Route("reta_detail/{retaId}/{zonaId}") {
        fun createRoute(retaId: String, zonaId: String) = "reta_detail/$retaId/$zonaId"
    }
}

@Composable
fun AppNavigation(sessionManager: SessionManager) {
    val isLoggedIn by sessionManager.isLoggedIn.collectAsState(initial = false)
    val navController = rememberNavController()

    val startDestination = if (isLoggedIn) Route.Lobby.route else Route.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Route.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Route.Lobby.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.Lobby.route) {
            LobbyScreen(
                onRetaClick = { retaId, zonaId ->
                    navController.navigate(Route.RetaDetail.createRoute(retaId, zonaId))
                }
            )
        }

        composable(
            route = Route.RetaDetail.route,
            arguments = listOf(
                navArgument("retaId") { type = NavType.StringType },
                navArgument("zonaId") { type = NavType.StringType }
            )
        ) {
            RetaDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
