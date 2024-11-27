package com.aws.demo.dynamodb.dto.req

data class CustomerAddRequest(
    val name: String,
    val age: Int,
    val email: String,
)