package com.aws.demo.sqs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = ["com.aws.demo"])
class SqsDemoApplication

fun main(args: Array<String>) {
    runApplication<SqsDemoApplication>(*args)
}
