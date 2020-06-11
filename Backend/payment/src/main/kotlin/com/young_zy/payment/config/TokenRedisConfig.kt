package com.young_zy.payment.config

import com.young_zy.payment.config.property.TokenRedisProperties
import com.young_zy.payment.model.Token
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
class TokenRedisConfig(
        @Autowired val tokenRedisProperties: TokenRedisProperties
) {
    @Bean
    @Primary
    fun tokenRedisConnectionFactory(): RedisConnectionFactory {
        val conf = RedisStandaloneConfiguration(tokenRedisProperties.host, tokenRedisProperties.port)
        conf.setPassword(tokenRedisProperties.password)
        conf.database = tokenRedisProperties.dbIndex
        return LettuceConnectionFactory(conf)
    }

    @Bean
    fun tokenRedisTemplate(): RedisTemplate<String, Token>? {
        val template = RedisTemplate<String, Token>()
        template.setConnectionFactory(tokenRedisConnectionFactory())
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = Jackson2JsonRedisSerializer(Token::class.java)
        return template
    }

}