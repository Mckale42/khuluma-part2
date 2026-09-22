package com.viltrumites.khuluma

import com.viltrumites.khuluma.data.remote.ExerciseDto
import com.viltrumites.khuluma.ui.lesson.LessonUi
import com.viltrumites.khuluma.ui.navigation.Routes
import org.junit.Assert.assertEquals
import org.junit.Test

/** JVM unit tests (run by GitHub Actions on every push). */
class KhulumaUnitTest {

    @Test
    fun lessonRoute_buildsExpectedPath() {
        assertEquals("lesson/l1", Routes.lesson("l1"))
    }

    @Test
    fun lessonProgress_isFractionOfExercisesDone() {
        val exercises = List(4) { ExerciseDto(id = "e$it", type = "mcq", prompt = "?", options = listOf("a", "b")) }
        val ui = LessonUi(title = "t", xpReward = 20, exercises = exercises, index = 1)
        assertEquals(0.25f, ui.progress, 0.0001f)
    }

    @Test
    fun lessonProgress_isZeroWhenNoExercises() {
        val ui = LessonUi(title = "t", xpReward = 0, exercises = emptyList())
        assertEquals(0f, ui.progress, 0.0001f)
    }
}
