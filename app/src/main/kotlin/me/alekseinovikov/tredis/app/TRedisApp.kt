package me.alekseinovikov.tredis.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TRedisApp

fun main(vararg args: String) {
    runApplication<TRedisApp>(*args)
}