package com.example.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class CheatActivity : Activity() {
    private var revealed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        findViewById<TextView>(R.id.questionText).text = intent.getStringExtra(EXTRA_QUESTION)
        revealed = savedInstanceState?.getBoolean(KEY_REVEALED) ?: false
        findViewById<Button>(R.id.showAnswerButton).setOnClickListener {
            revealed = true
            renderAnswer()
        }
        findViewById<Button>(R.id.backButton).setOnClickListener { finish() }
        renderAnswer()
    }
    private fun renderAnswer() {
        findViewById<TextView>(R.id.answerText).setText(when {
            !revealed -> R.string.answer_hidden
            intent.getBooleanExtra(EXTRA_ANSWER, false) -> R.string.answer_true
            else -> R.string.answer_false
        })
        findViewById<Button>(R.id.showAnswerButton).isEnabled = !revealed
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(KEY_REVEALED, revealed)
        super.onSaveInstanceState(outState)
    }
    companion object {
        private const val EXTRA_QUESTION = "com.example.geoquiz.question"
        private const val EXTRA_ANSWER = "com.example.geoquiz.answer"
        private const val KEY_REVEALED = "answer_revealed"
        fun newIntent(context: Context, question: String, answer: Boolean): Intent =
            Intent(context, CheatActivity::class.java).apply {
                putExtra(EXTRA_QUESTION, question)
                putExtra(EXTRA_ANSWER, answer)
            }
    }
}
