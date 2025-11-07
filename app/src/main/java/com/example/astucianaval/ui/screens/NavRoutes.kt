package com.example.astucianaval.ui.screens

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Ajustes : NavRoutes("ajustes")
    object Dificultad : NavRoutes("dificultad")
    object ColocarBarcos : NavRoutes("colocarBarcos")
    object Conteo : NavRoutes("conteo")
    object Tablero : NavRoutes("tablero")
    object Pausa : NavRoutes("pausa")
    object Ganar : NavRoutes("ganar")
    object Perder : NavRoutes("perder")
}
