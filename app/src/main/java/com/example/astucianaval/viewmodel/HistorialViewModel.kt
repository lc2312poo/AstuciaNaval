package com.example.astucianaval.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astucianaval.di.FirebaseModule
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class GameRecord(val result: String, val date: String)

class HistorialViewModel : ViewModel() {

    private val db = FirebaseModule.firestore
    private val auth = FirebaseModule.firebaseAuth

    private val _historyState = MutableStateFlow<List<GameRecord>>(emptyList())
    val historyState: StateFlow<List<GameRecord>> = _historyState

    private val achievements = mapOf(
        "FIRST_VICTORY" to "¡Primera Victoria!",
        "STREAK_3" to "¡En racha! (3 victorias)",
        "STREAK_5" to "¡Imparable! (5 victorias)"
    )

    fun fetchHistory() {
        val userId = auth.currentUser?.uid ?: return

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
                _historyState.value = records
            } catch (e: Exception) {
                Log.e("HistorialViewModel", "Error fetching history", e)
            }
        }
    }

    fun addGameRecord(result: String) {
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
                fetchHistory()
            } catch (e: Exception) {
                Log.e("HistorialViewModel", "Error adding game record", e)
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
            }
        } catch (e: Exception) {
            Log.e("HistorialViewModel", "Error updating player stats", e)
        }
    }
}
