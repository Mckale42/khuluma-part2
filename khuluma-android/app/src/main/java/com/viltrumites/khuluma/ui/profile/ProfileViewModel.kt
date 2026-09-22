package com.viltrumites.khuluma.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viltrumites.khuluma.ServiceLocator
import com.viltrumites.khuluma.data.remote.MeResponse
import com.viltrumites.khuluma.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repo = ServiceLocator.repository
    private val _state = MutableStateFlow<UiState<MeResponse>>(UiState.Loading)
    val state = _state.asStateFlow()

    init { load() }
    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            try { _state.value = UiState.Success(repo.me()) }
            catch (e: Exception) { _state.value = UiState.Error(e.message ?: "Could not load profile.") }
        }
    }
}
