package com.viltrumites.khuluma.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viltrumites.khuluma.ServiceLocator
import com.viltrumites.khuluma.data.remote.Settings
import com.viltrumites.khuluma.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val repo = ServiceLocator.repository
    private val auth = ServiceLocator.auth

    private val _state = MutableStateFlow<UiState<Settings>>(UiState.Loading)
    val state = _state.asStateFlow()
    val saved = MutableStateFlow(false)

    init { load() }
    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            try { _state.value = UiState.Success(repo.me().settings) }
            catch (e: Exception) { _state.value = UiState.Error(e.message ?: "Could not load settings.") }
        }
    }

    /** Persist a changed setting to the API (FR-3: settings apply immediately + persist). */
    fun update(newSettings: Settings) {
        _state.value = UiState.Success(newSettings) // optimistic UI
        viewModelScope.launch {
            try { repo.updateSettings(newSettings); saved.value = true }
            catch (e: Exception) { Log.e("SettingsViewModel", "save failed", e) }
        }
    }

    suspend fun signOut() = auth.signOut()
}
