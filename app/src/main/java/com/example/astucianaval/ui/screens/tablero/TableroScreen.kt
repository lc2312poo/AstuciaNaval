package com.example.astucianaval.ui.screens.tablero

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import com.example.astucianaval.ui.screens.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableroScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tablero de juego") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text("Aquí va el tablero :D")

                Button(onClick = { navController.navigate(NavRoutes.Home.route) }) {
                    Text("Volver al Inicio")
                }
            }
        }
    }
}
