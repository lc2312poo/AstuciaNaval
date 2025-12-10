package com.example.astucianaval.ui.screens.historial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.astucianaval.R
import com.example.astucianaval.viewmodel.GameRecord
import com.example.astucianaval.viewmodel.HistorialViewModel
import com.example.astucianaval.viewmodel.HistoryUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    navController: NavHostController,
    viewModel: HistorialViewModel = viewModel()
) {
    val uiState by viewModel.historyState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchHistory()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.history_title), color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFF001F3F)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Brush.verticalGradient(listOf(Color(0xFF001F3F), Color(0xFF003F7F)))),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is HistoryUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is HistoryUiState.Success -> {
                    if (state.records.isEmpty()) {
                        Text("No hay historial de partidas.", color = Color.White, fontSize = 18.sp)
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(state.records) { record ->
                                GameHistoryItem(record)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
                is HistoryUiState.Error -> {
                    Text(state.message, color = Color.Red, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun GameHistoryItem(record: GameRecord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = record.result, color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = record.date, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}