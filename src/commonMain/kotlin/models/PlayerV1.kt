package models

import com.soywiz.klock.TimeSpan
import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.SpriteAnimation
import com.soywiz.korge.view.Text
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.onCollision
import com.soywiz.korge.view.position
import com.soywiz.korge.view.sprite
import com.soywiz.korge.view.xy
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import utils.CoordinatesV1

class PlayerV1(private val game: GameManagerV1) : Container() {

    private var status = Status.RUNNING
    private var speedFactor = 10.0
    private val yLimit = 150.0
    private val initialY = 300.0
    private lateinit var dino: Sprite
    private var message: Text? = null

    suspend fun init() {
        dino = this.buildDino()

        keys {
            down(Key.SPACE) {
                if (game.status == GameStatusV1.NOT_STARTED) {
                    game.start()
                }
                if (game.status == GameStatusV1.FINISHED) {
                    game.restart()
                } else {
                    val dinoY = dino.y
                    if (dinoY >= 200) {
                        status = Status.JUMPING_UP
                    }

                }
            }
        }
        dino.onCollision({ it.name === "obstacle" }) {
            game.finish()
            stopAnimation()
        }
        dino.addUpdater {
            if (game.isRunning) {
                val coordinates = this@PlayerV1.getCoordinates(this.x, this.y)
                dino.position(coordinates.x, coordinates.y)
            }
        }
    }

    private suspend fun buildDino(): Sprite {
        val image = resourcesVfs["dino.png"].readBitmap()
        val walking = SpriteAnimation(
            spriteMap = image,
            spriteWidth = 53,
            spriteHeight = 56,
            columns = 2

        )
        val sprite = sprite(walking)
        sprite.xy(40.0, initialY)

        return sprite
    }

    private fun startAnimation() {
        dino.playAnimationLooped(spriteDisplayTime = TimeSpan(200.0))
        this.message?.removeFromParent()
        this.message = null
    }

    private fun stopAnimation() {
        dino.stopAnimation()
    }

    private fun getCoordinates(startingX: Double, startingY: Double): CoordinatesV1 {
        val x: Double = startingX
        var y: Double = startingY

        when (status) {
            Status.JUMPING_UP -> {
                y -= 1 * speedFactor
                if (y <= yLimit) {
                    status = Status.JUMPING_DOWN
                }
            }

            Status.JUMPING_DOWN -> {
                y += 1 * speedFactor
                if (y >= initialY) {
                    status = Status.RUNNING
                }
            }
            Status.RUNNING -> {
                this.startAnimation()
            }
        }

        return CoordinatesV1(x, y)
    }

    enum class Status {
        RUNNING,
        JUMPING_UP,
        JUMPING_DOWN
    }

}
