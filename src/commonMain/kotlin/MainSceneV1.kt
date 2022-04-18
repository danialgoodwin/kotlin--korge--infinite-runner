import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Container
import models.GameManagerV1
import models.PlayerV1
import models.ScoreBoardV1
import models.WorldV1

class MainSceneV1 : Scene() {

    override suspend fun Container.sceneInit() {
        val game = GameManagerV1(this)
        val player = PlayerV1(game)
        val world = WorldV1(game)
        val scoreBoard = ScoreBoardV1(game)
        addChild(player)
        addChild(world)
        addChild(scoreBoard)
        world.init()
        player.init()
    }

}
