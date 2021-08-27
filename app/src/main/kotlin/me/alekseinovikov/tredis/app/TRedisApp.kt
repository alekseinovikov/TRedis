package me.alekseinovikov.tredis.app

import javafx.application.Application
import javafx.scene.image.Image
import me.alekseinovikov.tredis.ui.components.FirstView
import me.alekseinovikov.tredis.ui.components.Images
import me.alekseinovikov.tredis.ui.components.Main
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import tornadofx.App
import tornadofx.DIContainer
import tornadofx.FX
import tornadofx.addStageIcon
import kotlin.reflect.KClass

@SpringBootApplication
class TRedisApp : App(Main::class) {
    private lateinit var context: ConfigurableApplicationContext

    override fun init() {
        this.context =
            SpringApplication.run(this.javaClass)
        context.autowireCapableBeanFactory.autowireBean(this)

        FX.dicontainer = object : DIContainer {
            override fun <T : Any> getInstance(type: KClass<T>): T =
                context.getBean(type.java)

            override fun <T : Any> getInstance(type: KClass<T>, name: String): T = context.getBean(name, type.java)
        }

        addStageIcon(Image(Images.logo))
    }

    override fun stop() {
        super.stop()
        context.close()
    }
}

fun main(vararg args: String) {
    Application.launch(TRedisApp::class.java, *args)
}