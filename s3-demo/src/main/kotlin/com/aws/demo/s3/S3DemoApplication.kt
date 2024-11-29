package com.aws.demo.s3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.aws.demo"])
class S3DemoApplication

fun main(args: Array<String>) {
    runApplication<S3DemoApplication>(*args)
}
