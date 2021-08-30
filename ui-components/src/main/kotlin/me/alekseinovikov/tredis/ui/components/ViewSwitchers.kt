package me.alekseinovikov.tredis.ui.components

import javafx.animation.Interpolator
import javafx.geometry.Pos
import tornadofx.*
import kotlin.reflect.KClass

class LoadingView<T : UIComponent>(private val nextView: KClass<T>) : View("TRedis") {

    override val root = vbox {
        prefHeight = 400.0
        prefWidth = 400.0

        val logo = imageview(Images.logo) {
            alignment = Pos.CENTER
            fitHeight = 150.0
            fitWidth = 150.0
        }

        timeline {
            keyframe(0.3.seconds) {
                this.keyvalue(logo.fitHeightProperty(), 250.0, Interpolator.EASE_BOTH)
                this.keyvalue(logo.fitWidthProperty(), 250.0, Interpolator.EASE_BOTH)
            }

            isAutoReverse = true
            cycleCount = 5

            this.setOnFinished {
                replaceWith(nextView)
            }
        }
    }
}

inline fun <reified T : UIComponent> UIComponent.replaceWithLoadingScreen() = run {
    val loadingScreen = LoadingView(T::class)
    replaceWith(loadingScreen)
}