package me.alekseinovikov.tredis.app

import tornadofx.*

class FirstView : View("FirstTitle") {

    override val root = vbox {
        label { text = "Hello! World!" }
        button("Go to second view") { action { replaceWith<SecondView>() } }
    }

}

class SecondView : View("SecondTitle") {

    override val root = vbox {
        label { text = "Hello! Again!" }
        button("Go to first view") { action { replaceWith<FirstView>() } }
    }

}