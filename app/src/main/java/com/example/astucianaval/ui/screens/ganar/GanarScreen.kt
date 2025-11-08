package com.example.astucianaval.ui.screens.ganar

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random
import kotlin.math.PI
import kotlin.math.sin


@Composable
fun GanarScreen(onVolverInicio: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3F51B5)),
        contentAlignment = Alignment.Center
    ) {
        ConfettiAnimation(confettiCount = 120)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "¡Felicidades, ganaste :D!",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = onVolverInicio) {
                Text("Volver al inicio")
            }
        }
    }
}

@Composable
fun ConfettiAnimation(confettiCount: Int = 120) {
    // Crear las partículas con parámetros relativos (xStartFraction entre 0..1)
    val confetti = remember {
        List(confettiCount) {
            ConfettiPiece(
                xStartFraction = Random.nextFloat(),        // 0..1, relativo al ancho del canvas
                startOffsetFraction = Random.nextFloat() * -0.8f, // inicia arriba (negativo)
                duration = Random.nextInt(2500, 6000),
                sizePx = Random.nextInt(4, 14).toFloat(),  // radio en px (aprox)
                driftFraction = Random.nextFloat() * 0.08f + 0.01f, // cuanto se mueve lateral relativo al ancho
                phase = Random.nextFloat() * 2f * PI.toFloat(),
                color = listOf(
                    Color(0xFFE53935), Color(0xFF1E88E5), Color(0xFF43A047),
                    Color(0xFFFDD835), Color(0xFF8E24AA), Color(0xFF00ACC1)
                ).random()
            )
        }
    }

    val transition = rememberInfiniteTransition()

    // Para cada partícula obtengo un progreso animado 0..1 con su propia duración y delay aleatorio
    val progresses = confetti.map { piece ->
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = piece.duration,
                    delayMillis = Random.nextInt(0, 1200),
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        confetti.forEachIndexed { i, piece ->
            val progress = progresses[i].value.coerceIn(0f, 1f)

            // X: posición base relativa al ancho + oscilación lateral dependiente del progreso
            val xBase = piece.xStartFraction * w
            val xDrift = sin(progress * 2f * PI.toFloat() + piece.phase) * piece.driftFraction * w
            val x = (xBase + xDrift).coerceIn(0f, w)

            // Y: empieza arriba (startOffsetFraction * h) y cae hasta h + extra
            val yStart = piece.startOffsetFraction * h
            val y = yStart + progress * (h + h * 0.3f) // cae un poco más allá para que salga de pantalla

            // Dibuja la partícula
            drawCircle(
                color = piece.color,
                radius = piece.sizePx,
                center = Offset(x, y)
            )
        }
    }
}

data class ConfettiPiece(
    val xStartFraction: Float = 0.5f,   // 0..1 relativo al ancho
    val startOffsetFraction: Float = -0.5f, // donde comienza verticalmente relativo al alto (negativo = arriba)
    val duration: Int = 3000,          // ms
    val sizePx: Float = 6f,            // radio en px
    val driftFraction: Float = 0.03f,  // cuanto se desplaza lateral relativo al ancho (ej 0.05 -> 5% del ancho)
    val phase: Float = 0f,             // fase para la oscilación
    val color: Color = Color.Red
)