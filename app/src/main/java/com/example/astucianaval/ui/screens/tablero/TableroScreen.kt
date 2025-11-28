package com.example.astucianaval.ui.screens.tablero

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.astucianaval.ui.screens.NavRoutes
import com.example.astucianaval.viewmodel.TableroViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TableroScreen(navController: NavHostController, playerName: String = "Jugador 1") {
    val vm: TableroViewModel = viewModel()
    val scope = rememberCoroutineScope()

    val gridSize = 8
    val totalCells = gridSize * gridSize

    val playerShot = remember {
        mutableStateListOf<Boolean>().apply { repeat(totalCells) { add(false) } }
    }

    val playerShotHit = remember {
        mutableStateListOf<Boolean>().apply { repeat(totalCells) { add(false) } }
    }

    LaunchedEffect(Unit) {
        val savedFromPrev = navController.previousBackStackEntry?.savedStateHandle?.get<List<Int>>("barcos")
        val savedFromCurrent = navController.currentBackStackEntry?.savedStateHandle?.get<List<Int>>("barcos")
        val saved = savedFromPrev ?: savedFromCurrent
        if (saved != null && saved.isNotEmpty()) {
            vm.cargarBarcos(saved)
        }

        vm.generarBarcosEnemigo()

        vm.iniciarTemporizador {
            navController.navigate(NavRoutes.Perder.route)
        }
    }

    val tiempo by remember { derivedStateOf { vm.tiempoRestante.value } }
    val aciertos by remember { derivedStateOf { vm.aciertos.value } }
    val fallos by remember { derivedStateOf { vm.fallos.value } }

    val letras = ('A'..'H').map { it.toString() }
    val numeros = (1..8).map { it.toString() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF001F3F), Color(0xFF003F7F))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🌊 Astucia Naval 🌊",
                fontSize = 26.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Comandante: $playerName",
                fontSize = 18.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tiempo restante: ${tiempo}s",
                color = if (tiempo > 10) Color.Yellow else Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                TableroIndividualEnemigo(
                    titulo = "Tablero Enemigo",
                    gridSize = gridSize,
                    totalCells = totalCells,
                    enemyShipPositions = vm.barcosEnemigo,
                    playerShot = playerShot,
                    playerShotHit = playerShotHit,
                    letras = letras,
                    numeros = numeros
                ) { index ->
                    if (playerShot[index]) return@TableroIndividualEnemigo

                    val hit = vm.barcosEnemigo.contains(index)

                    playerShot[index] = true
                    playerShotHit[index] = hit

                    vm.disparoJugador(
                        index,
                        onWin = { navController.navigate(NavRoutes.Ganar.route) },
                        onLose = { navController.navigate(NavRoutes.Perder.route) }
                    )

                    scope.launch {
                        delay(350)
                        val perdiste = vm.disparoEnemigo()
                        if (perdiste) {
                            navController.navigate(NavRoutes.Perder.route)
                        }
                    }
                }

                TableroIndividualJugador(
                    titulo = "Tu tablero",
                    gridSize = gridSize,
                    totalCells = totalCells,
                    playerShipPositions = vm.barcosJugador,
                    enemyShots = vm.jugadorCeldas,
                    enemyHits = vm.jugadorImpactos,
                    letras = letras,
                    numeros = numeros
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 8.dp, start = 32.dp, end = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EstadoBox("Aciertos", aciertos, Color(0xFF00E676))
            EstadoBox("Fallos", fallos, Color(0xFFFF5252))
        }

        IconButton(
            onClick = { navController.navigate(NavRoutes.Pausa.route) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Pause,
                contentDescription = "Pausar",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


@Composable
fun TableroIndividualEnemigo(
    titulo: String,
    gridSize: Int,
    totalCells: Int,
    enemyShipPositions: MutableList<Int>,
    playerShot: SnapshotStateList<Boolean>,
    playerShotHit: SnapshotStateList<Boolean>,
    letras: List<String>,
    numeros: List<String>,
    onCeldaClick: (Int) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(0.48f),
        contentAlignment = Alignment.Center
    ) {
        val tableroSize = maxHeight * 0.95f

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = titulo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

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
                        modifier = Modifier.width(tableroSize * 0.9f).padding(bottom = 4.dp)
                    ) {
                        letras.forEach { Text(it, color = Color(0xFF263238), fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.height(tableroSize * 0.9f)
                        ) {
                            numeros.forEach { Text(it, color = Color(0xFF263238), fontWeight = FontWeight.Bold, fontSize = 13.sp) }
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
                                    val shot = playerShot.getOrNull(index) ?: false
                                    val hit = playerShotHit.getOrNull(index) ?: false

                                    val bg = when {
                                        shot && hit -> Color(0xFF42A5F5)
                                        shot && !hit -> Color(0xFF4FC3F7)
                                        else -> Color(0xFF1976D2)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .background(bg, RoundedCornerShape(3.dp))
                                            .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(3.dp))
                                            .clickable { if (!shot) onCeldaClick(index) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        when {
                                            shot && hit -> Text("💥", fontSize = 15.sp)
                                            shot && !hit -> Text("💧", fontSize = 15.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TableroIndividualJugador(
    titulo: String,
    gridSize: Int,
    totalCells: Int,
    playerShipPositions: MutableList<Int>,
    enemyShots: SnapshotStateList<Boolean>,
    enemyHits: SnapshotStateList<Boolean>,
    letras: List<String>,
    numeros: List<String>
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth(0.48f), contentAlignment = Alignment.Center) {
        val tableroSize = maxHeight * 0.95f

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = titulo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .size(tableroSize)
                    .background(Color(0xFFB0BEC5), RoundedCornerShape(12.dp))
                    .border(6.dp, Color.DarkGray, RoundedCornerShape(12.dp))
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.width(tableroSize * 0.9f)) {
                        letras.forEach { Text(it, color = Color(0xFF263238), fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.height(tableroSize * 0.9f)) {
                            numeros.forEach { Text(it, color = Color(0xFF263238), fontWeight = FontWeight.Bold, fontSize = 13.sp) }
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
                                    val shotByEnemy = enemyShots.getOrNull(index) ?: false
                                    val hitByEnemy = enemyHits.getOrNull(index) ?: false
                                    val hasShip = playerShipPositions.contains(index)

                                    val bg = when {
                                        hitByEnemy -> Color(0xFF42A5F5)
                                        shotByEnemy && !hitByEnemy -> Color(0xFF4FC3F7)
                                        hasShip -> Color(0xFF1976D2).copy(alpha = 0.85f)
                                        else -> Color(0xFF1976D2)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .background(bg, RoundedCornerShape(3.dp))
                                            .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(3.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        when {
                                            hitByEnemy -> Text("💥", fontSize = 15.sp)
                                            shotByEnemy && !hitByEnemy -> Text("💧", fontSize = 15.sp)
                                            hasShip -> Text("🚢")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EstadoBox(label: String, value: Int, color: Color) {
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
