package com.example.astucianaval.ui.screens.dificultad

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.astucianaval.ui.screens.NavRoutes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DificultadScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Selecciona la dificultad ⚙️") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(NavRoutes.Home.route) }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Volver al menú"
                        )
                    }
                }
            )
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
                    text = "Elige tu nivel de desafío:",
                    style = MaterialTheme.typography.titleLarge
                )

                Button(
                    onClick = { navController.navigate(NavRoutes.ColocarBarcos.route) },
                    modifier = Modifier.width(200.dp)
                ) {
                    Text("Fácil 🌊")
                }

                Button(
                    onClick = { navController.navigate(NavRoutes.ColocarBarcos.route) },
                    modifier = Modifier.width(200.dp)
                ) {
                    Text("Medio ⚓")
                }

                Button(
                    onClick = { navController.navigate(NavRoutes.ColocarBarcos.route) },
                    modifier = Modifier.width(200.dp)
                ) {
                    Text("Experto 💣")
                }
            }
        }
    }
}
