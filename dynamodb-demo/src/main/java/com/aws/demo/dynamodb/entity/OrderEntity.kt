package com.aws.demo.dynamodb.entity

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

@DynamoDbBean
class OrderEntity(
    @get:DynamoDbPartitionKey
    var pk: String, // ORDER#{orderId}
    @get:DynamoDbSortKey
    var sk: String, // TIMESTAMP#{timestamp}
    var customerId: String,
    var amount: String,
    var orderItem: List<OrderItem>, // upserted timestamp
)

@DynamoDbBean
class OrderItem(
    var name: String,
    var price: Int,
)