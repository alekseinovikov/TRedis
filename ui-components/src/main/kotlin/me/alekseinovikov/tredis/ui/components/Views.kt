package me.alekseinovikov.tredis.ui.components

import tornadofx.*

class FirstView : View("FirstTitle") {

    override val root = vbox {
        label { text = "Hello! World!" }
        button("Show animation again!") { action { replaceWithLoadingScreen<Main>() } }
    }

}
