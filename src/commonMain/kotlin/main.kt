import com.soywiz.korge.Korge
import com.soywiz.korge.scene.Module
import com.soywiz.korinject.AsyncInjector

suspend fun main() = Korge(Korge.Config(module = MainModule))

object MainModule : Module() {

    override val mainScene = MainSceneV1::class

    override suspend fun AsyncInjector.configure() {
        mapPrototype { MainSceneV1() }
    }

}
