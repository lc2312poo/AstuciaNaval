package com.example.astucianaval.ui.screens.colocar

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.astucianaval.R
import com.example.astucianaval.ui.screens.NavRoutes
import com.example.astucianaval.viewmodel.ColocarBarcosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColocarBarcosScreen(
    navController: NavController,
    viewModel: ColocarBarcosViewModel = viewModel()
) {

    val cells by viewModel.cells.collectAsState()
    val mensajeKey by viewModel.mensaje.collectAsState()

    val gridSize = viewModel.gridSize
    val totalCells = gridSize * gridSize

    val context = LocalContext.current

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val waveShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            tween(6000, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = ""
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.place_boats_title),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(NavRoutes.Dificultad.route) }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D47A1)
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val gradient = Brush.verticalGradient(
                        colors = listOf(Color(0xFF001F54), Color(0xFF0D47A1), Color(0xFF2196F3))
                    )
                    drawRect(gradient)

                    for (i in 0..10) {
                        val y = (i * 100 + waveShift % 2000) % size.height
                        drawLine(
                            color = Color.White.copy(alpha = 0.05f),
                            start = Offset(0f, y),
                            end = Offset(size.width, y + 30f),
                            strokeWidth = 6f
                        )
                    }
                }
                .padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Text(
                    text = stringResource(R.string.place_boats_instruction),
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .width(360.dp)
                            .height(360.dp)
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
                                            if (isSelected) Color(0xFF42A5F5)
                                            else Color(0xFF1976D2),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .border(
                                            1.dp,
                                            Color.White.copy(alpha = 0.5f),
                                            RoundedCornerShape(4.dp)
                                        )
                                        .clickable { viewModel.toggleCell(index) },
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
                                val barcos = viewModel.obtenerBarcos()

                                if (!viewModel.validarBarcos()) {
                                    viewModel.setMensaje("error_missing_positions")
                                } else {
                                    navController.currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("barcos", barcos)

                                    navController.navigate(NavRoutes.Tablero.route)
                                }
                            },
                            modifier = Modifier
                                .width(140.dp)
                                .height(60.dp)
                        ) {
                            Text(stringResource(R.string.next_button))
                        }

                        if (mensajeKey.isNotEmpty()) {

                            val mensajeTraducido = stringResource(
                                id = when (mensajeKey) {
                                    "error_missing_positions" -> R.string.error_missing_positions
                                    "error_max_barcos" -> R.string.error_max_barcos
                                    else -> R.string.empty_text
                                }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = mensajeTraducido,
                                color = Color.Red,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(160.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
