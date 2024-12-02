package com.aws.demo.paramstore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = ["com.aws.demo"])
class ParameterStoreDemoApplication

fun main(args: Array<String>) {
    runApplication<ParameterStoreDemoApplication>(*args)
}
