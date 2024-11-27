package com.aws.demo.dynamodb

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

val <T : Any> T.logger: Logger
    get() = LoggerFactory.getLogger(this::class.java)

@SpringBootApplication
class DynamodbDemoApplication

fun main(args: Array<String>) {
    runApplication<DynamodbDemoApplication>(*args)
}
