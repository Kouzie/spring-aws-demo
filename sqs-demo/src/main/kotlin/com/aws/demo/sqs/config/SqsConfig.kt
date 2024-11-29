package com.aws.demo.sqs.config

import io.awspring.cloud.sqs.config.AbstractMessageListenerContainerFactory
import io.awspring.cloud.sqs.config.MessageListenerContainerFactory
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory
import io.awspring.cloud.sqs.listener.MessageListenerContainer
import io.awspring.cloud.sqs.listener.SqsMessageListenerContainer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI
import java.time.Duration


@Configuration
class SqsConfig {

    @Bean
    fun sqsClient(credentialsProvider: AwsCredentialsProvider): SqsClient {
        return SqsClient.builder()
            .endpointOverride(URI.create("http://localhost:4566")) // LocalStack 엔드포인트
            .credentialsProvider(credentialsProvider)
            .region(Region.US_EAST_1).build();
    }

    @Bean
    fun sqsAsyncClient(credentialsProvider: AwsCredentialsProvider): SqsAsyncClient {
        return SqsAsyncClient.builder()
            .endpointOverride(URI.create("http://localhost:4566")) // LocalStack 엔드포인트
            .credentialsProvider(credentialsProvider)
            .region(Region.US_EAST_1)
            .build();
    }

//    @Bean
//    fun sqsMessageListenerContainerFactory(sqsClient: SqsAsyncClient): MessageListenerContainerFactory<SqsMessageListenerContainer<String>> {
//        var factory = SqsMessageListenerContainerFactory<String>()
//        factory.setSqsAsyncClient(sqsClient)
//        factory.configure { it.pollTimeout(Duration.ofSeconds(10)) }
//        return factory;
//    }
}