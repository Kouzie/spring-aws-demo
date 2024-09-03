package com.aws.demo.dynamodb.dto.req

data class ProductAddRequest(
    val name: String,
    val price: String,
)
