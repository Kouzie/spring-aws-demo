package com.aws.demo.dynamodb.entity

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

/**
 * 주문 상세
 * */
@DynamoDbBean
class OrderEntity(
    @get:DynamoDbPartitionKey
    var pk: String, // ORDER#{orderId}
    @get:DynamoDbSortKey
    var sk: String, // ORDER#{timestamp}
    var type: String, // ORDER
    var customerId: String,
    var amount: Int, // 결제금액
    var expired: Long, // 유효기간
    var orderItems: List<OrderItem>
)

@DynamoDbBean
data class OrderItem (
    var price: Int,
    var productId: String,
)
