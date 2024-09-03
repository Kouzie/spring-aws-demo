package com.aws.demo.dynamodb.entity

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

@DynamoDbBean
class CustomerEntity(
    @get:DynamoDbPartitionKey
    var pk: String, // customer#{customerId}
    @get:DynamoDbSortKey
    var sk: String, // TIMESTAMP#{timestamp}
    var name: String,
    var age: Int,
    var email: String,
)
