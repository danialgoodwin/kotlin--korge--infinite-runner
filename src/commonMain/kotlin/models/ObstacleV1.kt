package models

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.image
import com.soywiz.korge.view.name
import com.soywiz.korge.view.xy
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs

class ObstacleV1(private val startX: Double, private val startY: Double ): Container() {

    suspend fun create(): Image {
        val bitmap = resourcesVfs["cactus.png"].readBitmap()
        val img = image(bitmap).xy(startX, startY)
//        img.container().name("obstacle")
        img.name("obstacle")
        return img
    }

}
