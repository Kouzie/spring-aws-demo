package com.aws.demo.lambda.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.services.lambda.LambdaClient
import java.net.URI

@Configuration
class LambdaConfig {
    @Bean
    fun lambdaClient(credentialsProvider: AwsCredentialsProvider): LambdaClient {
        var lambdaClient = LambdaClient.builder()
            .credentialsProvider(credentialsProvider)
            .endpointOverride(URI.create("http://localhost:4566"))
            .build()
        return lambdaClient
    }

}