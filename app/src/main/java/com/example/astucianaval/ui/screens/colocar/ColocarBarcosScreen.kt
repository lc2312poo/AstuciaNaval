package com.example.astucianaval.ui.screens.colocarbarcos

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.astucianaval.ui.screens.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColocarBarcosScreen(navController: NavController) {
    val gridSize = 8
    val totalCells = gridSize * gridSize
    val cells = remember { mutableStateListOf<Boolean>().apply { repeat(totalCells) { add(false) } } }

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
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridSize),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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
                            .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .clickable {
                                cells[index] = !cells[index]
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) Text("🚢")
                    }
                }
            }

            Button(
                onClick = {
                    val barcosSeleccionados = cells.mapIndexedNotNull { index, isSelected ->
                        if (isSelected) index else null
                    }

                    // Guardamos los datos antes de navegar
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "barcos",
                        barcosSeleccionados
                    )
                    navController.navigate(NavRoutes.Tablero.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Seguir ➡️")
            }
        }
    }
}
