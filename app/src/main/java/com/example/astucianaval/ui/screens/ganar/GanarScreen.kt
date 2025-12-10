package com.example.astucianaval.ui.screens.ganar

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.astucianaval.R
import com.example.astucianaval.ui.screens.NavRoutes
import com.example.astucianaval.viewmodel.HistorialViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun GanarScreen(
    navController: NavController,
    historialViewModel: HistorialViewModel
) {
    val context = LocalContext.current

    // The ViewModel's internal lock will prevent duplicate submissions.
    LaunchedEffect(Unit) {
        historialViewModel.addGameRecord("Victoria")
    }

    // Listens for achievement toasts.
    LaunchedEffect(key1 = historialViewModel.achievementUnlocked) {
        historialViewModel.achievementUnlocked.collectLatest { achievement ->
            Toast.makeText(context, "¡Logro desbloqueado: $achievement!", Toast.LENGTH_LONG).show()
        }
    }

    val winTitle = stringResource(id = R.string.win_title)
    val backToHome = stringResource(id = R.string.back_to_home)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3F51B5)),
        contentAlignment = Alignment.Center
    ) {
        ConfettiAnimation(confettiCount = 120)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = winTitle,
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
                Text(backToHome)
            }
        }
    }
}

@Composable
fun ConfettiAnimation(confettiCount: Int = 120) {

    val confetti = remember {
        List(confettiCount) {
            ConfettiPiece(
                xStartFraction = Random.nextFloat(),
                startOffsetFraction = Random.nextFloat() * -0.8f,
                duration = Random.nextInt(2500, 6000),
                sizePx = Random.nextInt(4, 14).toFloat(),
                driftFraction = Random.nextFloat() * 0.08f + 0.01f,
                phase = Random.nextFloat() * 2f * PI.toFloat(),
                color = listOf(
                    Color(0xFFE53935), Color(0xFF1E88E5), Color(0xFF43A047),
                    Color(0xFFFDD835), Color(0xFF8E24AA), Color(0xFF00ACC1)
                ).random()
            )
        }
    }

    val transition = rememberInfiniteTransition()

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

            val xBase = piece.xStartFraction * w
            val xDrift = sin(progress * 2f * PI.toFloat() + piece.phase) * piece.driftFraction * w
            val x = (xBase + xDrift).coerceIn(0f, w)

            val yStart = piece.startOffsetFraction * h
            val y = yStart + progress * (h + h * 0.3f)

            drawCircle(
                color = piece.color,
                radius = piece.sizePx,
                center = Offset(x, y)
            )
        }
    }
}

data class ConfettiPiece(
    val xStartFraction: Float = 0.5f,
    val startOffsetFraction: Float = -0.5f,
    val duration: Int = 3000,
    val sizePx: Float = 6f,
    val driftFraction: Float = 0.03f,
    val phase: Float = 0f,
    val color: Color = Color.Red
)
