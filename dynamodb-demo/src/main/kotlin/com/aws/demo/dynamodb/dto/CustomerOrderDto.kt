package com.aws.demo.dynamodb.dto

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

class CustomerOrderDto(
    var pk: String, // CUSTOMER#{customerId}
    var sk: String, // ORDER#{timestamp}#{orderId}
)