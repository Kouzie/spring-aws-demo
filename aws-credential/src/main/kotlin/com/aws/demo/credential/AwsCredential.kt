package com.aws.demo.credential

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider

val <T : Any> T.logger: Logger
    get() = LoggerFactory.getLogger(this::class.java)

@Configuration
class AwsCredential {

    @Bean
    fun awsCredentialsProvider(): AwsCredentialsProvider {
        return StaticCredentialsProvider.create(
            AwsBasicCredentials.create("test-access-key", "test-secret-key")
        )
    }
}