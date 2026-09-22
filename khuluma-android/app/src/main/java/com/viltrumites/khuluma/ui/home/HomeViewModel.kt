package com.viltrumites.khuluma.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viltrumites.khuluma.ServiceLocator
import com.viltrumites.khuluma.data.remote.CoursePath
import com.viltrumites.khuluma.data.remote.MeResponse
import com.viltrumites.khuluma.data.remote.WordOfDay
import com.viltrumites.khuluma.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeData(val me: MeResponse, val path: CoursePath, val word: WordOfDay)

class HomeViewModel : ViewModel() {
    private val repo = ServiceLocator.repository
    private val _state = MutableStateFlow<UiState<HomeData>>(UiState.Loading)
    val state = _state.asStateFlow()

    init { load() }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            try {
                val me = repo.me()
                val path = repo.path(me.learningLanguage)
                val word = repo.wordOfDay()
                _state.value = UiState.Success(HomeData(me, path, word))
            } catch (e: Exception) {
                Log.e("HomeViewModel", "load failed", e)
                _state.value = UiState.Error(e.message ?: "Could not load your learning path.")
            }
        }
    }
}
