package com.example.astucianaval

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.astucianaval.notifications.AlarmReceiver
import com.example.astucianaval.notifications.NotificationHelper
import com.example.astucianaval.ui.screens.NavRoutes
import com.example.astucianaval.ui.screens.achievements.AchievementsScreen
import com.example.astucianaval.ui.screens.ajustes.AjustesScreen
import com.example.astucianaval.ui.screens.colocar.ColocarBarcosScreen
import com.example.astucianaval.ui.screens.dificultad.DificultadScreen
import com.example.astucianaval.ui.screens.ganar.GanarScreen
import com.example.astucianaval.ui.screens.historial.HistorialScreen
import com.example.astucianaval.ui.screens.home.HomeScreen
import com.example.astucianaval.ui.screens.login.LoginScreen
import com.example.astucianaval.ui.screens.pausa.PausaScreen
import com.example.astucianaval.ui.screens.perder.PerderScreen
import com.example.astucianaval.ui.screens.registro.RegistroScreen
import com.example.astucianaval.ui.screens.tablero.TableroScreen
import com.example.astucianaval.ui.theme.AppTheme
import com.example.astucianaval.viewmodel.HistorialViewModel
import com.example.astucianaval.viewmodel.TableroViewModel

class MainActivity : ComponentActivity() {

    private val CHANNEL_ID = "astucia_channel"

    override fun attachBaseContext(newBase: Context?) {
        val lang = newBase?.let { LanguagePreferences.getLanguage(it) } ?: "es"
        val updatedContext = newBase?.let { LocaleHelper.setLocale(it, lang) }
        super.attachBaseContext(updatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationHelper.createChannel(this)
        }

        // Pedir permiso para notificaciones en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
        }

        programarNotificacion(
            triggerDelayMillis = 10_000L,
            requestCode = 100,
            message = "¡Es hora de jugar! Bienvenido de nuevo, capitán 😎🚢"
        )

        setContent {
            AppTheme {
                AppNavigation()
            }
        }
    }

    private fun programarNotificacion(
        triggerDelayMillis: Long = 10_000L,
        requestCode: Int = 0,
        message: String = "¡Es hora de jugar! Bienvenido de nuevo, capitán 😎🚢"
    ) {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("msg", message)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerTime = System.currentTimeMillis() + triggerDelayMillis

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }

    override fun onStop() {
        super.onStop()

        programarNotificacion(
            triggerDelayMillis = 5_000L,
            requestCode = 200,
            message = "Vuelve, no te vayas aún, capitán :("
        )
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Create a single, shared instance of the ViewModel for the entire navigation graph.
    val historialViewModel: HistorialViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Login.route
    ) {
        composable(NavRoutes.Login.route) {
            LoginScreen(navController)
        }
        composable(NavRoutes.Registro.route) {
            RegistroScreen(navController)
        }
        composable(NavRoutes.Home.route) {
            HomeScreen(navController, historialViewModel)
        }
        composable(NavRoutes.Ajustes.route) {
            AjustesScreen(navController)
        }
        composable(NavRoutes.Tablero.route) { backStackEntry ->
            val viewModel: TableroViewModel = viewModel(backStackEntry)
            TableroScreen(navController, viewModel)
        }
        composable(NavRoutes.Dificultad.route) {
            DificultadScreen(navController)
        }
        composable(NavRoutes.ColocarBarcos.route) {
            ColocarBarcosScreen(navController)
        }
        composable(NavRoutes.Pausa.route) {
            PausaScreen(navController)
        }
        composable(NavRoutes.Perder.route) {
            PerderScreen(
                navController = navController,
                historialViewModel = historialViewModel
            )
        }
        composable(NavRoutes.Ganar.route) {
            GanarScreen(
                navController = navController,
                historialViewModel = historialViewModel
            )
        }
        composable(NavRoutes.Historial.route) {
            HistorialScreen(navController)
        }
        composable(NavRoutes.Achievements.route) {
            AchievementsScreen(navController)
        }
    }
}
