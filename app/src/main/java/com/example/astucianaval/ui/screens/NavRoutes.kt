package com.example.astucianaval.ui.screens

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Ajustes : NavRoutes("ajustes")
    object Tablero : NavRoutes("tablero")
}
