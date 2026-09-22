package com.viltrumites.khuluma.ui.buddy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viltrumites.khuluma.ServiceLocator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(val text: String, val fromUser: Boolean)

class BuddyViewModel : ViewModel() {
    private val repo = ServiceLocator.repository

    private val _messages = MutableStateFlow(
        listOf(ChatMessage("Sawubona! I'm Buddy. Ask me anything about isiZulu.", fromUser = false))
    )
    val messages = _messages.asStateFlow()
    val sending = MutableStateFlow(false)

    /** Send a message to the AI Buddy tutor via the API (FR-8). */
    fun send(text: String) {
        if (text.isBlank()) return
        _messages.value = _messages.value + ChatMessage(text, true)
        sending.value = true
        viewModelScope.launch {
            val reply = try { repo.chat(text) } catch (e: Exception) { "Sorry, I couldn't reply just now." }
            _messages.value = _messages.value + ChatMessage(reply, false)
            sending.value = false
        }
    }
}
