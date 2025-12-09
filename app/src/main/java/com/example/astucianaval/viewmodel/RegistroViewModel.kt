package com.example.astucianaval.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astucianaval.di.FirebaseModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegistroViewModel : ViewModel() {

    private val auth = FirebaseModule.firebaseAuth
    private val firestore = FirebaseModule.firestore

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid
                if (userId != null) {
                    val userProfile = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "winStreak" to 0,
                        "achievements" to emptyList<String>()
                    )
                    firestore.collection("users").document(userId).set(userProfile).await()
                }
                _registrationState.value = RegistrationState.Success
            } catch (e: Exception) {
                _registrationState.value = RegistrationState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

sealed class RegistrationState {
    object Idle : RegistrationState()
    object Loading : RegistrationState()
    object Success : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}