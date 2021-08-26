package me.alekseinovikov.tredis.redis

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@ComponentScan(basePackageClasses = [RedisConfiguration::class])
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
