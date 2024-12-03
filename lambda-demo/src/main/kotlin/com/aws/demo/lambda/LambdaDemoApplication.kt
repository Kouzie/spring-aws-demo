package com.aws.demo.lambda

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.aws.demo"])
class LambdaDemoApplication

fun main(args: Array<String>) {
    runApplication<LambdaDemoApplication>(*args)
}
