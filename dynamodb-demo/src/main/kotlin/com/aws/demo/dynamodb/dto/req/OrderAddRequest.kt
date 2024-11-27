package com.aws.demo.dynamodb.dto.req

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean

data class OrderAddRequest(
    val customerId: String,
    val orderItem: List<OrderItemAddRequest>,
)

data class OrderItemAddRequest(
    var price: Int,
    var productId: String,
)