package com.example.astucianaval.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import com.example.astucianaval.ui.screens.NavRoutes
import android.R.style

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Astucia Naval") })
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
                Text(
                    text = "Bienvenido al menú principal",
                    style = MaterialTheme.typography.titleLarge
                )


                Button(onClick = { navController.navigate(NavRoutes.Tablero.route) }) {
                    Text("Ir al Tablero")
                }

                Button(onClick = { navController.navigate(NavRoutes.Ajustes.route) }) {
                    Text("Ajustes")
                }
            }
        }
    }
}
