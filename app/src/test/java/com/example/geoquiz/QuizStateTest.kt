package com.example.geoquiz

import org.junit.Assert.*
import org.junit.Test

class QuizStateTest {
    @Test fun repeatedAnswerDoesNotChangeScore() {
        val state = QuizState(6)
        assertTrue(state.answer(true))
        assertFalse(state.answer(false))
        assertEquals(1, state.score)
        assertEquals(1, state.answeredCount)
    }
    @Test fun restoredStateRetainsProgressAndBlocksRescoring() {
        val state = QuizState(6)
        state.answer(true); state.next(); state.answer(false)
        val restored = QuizState(6, state.currentIndex, state.snapshot())
        assertEquals(1, restored.currentIndex)
        assertEquals(1, restored.score)
        assertEquals(2, restored.answeredCount)
        assertFalse(restored.answer(true))
    }
    @Test fun allQuestionsMustBeAnsweredForCompletion() {
        val state = QuizState(6)
        repeat(5) { state.answer(true); state.next() }
        assertFalse(state.complete)
        state.answer(false)
        assertTrue(state.complete)
        assertEquals(5, state.score)
    }
    @Test fun navigationWrapsAndPreservesAnswers() {
        val state = QuizState(6)
        state.answer(true); state.previous()
        assertEquals(5, state.currentIndex)
        state.next()
        assertEquals(1, state.currentResult)
    }
}
