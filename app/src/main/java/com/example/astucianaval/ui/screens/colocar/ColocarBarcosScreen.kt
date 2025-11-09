package com.example.astucianaval.ui.screens.colocar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.astucianaval.ui.screens.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColocarBarcosScreen(navController: NavController) {
    val gridSize = 8
    val totalCells = gridSize * gridSize
    val maxBarcos = 8
    val cells = remember { mutableStateListOf<Boolean>().apply { repeat(totalCells) { add(false) } } }
    var mensaje by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Coloca tus barcos 🚢") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "Selecciona las posiciones de tus barcos",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .width(320.dp)
                        .height(320.dp)
                        .background(Color(0xFF0D47A1), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(gridSize),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(totalCells) { index ->
                            val isSelected = cells[index]
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .background(
                                        if (isSelected) Color(0xFF42A5F5) else Color(0xFF1976D2),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .border(
                                        1.dp,
                                        Color.White.copy(alpha = 0.5f),
                                        RoundedCornerShape(4.dp)
                                    )
                                    .clickable {
                                        val seleccionados = cells.count { it }
                                        if (!isSelected && seleccionados >= maxBarcos) {
                                            mensaje = "⚠️ No puedes añadir más barcos"
                                        } else {
                                            cells[index] = !isSelected
                                            mensaje = ""
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) Text("🚢")
                            }
                        }
                    }
                }


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            val barcosSeleccionados = cells.mapIndexedNotNull { index, isSelected ->
                                if (isSelected) index else null
                            }

                            if (barcosSeleccionados.size < maxBarcos) {
                                mensaje = "⚠️ Aún te faltan posiciones por colocar"
                            } else {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "barcos",
                                    barcosSeleccionados
                                )
                                navController.navigate(NavRoutes.Tablero.route)
                            }
                        },
                        modifier = Modifier
                            .width(140.dp)
                            .height(60.dp)
                    ) {
                        Text("Seguir ➡️")
                    }

                    if (mensaje.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = mensaje,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(140.dp)
                        )
                    }
                }
            }
        }
    }
}
