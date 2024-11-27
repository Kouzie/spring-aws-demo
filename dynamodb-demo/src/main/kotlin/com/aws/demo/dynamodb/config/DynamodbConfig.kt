package com.aws.demo.dynamodb.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

@Configuration
class DynamodbConfig {

    @Bean
    fun dynamoDbClient(): DynamoDbClient {
        return DynamoDbClient.builder()
            .endpointOverride(URI("http://localhost:4566")) // 로컬 DynamoDB URL
            .region(Region.US_EAST_1)
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("fakeAccessKey", "fakeSecretKey") // 로컬 DynamoDB는 임의의 키 사용
                )
            )
            .build()
    }

    @Bean
    fun dynamoDbEnhancedClient(dynamoDbClient: DynamoDbClient): DynamoDbEnhancedClient {
        val enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build()
        return enhancedClient
    }

}
