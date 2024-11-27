package com.aws.demo.dynamodb.dto

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

data class ProductDto(
    var productId: String,
    var name: String,
    var price: Int,
    var expired: Long,
)
