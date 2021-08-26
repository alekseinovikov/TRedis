package me.alekseinovikov.tredis.app

import javafx.application.Application
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import tornadofx.App
import tornadofx.DIContainer
import tornadofx.FX
import kotlin.reflect.KClass

@SpringBootApplication
class TRedisApp : App(FirstView::class) {
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
    }

    override fun stop() {
        super.stop()
        context.close()
    }
}

fun main(vararg args: String) {
    Application.launch(TRedisApp::class.java, *args)
}