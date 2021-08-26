package me.alekseinovikov.tredis.redis

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.executeAsFlow
import org.springframework.data.redis.core.getAndAwait
import org.springframework.data.redis.core.setAndAwait
import org.springframework.stereotype.Component
import java.nio.ByteBuffer


data class Coffee(val id: String, val name: String)

private val logger = KotlinLogging.logger {}

@Component
class RedisRunner : CommandLineRunner {

    @Autowired
    private lateinit var redisOperations: ReactiveRedisOperations<String, Coffee>

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

        val stats = redisOperations.executeAsFlow { conn ->
            keys.map { key ->
                val keyByteBuffer = ByteBuffer.wrap(key.toByteArray())
                Triple(
                    key,
                    conn.keyCommands().encodingOf(keyByteBuffer),
                    conn.keyCommands().refcount(keyByteBuffer)
                )
            }.asFlow()
        }.mapNotNull { origin ->
            val refsCount = origin.third.awaitFirstOrNull()
            if (refsCount == null || refsCount <= 0) return@mapNotNull null

            val encodingValue: String? =
                origin.second.awaitSingle().raw()
            Triple(origin.first, encodingValue, refsCount)
        }.toList()

        logger.info { "Stats: $stats" }
    }

}