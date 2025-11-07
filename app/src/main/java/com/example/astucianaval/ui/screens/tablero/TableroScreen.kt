package com.example.astucianaval.ui.screens.tablero

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TableroScreen(
    navController: NavHostController,
    playerName: String = "Jugador 1"
) {
    val gridSize = 8
    val totalCells = gridSize * gridSize
    val cells = remember { mutableStateListOf<Boolean>().apply { repeat(totalCells) { add(false) } } }
    val letras = ('A'..'H').map { it.toString() }
    val numeros = (1..8).map { it.toString() }
    val aciertos = remember { mutableStateOf(0) }
    val fallos = remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF001F3F), Color(0xFF003F7F))
                )
            )
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "🌊 Astucia Naval 🌊",
                fontSize = 26.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Comandante: $playerName",
                fontSize = 18.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(20.dp))


            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                val tableroSize = maxHeight * 0.8f

                Box(
                    modifier = Modifier
                        .size(tableroSize)
                        .background(Color(0xFFB0BEC5), RoundedCornerShape(12.dp))
                        .border(6.dp, Color.DarkGray, RoundedCornerShape(12.dp))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .width(tableroSize * 0.9f)
                                .padding(bottom = 4.dp)
                        ) {
                            letras.forEach { letra ->
                                Text(
                                    text = letra,
                                    color = Color(0xFF263238),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }


                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column(
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.height(tableroSize * 0.9f)
                            ) {
                                numeros.forEach { num ->
                                    Text(
                                        text = num,
                                        color = Color(0xFF263238),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(6.dp))


                            Box(
                                modifier = Modifier
                                    .size(tableroSize * 0.9f)
                                    .background(Color(0xFF90CAF9), RoundedCornerShape(8.dp))
                                    .border(2.dp, Color(0xFF1565C0), RoundedCornerShape(8.dp))
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(gridSize),
                                    verticalArrangement = Arrangement.spacedBy(2.dp),
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(totalCells) { index ->
                                        val isSelected = cells[index]
                                        Box(
                                            modifier = Modifier
                                                .aspectRatio(1f)
                                                .background(
                                                    if (isSelected) Color(0xFF42A5F5)
                                                    else Color(0xFF1976D2),
                                                    shape = RoundedCornerShape(3.dp)
                                                )
                                                .border(
                                                    1.dp,
                                                    Color.White.copy(alpha = 0.4f),
                                                    RoundedCornerShape(3.dp)
                                                )
                                                .clickable {
                                                    cells[index] = !cells[index]
                                                    if (cells[index]) aciertos.value++
                                                    else fallos.value++
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isSelected) Text("💥", fontSize = 14.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 8.dp, start = 32.dp, end = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EstadoBox(label = "Aciertos", value = aciertos.value, color = Color(0xFF00E676))
            EstadoBox(label = "Fallos", value = fallos.value, color = Color(0xFFFF5252))
        }


        IconButton(onClick = { navController.navigate("pausa") }) {
            Icon(Icons.Filled.Pause, contentDescription = "Pausar")
        }
            Icon(
                imageVector = Icons.Default.Pause,
                contentDescription = "Pausa",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }


@Composable
private fun EstadoBox(label: String, value: Int, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .padding(12.dp)
            .width(120.dp)
    ) {
        Text(text = label, color = Color.White, fontWeight = FontWeight.SemiBold)
        Text(text = value.toString(), color = color, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}
