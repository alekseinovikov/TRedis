package me.freedom4live.tredis.tredis

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.*
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.nio.ByteBuffer


private val logger = KotlinLogging.logger {}

@SpringBootApplication
class TRedisApplication : CommandLineRunner {

    @Autowired
    private lateinit var redisOperations: ReactiveRedisOperations<String, Coffee>

    @Autowired
    private lateinit var redisConnectionFactory: ReactiveRedisConnectionFactory


    override fun run(vararg args: String?): Unit = runBlocking {
        val late = Coffee("LATE", "late")
        val result = redisOperations.opsForValue().setAndAwait(late.id, late)

        logger.info { "Result of adding value: $result" }

        val fromRedis = redisOperations.opsForValue().getAndAwait(late.id)

        logger.info { "Found value in redis: $fromRedis" }

        val keys = redisOperations.executeAsFlow {
            it.keyCommands().scan()
                .map { bb -> String(bb.array()) }
                .asFlow()
        }.toList()

        val refsCount = redisOperations.executeAsFlow { conn ->
            keys.map { key ->
                conn.keyCommands().refcount(ByteBuffer.wrap(key.toByteArray()))
            }.asFlow()
        }.mapNotNull { it.awaitFirstOrNull() }
            .toList()

        logger.info { "Refs count: $refsCount" }
    }

}

fun main(args: Array<String>) {
    runApplication<TRedisApplication>(*args)
}


@Configuration
class RedisConfiguration {

    @Bean
    fun redisOperations(redisFactory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, Coffee> {
        val serializer = Jackson2JsonRedisSerializer(Coffee::class.java).apply {
            setObjectMapper(ObjectMapper().registerKotlinModule())
        }

        val builder = RedisSerializationContext.newSerializationContext<String, Coffee>(StringRedisSerializer())

        val context = builder.value(serializer).build()
        return ReactiveRedisTemplate(redisFactory, context)
    }

}

data class Coffee(val id: String, val name: String)