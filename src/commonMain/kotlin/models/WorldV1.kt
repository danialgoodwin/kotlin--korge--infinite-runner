package models

import com.soywiz.klock.timesPerSecond
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.addFixedUpdater
import com.soywiz.korge.view.position
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import utils.randomInRange

class WorldV1(private val game: GameManagerV1) : Container() {

    private var speedFactor: Double = 10.0
    private val originX = 1300.0
    private val xSpacer = 300.0
    private val speedIncrement = 0.005
    private val floorY = 350.0
    private var obstacles: MutableList<Image> = mutableListOf()
    private var floor: MutableList<Image> = mutableListOf()
    suspend fun init() {
        initializeWorld()
        initializeUpdater()
    }

    private suspend fun initializeWorld() {
        var obstacleX = originX
        val y = 320.0
        var floorX = 0.0
        for (i in 0..2) {
            obstacles.add(ObstacleV1(obstacleX, y).create())
            floor.add(FloorTileV1(floorX, floorY).create())
            obstacleX += 500
            floorX += 1200
        }
        obstacles.forEach {
            addChild(it)
        }
        floor.forEach {
            addChild(it)
        }
    }

    private fun initializeUpdater() {
        addFixedUpdater(60.timesPerSecond) {
            if (game.status == GameStatusV1.RESTARTED) {
                destroyWorld()
                speedFactor = 10.0
            }
            if (game.isRunning) {
                speedFactor += speedIncrement
                val floorIterator = this.floor.iterator()
                while (floorIterator.hasNext()) {
                    val floor = floorIterator.next()
                    val x = floor.x - (1 * speedFactor)
                    floor.position(x, floor.y)

                    if ((x + 1200 < 0)) {
                        floorIterator.remove()
                        removeChild(floor)
                        addFloorTile()
                    }
                }

                val obstacleIterator = this.obstacles.iterator()
                while (obstacleIterator.hasNext()) {
                    val obstacle = obstacleIterator.next()
                    val x = obstacle.x - (1 * speedFactor)
                    obstacle.position(x, obstacle.y)
                    if (x < 0) {
                        obstacleIterator.remove()
                        removeChild(obstacle)
                        addObstacle()
                    }
                }
            }
        }
    }


    private fun addObstacle() {
        if (game.isRunning) {
            GlobalScope.launch {
                val newObstacle = ObstacleV1(originX + randomInRange(0.0, xSpacer), 320.0).create()
                addChild(newObstacle)
                obstacles.add(newObstacle)
            }
        }
    }

    private fun addFloorTile() {
        if (game.isRunning) {
            GlobalScope.launch {
                val newFloor = FloorTileV1(1200.0, floorY).create()
                addChild(newFloor)
                floor.add(newFloor)
            }
        }
    }

    private fun destroyWorld() {
        obstacles.forEach { this@WorldV1.removeChild(it) }
        floor.forEach { this@WorldV1.removeChild(it) }
        obstacles = mutableListOf()
        floor = mutableListOf()
        GlobalScope.launch {
            initializeWorld()
        }
    }
}
