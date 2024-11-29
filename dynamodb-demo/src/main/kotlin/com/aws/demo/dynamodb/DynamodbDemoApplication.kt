package com.aws.demo.dynamodb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.aws.demo"])
class DynamodbDemoApplication

fun main(args: Array<String>) {
    runApplication<DynamodbDemoApplication>(*args)
}
