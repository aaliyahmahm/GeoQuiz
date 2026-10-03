package com.example.geoquiz

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var quiz: QuizState
    private val questions = QuestionBank.questions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        quiz = QuizState(questions.size, savedInstanceState?.getInt(KEY_INDEX) ?: 0,
            savedInstanceState?.getIntArray(KEY_RESULTS))
        findViewById<Button>(R.id.trueButton).setOnClickListener { checkAnswer(true) }
        findViewById<Button>(R.id.falseButton).setOnClickListener { checkAnswer(false) }
        findViewById<Button>(R.id.nextButton).setOnClickListener { quiz.next(); render() }
        findViewById<Button>(R.id.previousButton).setOnClickListener { quiz.previous(); render() }
        findViewById<Button>(R.id.cheatButton).setOnClickListener {
            val question = questions[quiz.currentIndex]
            // Explicit Intent: names the destination Activity and supplies extras.
            startActivity(CheatActivity.newIntent(this, getString(question.textResId), question.answer))
        }
        findViewById<Button>(R.id.restartButton).setOnClickListener {
            quiz = QuizState(questions.size)
            render()
        }
        render()
    }

    private fun checkAnswer(answer: Boolean) {
        val correct = answer == questions[quiz.currentIndex].answer
        if (quiz.answer(correct)) {
            Toast.makeText(this, if (correct) R.string.correct else R.string.incorrect,
                Toast.LENGTH_SHORT).show()
            render()
        }
    }

    private fun render() {
        findViewById<TextView>(R.id.questionNumber).text =
            getString(R.string.question_number, quiz.currentIndex + 1, questions.size)
        findViewById<TextView>(R.id.questionText).setText(questions[quiz.currentIndex].textResId)
        findViewById<TextView>(R.id.scoreText).text = if (quiz.complete)
            getString(R.string.final_score, quiz.score, questions.size)
        else getString(R.string.progress, quiz.answeredCount, questions.size, quiz.score)
        findViewById<TextView>(R.id.answerStatus).setText(when (quiz.currentResult) {
            1 -> R.string.answered_correct
            0 -> R.string.answered_incorrect
            else -> R.string.unanswered
        })
        findViewById<Button>(R.id.trueButton).isEnabled = quiz.currentResult == -1
        findViewById<Button>(R.id.falseButton).isEnabled = quiz.currentResult == -1
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_INDEX, quiz.currentIndex)
        outState.putIntArray(KEY_RESULTS, quiz.snapshot())
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val KEY_INDEX = "question_index"
        private const val KEY_RESULTS = "question_results"
    }
}
