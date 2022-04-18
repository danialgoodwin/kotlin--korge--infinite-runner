package models

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Text
import com.soywiz.korge.view.centerOnStage
import com.soywiz.korge.view.text
import com.soywiz.korio.async.delay

class GameManagerV1(private val container: Container) {

    var isRunning = false
    var status = GameStatusV1.NOT_STARTED
    private var message: Text? = null
    private var gameOverMessage: Text? = null

    fun start() {
        isRunning = true
        status = GameStatusV1.RUNNING
    }

    fun finish() {
        isRunning = false
        status = GameStatusV1.FINISHED
        if (message == null && gameOverMessage == null) {
            displayGameOverMessage()
        }
    }

    suspend fun restart() {
        status = GameStatusV1.RESTARTED
        removeGameOverMessage()
        delay(TimeSpan(500.0))
        start()
    }

    private fun displayGameOverMessage() {
        val gameOver = container.text("GAME OVER")
        gameOver.centerOnStage()
        gameOver.y += 50
        val text = container.text("Press Space to restart")
        text.centerOnStage()
        text.y += 100
        container.addChild(text)
        container.addChild(gameOver)
        message = text
        gameOverMessage = gameOver
    }

    private fun removeGameOverMessage() {
        container.removeChild(gameOverMessage)
        container.removeChild(message)
        message = null
        gameOverMessage = null
    }
}
