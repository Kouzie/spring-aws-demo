package com.aws.demo.dynamodb.entity

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

@DynamoDbBean
class ProductEntity(
    @get:DynamoDbPartitionKey
    var pk: String, // PRODUCT#{productId}
    @get:DynamoDbSortKey
    var sk: String, // PRODUCT#{productId}
    var type: String, // PRODUCT
    var name: String,
    var price: Int,
    var expired: Long, // 유효기간
)