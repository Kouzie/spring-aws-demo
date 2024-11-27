package com.aws.demo.dynamodb.dto

import com.aws.demo.dynamodb.entity.OrderItem
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

data class OrderDto(
    var orderId: String, // ORDER#{orderId}
    var customerId: String,
    var amount: Int, // 결제금액
    var expired: Long, // 유효기간
    var orderItems: List<OrderItemDto>
)

data class OrderItemDto(
    var price: Int,
    var productId: String,
)