package com.example.astucianaval

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.astucianaval.ui.screens.NavRoutes
import com.example.astucianaval.ui.screens.pausa.PausaScreen
import com.example.astucianaval.ui.screens.home.HomeScreen
import com.example.astucianaval.ui.screens.ajustes.AjustesScreen
import com.example.astucianaval.ui.screens.tablero.TableroScreen
import com.example.astucianaval.ui.screens.dificultad.DificultadScreen
import com.example.astucianaval.ui.screens.colocar.ColocarBarcosScreen
import com.example.astucianaval.ui.screens.ganar.GanarScreen
import com.example.astucianaval.ui.screens.perder.PerderScreen
import com.example.astucianaval.ui.theme.AppTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(navController)
        }
        composable(NavRoutes.Ajustes.route) {
            AjustesScreen(navController)
        }
        composable(NavRoutes.Tablero.route) {
            TableroScreen(navController)
        }
        composable(NavRoutes.Dificultad.route) {
            DificultadScreen(navController)
        }
        composable(NavRoutes.ColocarBarcos.route) {
            ColocarBarcosScreen(navController)
        }
        composable("pausa") {
            PausaScreen(navController)
        }
        composable(NavRoutes.Perder.route) {
            PerderScreen(
                onVolverInicio = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Ganar.route) {
            GanarScreen(
                onVolverInicio = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                    }
                }
            )
        }


    }
}
