package me.alekseinovikov.tredis.ui.components

import mu.KotlinLogging
import tornadofx.*

private val logger = KotlinLogging.logger {}

class Main : View("TRedis") {

    override val root = vbox {
        prefHeight = 400.0
        prefWidth = 400.0
    }

    override fun onDock() {
        super.onDock()

        runLater(100.millis) { replaceWithLoadingScreen<FirstView>() }
    }

}