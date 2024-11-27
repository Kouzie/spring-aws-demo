package com.aws.demo.dynamodb.entity

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbFlatten
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

@DynamoDbBean
class CustomerOrderEntity(
    @get:DynamoDbPartitionKey
    var pk: String, // CUSTOMER#{customerId}
    @get:DynamoDbSortKey
    var sk: String, // ORDER#{timestamp}#{orderId}
    var type: String, // CUSTOMER_ORDER

    var amount: Int, // 결제금액
    var title: String, // 주문제목 ex: 삼다수 외 1건
)
