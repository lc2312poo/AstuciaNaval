package com.example.astucianaval.ui.screens.perder

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.astucianaval.R
import com.example.astucianaval.ui.screens.NavRoutes
import com.example.astucianaval.viewmodel.HistorialViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun PerderScreen(
    navController: NavController,
    historialViewModel: HistorialViewModel
) {
    // The ViewModel's internal lock will prevent duplicate submissions.
    LaunchedEffect(Unit) {
        historialViewModel.addGameRecord("Derrota")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {

        TormentaAnimada()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.lose_message),
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                navController.navigate(NavRoutes.Home.route) {
                    popUpTo(NavRoutes.Home.route) { inclusive = true }
                }
            }) {
                Text(stringResource(R.string.back_to_home))
            }
        }
    }
}

@Composable
fun TormentaAnimada() {
    val lluvia = remember { List(150) { Random.nextFloat() to Random.nextFloat() } }
    val infiniteTransition = rememberInfiniteTransition(label = "lluvia")

    val desplazamiento by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "desplazamiento"
    )

    var relampagoVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(Random.nextLong(2000, 5000))
            relampagoVisible = true
            delay(200)
            relampagoVisible = false
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val ancho = size.width
        val alto = size.height

        lluvia.forEach { (xFactor, yFactor) ->
            val x = xFactor * ancho
            val y = (yFactor + desplazamiento) % 1f * alto
            drawLine(
                color = Color.Cyan.copy(alpha = 0.5f),
                start = androidx.compose.ui.geometry.Offset(x, y),
                end = androidx.compose.ui.geometry.Offset(x, y + 15f),
                strokeWidth = 2f
            )
        }

        if (relampagoVisible) {
            drawRect(Color.White.copy(alpha = 0.7f))
        }
    }
}
