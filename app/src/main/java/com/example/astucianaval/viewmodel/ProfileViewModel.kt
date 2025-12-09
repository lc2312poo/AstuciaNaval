package com.example.astucianaval.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astucianaval.di.FirebaseModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class PlayerProfile(
    val name: String = "",
    val winStreak: Int = 0,
    val achievements: List<String> = emptyList()
)

class ProfileViewModel : ViewModel() {

    private val db = FirebaseModule.firestore
    private val auth = FirebaseModule.firebaseAuth

    private val _profileState = MutableStateFlow(PlayerProfile())
    val profileState: StateFlow<PlayerProfile> = _profileState

    fun fetchUserProfile() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val doc = db.collection("users").document(userId).get().await()
                val name = doc.getString("name") ?: ""
                val winStreak = doc.getLong("winStreak")?.toInt() ?: 0
                val achievements = doc.get("achievements") as? List<String> ?: emptyList()
                _profileState.value = PlayerProfile(name, winStreak, achievements)
            } catch (e: Exception) {
                // Handle error, e.g., show a message
            }
        }
    }
}