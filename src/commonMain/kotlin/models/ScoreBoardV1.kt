package models

import com.soywiz.klock.timesPerSecond
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Text
import com.soywiz.korge.view.addFixedUpdater
import com.soywiz.korge.view.text
import com.soywiz.korge.view.xy

class ScoreBoardV1(private val game: GameManagerV1) : Container() {
    private var score: Int = 0;
    private var scoreView: Text = text("SCORE $score").xy(10.0, 10.0)

    init {
        addChild(scoreView)
        addFixedUpdater(2.timesPerSecond) {
            if (game.status == GameStatusV1.RESTARTED) {
                restartScore()
            } else if (game.isRunning) {
                updateScore()
            }
        }
    }

    private fun updateScore() {
        score++
        scoreView.text = "SCORE $score"
    }

    private fun restartScore() {
        score = 0
        scoreView.text = "SCORE $score"
    }

}
