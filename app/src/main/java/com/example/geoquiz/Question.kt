package com.example.geoquiz

data class Question(val textResId: Int, val answer: Boolean)

object QuestionBank {
    val questions = listOf(
        Question(R.string.q1, true), Question(R.string.q2, false),
        Question(R.string.q3, true), Question(R.string.q4, false),
        Question(R.string.q5, true), Question(R.string.q6, false)
    )
}
