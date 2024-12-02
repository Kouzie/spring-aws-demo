package com.aws.demo.paramstore.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SystemParamConfig {
    @Value("\${demo.properties.id}")
    lateinit var id: String
    @Value("\${demo.properties.key}")
    lateinit var key: String
    @Value("\${demo.properties.url}")
    lateinit var url: String

}