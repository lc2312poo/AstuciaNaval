package com.example.astucianaval.ui.screens.pausa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.astucianaval.ui.screens.NavRoutes
import com.example.astucianaval.R
import androidx.compose.material3.ButtonDefaults

@Composable
fun PausaScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Text(
                text = stringResource(R.string.pause_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.width(200.dp)
            ) {
                Text(stringResource(R.string.pause_continue))
            }

            Button(
                onClick = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    stringResource(R.string.pause_back_menu),
                    color = Color.White
                )
            }
        }
    }
}
