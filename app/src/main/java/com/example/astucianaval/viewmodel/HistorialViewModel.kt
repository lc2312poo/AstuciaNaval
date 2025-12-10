package com.example.astucianaval.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astucianaval.di.FirebaseModule
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class GameRecord(val result: String, val date: String)

sealed interface HistoryUiState {
    object Loading : HistoryUiState
    data class Success(val records: List<GameRecord>) : HistoryUiState
    data class Error(val message: String) : HistoryUiState
}

class HistorialViewModel : ViewModel() {

    private val db = FirebaseModule.firestore
    private val auth = FirebaseModule.firebaseAuth

    private val _historyState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val historyState: StateFlow<HistoryUiState> = _historyState

    private val _achievementUnlocked = Channel<String>()
    val achievementUnlocked = _achievementUnlocked.receiveAsFlow()

    // The master lock to prevent duplicate records
    private var isGameResultSubmitted = false

    private val achievements = mapOf(
        "FIRST_VICTORY" to "¡Primera Victoria!",
        "STREAK_3" to "¡En racha! (3 victorias)",
        "STREAK_5" to "¡Imparable! (5 victorias)"
    )

    fun resetGameResultSubmission() {
        isGameResultSubmitted = false
    }

    fun fetchHistory() {
        val userId = auth.currentUser?.uid ?: return
        _historyState.value = HistoryUiState.Loading
        viewModelScope.launch {
            try {
                val result = db.collection("historial")
                    .whereEqualTo("userId", userId)
                    .orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .await()

                val records = result.map {
                    GameRecord(
                        result = it.getString("result") ?: "",
                        date = it.getString("date") ?: ""
                    )
                }
                Log.d("HistorialViewModel", "Fetched ${records.size} records.")
                _historyState.value = HistoryUiState.Success(records)
            } catch (e: Exception) {
                Log.e("HistorialViewModel", "Error fetching history", e)
                _historyState.value = HistoryUiState.Error(e.message ?: "Error al cargar el historial")
            }
        }
    }

    fun addGameRecord(result: String) {
        if (isGameResultSubmitted) return // Master lock check
        isGameResultSubmitted = true // Activate the lock

        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val record = hashMapOf(
                    "userId" to userId,
                    "result" to result,
                    "date" to java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
                )
                db.collection("historial").add(record).await()
                updatePlayerStatsAndAchievements(userId, result)
                fetchHistory() // Refresh the history list
            } catch (e: Exception) {
                Log.e("HistorialViewModel", "Error adding game record", e)
                 // In case of error, we might want to allow retrying.
                 // For now, we keep it locked to prevent duplicates on navigation.
            }
        }
    }

    private suspend fun updatePlayerStatsAndAchievements(userId: String, result: String) {
        try {
            val userDocRef = db.collection("users").document(userId)
            val userSnapshot = userDocRef.get().await()
            val currentStreak = userSnapshot.getLong("winStreak")?.toInt() ?: 0
            val currentAchievements = userSnapshot.get("achievements") as? List<String> ?: emptyList()

            val newStreak = if (result == "Victoria") currentStreak + 1 else 0
            userDocRef.update("winStreak", newStreak).await()

            val newAchievements = mutableListOf<String>()
            if (result == "Victoria" && !currentAchievements.contains(achievements["FIRST_VICTORY"])) {
                newAchievements.add(achievements.getValue("FIRST_VICTORY"))
            }
            if (newStreak >= 3 && !currentAchievements.contains(achievements["STREAK_3"])) {
                newAchievements.add(achievements.getValue("STREAK_3"))
            }
            if (newStreak >= 5 && !currentAchievements.contains(achievements["STREAK_5"])) {
                newAchievements.add(achievements.getValue("STREAK_5"))
            }

            if (newAchievements.isNotEmpty()) {
                userDocRef.update("achievements", FieldValue.arrayUnion(*newAchievements.toTypedArray())).await()
                newAchievements.forEach { _achievementUnlocked.send(it) }
            }
        } catch (e: Exception) {
            Log.e("HistorialViewModel", "Error updating player stats", e)
        }
    }
}
