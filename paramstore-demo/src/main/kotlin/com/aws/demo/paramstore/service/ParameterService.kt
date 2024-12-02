package com.aws.demo.paramstore.service

import com.aws.demo.credential.logger
import com.aws.demo.paramstore.config.SystemParamConfig
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping

@Service
class ParameterService(
    val config: SystemParamConfig
) {

    @PostConstruct
    fun init() {
        logger.info("config.id: ${config.id}")
        logger.info("config.key: ${config.key}")
        logger.info("config.url: ${config.url}")
    }
}