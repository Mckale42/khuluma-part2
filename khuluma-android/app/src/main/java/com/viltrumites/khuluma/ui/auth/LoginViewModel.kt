package com.viltrumites.khuluma.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viltrumites.khuluma.ServiceLocator
import com.viltrumites.khuluma.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val auth = ServiceLocator.auth

    private val _state = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val state = _state.asStateFlow()

    val alreadySignedIn: Boolean get() = auth.isSignedIn

    /** Exchange the Google ID token for a Firebase session (SSO). */
    fun signIn(idToken: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            try {
                auth.firebaseSignInWithGoogleToken(idToken)
                _state.value = UiState.Success(Unit)
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Sign-in failed", e)
                _state.value = UiState.Error(e.message ?: "Sign-in failed.")
            }
        }
    }

    fun fail(message: String) { _state.value = UiState.Error(message) }
}
