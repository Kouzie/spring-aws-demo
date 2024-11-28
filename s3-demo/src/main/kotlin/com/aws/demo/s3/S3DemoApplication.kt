package com.aws.demo.s3

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

val <T : Any> T.logger: Logger
    get() = LoggerFactory.getLogger(this::class.java)

@SpringBootApplication(scanBasePackages = ["com.aws.demo"])
class S3DemoApplication

fun main(args: Array<String>) {
    runApplication<S3DemoApplication>(*args)
}
