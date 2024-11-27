package com.aws.demo.dynamodb.dto

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

class CustomerDto (
    var customerId: String, // customer#{customerId}
    var name: String,
    var age: Int,
    var email: String,
    var expired: Long, // 유효기간
    var updated: String,
    var created: String,
)