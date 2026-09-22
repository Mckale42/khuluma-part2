package com.viltrumites.khuluma.ui.lesson

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viltrumites.khuluma.ServiceLocator
import com.viltrumites.khuluma.data.remote.AttemptBody
import com.viltrumites.khuluma.data.remote.AttemptResult
import com.viltrumites.khuluma.data.remote.ExerciseDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class Phase { QUESTION, FEEDBACK, COMPLETE }

data class LessonUi(
    val title: String,
    val xpReward: Int,
    val exercises: List<ExerciseDto>,
    val index: Int = 0,
    val phase: Phase = Phase.QUESTION,
    val submitting: Boolean = false,
    val lastResult: AttemptResult? = null,
    val lastAnswer: String = "",
    val totalGainedXp: Int = 0,
    val correctCount: Int = 0,
    val explanation: String? = null,
    val explaining: Boolean = false
) {
    val current: ExerciseDto get() = exercises[index]
    val progress: Float get() = if (exercises.isEmpty()) 0f else index.toFloat() / exercises.size
}

sealed interface LessonState {
    data object Loading : LessonState
    data class Error(val message: String) : LessonState
    data class Running(val ui: LessonUi) : LessonState
}

class LessonViewModel : ViewModel() {
    private val repo = ServiceLocator.repository
    private val _state = MutableStateFlow<LessonState>(LessonState.Loading)
    val state = _state.asStateFlow()

    private var lessonId: String = ""
    private var questionStart = System.currentTimeMillis()

    fun load(id: String) {
        lessonId = id
        _state.value = LessonState.Loading
        viewModelScope.launch {
            try {
                val next = repo.nextExercises(id)
                questionStart = System.currentTimeMillis()
                _state.value = LessonState.Running(LessonUi(next.title, next.xpReward, next.exercises))
            } catch (e: Exception) {
                Log.e("LessonViewModel", "load failed", e)
                _state.value = LessonState.Error(e.message ?: "Could not load the lesson.")
            }
        }
    }

    /** Submit the learner's answer; the API decides correctness and awards XP. */
    fun submit(answer: String) {
        val ui = (_state.value as? LessonState.Running)?.ui ?: return
        val responseMs = System.currentTimeMillis() - questionStart
        _state.value = LessonState.Running(ui.copy(submitting = true, lastAnswer = answer))
        viewModelScope.launch {
            try {
                val result = repo.submit(AttemptBody(ui.current.id, lessonId, answer, responseMs))
                _state.value = LessonState.Running(
                    ui.copy(
                        submitting = false,
                        phase = Phase.FEEDBACK,
                        lastResult = result,
                        lastAnswer = answer,
                        totalGainedXp = ui.totalGainedXp + result.xpAwarded,
                        correctCount = ui.correctCount + if (result.correct) 1 else 0
                    )
                )
            } catch (e: Exception) {
                Log.e("LessonViewModel", "submit failed", e)
                _state.value = LessonState.Running(ui.copy(submitting = false))
            }
        }
    }

    /** AI mistake coach — asks the API why the last answer was wrong. */
    fun explain() {
        val ui = (_state.value as? LessonState.Running)?.ui ?: return
        val correct = ui.lastResult?.correctAnswer ?: return
        _state.value = LessonState.Running(ui.copy(explaining = true))
        viewModelScope.launch {
            val text = try {
                repo.explain(ui.current.prompt, ui.lastAnswer, correct)
            } catch (e: Exception) {
                "Could not load an explanation right now."
            }
            val now = (_state.value as? LessonState.Running)?.ui ?: return@launch
            _state.value = LessonState.Running(now.copy(explaining = false, explanation = text))
        }
    }

    fun next() {
        val ui = (_state.value as? LessonState.Running)?.ui ?: return
        if (ui.index + 1 >= ui.exercises.size) {
            _state.value = LessonState.Running(ui.copy(phase = Phase.COMPLETE))
        } else {
            questionStart = System.currentTimeMillis()
            _state.value = LessonState.Running(
                ui.copy(index = ui.index + 1, phase = Phase.QUESTION, lastResult = null, lastAnswer = "", explanation = null)
            )
        }
    }
}
