package com.aws.demo.dynamodb.entity

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey
import java.time.Instant

@DynamoDbBean
data class CustomerEntity(
    @get:DynamoDbPartitionKey
    var pk: String, // CUSTOMER#{customerId}
    @get:DynamoDbSortKey
    var sk: String, // CUSTOMER#{customerId}
    var type: String, // CUSTOMER
    var username: String,
    var password: String,
    var expired: Long, // 유효기간
    var updated: Instant,
    var created: Instant
) {
}
