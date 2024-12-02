package com.aws.demo.dynamodb.dto.req


data class CustomerAddRequest(
    val username: String,
    val password: String,
    val nickname: String,
    val intro: String,
    val age: Int,
    val email: String,
)