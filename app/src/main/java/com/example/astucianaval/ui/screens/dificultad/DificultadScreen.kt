package com.example.astucianaval.ui.screens.dificultad

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.astucianaval.ui.screens.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DificultadScreen(navController: NavController) {

    val infiniteTransition = rememberInfiniteTransition(label = "waveAnim")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveOffset"
    )


    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Selecciona la dificultad ⚙️",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(NavRoutes.Home.route) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver al menú",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D47A1)
                )
            )
        },
        containerColor = Color(0xFF001F3F)
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                val path = Path().apply {
                    moveTo(0f, height * 0.8f)
                    for (x in 0..width.toInt() step 20) {
                        val y = (height * 0.8f + kotlin.math.sin((x + waveOffset) * 0.03f) * 20)
                        lineTo(x.toFloat(), y.toFloat())
                    }
                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }

                drawPath(path, Color(0xFF1565C0))
                drawPath(path, Color(0xFF1E88E5).copy(alpha = 0.4f))
            }


            AnimatedVisibility(
                visible = visible,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Elige tu nivel de desafío:",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))


                    Button(
                        onClick = { navController.navigate(NavRoutes.ColocarBarcos.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .width(230.dp)
                            .height(55.dp)
                    ) {
                        Text("🌊 Fácil", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    Button(
                        onClick = { navController.navigate(NavRoutes.ColocarBarcos.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .width(230.dp)
                            .height(55.dp)
                    ) {
                        Text("⚓ Medio", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    Button(
                        onClick = { navController.navigate(NavRoutes.ColocarBarcos.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .width(230.dp)
                            .height(55.dp)
                    ) {
                        Text("💣 Experto", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }
        }
    }
}
