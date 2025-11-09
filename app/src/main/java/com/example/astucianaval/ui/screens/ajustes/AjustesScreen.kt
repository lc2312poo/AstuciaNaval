package com.example.astucianaval.ui.screens.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.astucianaval.ui.screens.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(navController: NavController) {
    var nombreUsuario by remember { mutableStateOf("Jugador 1") }
    var idiomaSeleccionado by remember { mutableStateOf("Español") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("⚙️ Ajustes", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D47A1),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF001F3F), Color(0xFF003F7F))
                    )
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Historial visual
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2))
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                "📜 Historial de Partidas",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Partida 1: Victoria", color = Color.White)
                            Text("Partida 2: Derrota", color = Color.White)
                            Text("Partida 3: Victoria", color = Color.White)
                        }
                    }
                }

                // Cambiar nombre de usuario
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0))
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                "👤 Cambiar nombre de usuario",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = nombreUsuario,
                                onValueChange = { nombreUsuario = it },
                                label = { Text("Nuevo nombre") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Cambiar cuenta
                item {
                    Button(
                        onClick = {
                            // Solo navegación, sin lógica
                            navController.navigate(NavRoutes.Login.route) {
                                popUpTo(NavRoutes.Home.route) { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                    ) {
                        Text("Cambiar cuenta", color = Color.White)
                    }
                }

                // Selección de idioma
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0))
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("🌐 Idioma", color = Color.White, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            IdiomaDropdownMenu(
                                idiomaSeleccionado,
                                onSelect = { idiomaSeleccionado = it }
                            )
                        }
                    }
                }

                // Volver al inicio
                item {
                    Button(
                        onClick = { navController.navigate(NavRoutes.Home.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
                    ) {
                        Text("Volver al inicio", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun IdiomaDropdownMenu(
    idiomaActual: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val idiomas = listOf("Español", "Inglés", "Francés")

    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text(idiomaActual, color = Color.White)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            idiomas.forEach { idioma ->
                DropdownMenuItem(
                    text = { Text(idioma) },
                    onClick = {
                        onSelect(idioma)
                        expanded = false
                    }
                )
            }
        }
    }
}
