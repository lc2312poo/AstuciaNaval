package com.example.astucianaval.ui.screens.ajustes

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.astucianaval.LanguagePreferences
import com.example.astucianaval.LocaleHelper
import com.example.astucianaval.R
import com.example.astucianaval.ui.screens.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(navController: NavController) {

    val context = LocalContext.current
    val activity = context as Activity

    val spanishLabel = stringResource(R.string.espanol)
    val englishLabel = stringResource(R.string.ingles)

    var idiomaSeleccionado by remember {
        mutableStateOf(
            if (LanguagePreferences.getLanguage(context) == "en") englishLabel else spanishLabel
        )
    }

    var nombreUsuario by remember { mutableStateOf("Jugador 1") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.profile_title), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D47A1),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF001F3F), Color(0xFF003F7F))
                    )
                ),
            contentAlignment = Alignment.TopCenter
        ) {

            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2))
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.history_title),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = stringResource(R.string.match_1), color = Color.White)
                            Text(text = stringResource(R.string.match_2), color = Color.White)
                            Text(text = stringResource(R.string.match_3), color = Color.White)
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0))
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.change_username_title),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = nombreUsuario,
                                onValueChange = { nombreUsuario = it },
                                label = { Text(stringResource(R.string.new_name_label)) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = {
                            navController.navigate(NavRoutes.Login.route) {
                                popUpTo(NavRoutes.Home.route) { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                    ) {
                        Text(text = stringResource(R.string.change_account), color = Color.White)
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0))
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.language_title),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            IdiomaDropdownMenu(
                                idiomaActual = idiomaSeleccionado,
                                opciones = listOf(spanishLabel, englishLabel),
                                onSelect = { nuevoIdioma ->

                                    idiomaSeleccionado = nuevoIdioma

                                    val langCode = when (nuevoIdioma) {
                                        englishLabel -> "en"
                                        else -> "es"
                                    }

                                    LanguagePreferences.saveLanguage(context, langCode)
                                    LocaleHelper.setLocale(context, langCode)
                                    activity.recreate()
                                }
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = { navController.navigate(NavRoutes.Home.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
                    ) {
                        Text(text = stringResource(R.string.back_home), color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun IdiomaDropdownMenu(
    idiomaActual: String,
    opciones: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text(text = idiomaActual, color = Color.White)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { idioma ->
                DropdownMenuItem(
                    text = { Text(text = idioma) },
                    onClick = {
                        onSelect(idioma)
                        expanded = false
                    }
                )
            }
        }
    }
}
