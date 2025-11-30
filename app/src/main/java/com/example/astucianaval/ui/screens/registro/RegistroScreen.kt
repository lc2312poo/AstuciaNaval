package com.example.astucianaval.ui.screens.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.astucianaval.R
import com.example.astucianaval.ui.screens.NavRoutes

@Composable
fun RegistroScreen(navController: NavHostController) {
    var nombre by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF001F3F), Color(0xFF003F7F)))
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(24.dp)
                .fillMaxWidth(0.9f)
                .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                .padding(vertical = 40.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.register_title),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text(stringResource(R.string.full_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text(stringResource(R.string.username)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text(stringResource(R.string.password)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmar,
                onValueChange = { confirmar = it },
                label = { Text(stringResource(R.string.confirm_password)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
            ) {
                Text(stringResource(R.string.create_account), color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = { navController.navigate(NavRoutes.Login.route) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    stringResource(R.string.back_to_login),
                    color = Color.White
                )
            }
        }
    }
}
