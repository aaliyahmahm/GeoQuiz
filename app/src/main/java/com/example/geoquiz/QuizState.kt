package com.example.geoquiz

/** Android-independent quiz state. -1 = unanswered, 0 = incorrect, 1 = correct. */
class QuizState(val questionCount: Int, initialIndex: Int = 0, savedResults: IntArray? = null) {
    init { require(questionCount > 0) }
    var currentIndex: Int = initialIndex.coerceIn(0, questionCount - 1)
        private set
    private val results = savedResults?.takeIf {
        it.size == questionCount && it.all { result -> result in -1..1 }
    }?.copyOf() ?: IntArray(questionCount) { -1 }
    val score: Int get() = results.count { it == 1 }
    val answeredCount: Int get() = results.count { it != -1 }
    val complete: Boolean get() = answeredCount == questionCount
    val currentResult: Int get() = results[currentIndex]
    fun answer(isCorrect: Boolean): Boolean {
        if (currentResult != -1) return false
        results[currentIndex] = if (isCorrect) 1 else 0
        return true
    }
    fun next() { currentIndex = (currentIndex + 1) % questionCount }
    fun previous() { currentIndex = (currentIndex + questionCount - 1) % questionCount }
    fun snapshot(): IntArray = results.copyOf()
}
