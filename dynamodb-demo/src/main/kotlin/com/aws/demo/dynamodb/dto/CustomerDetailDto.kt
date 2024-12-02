package com.aws.demo.dynamodb.dto

import java.time.Instant

data class CustomerDetailDto (
    var customerId: String, // customer#{customerId}
    val username: String,
    val password: String,
    val nickname: String,
    val intro: String,
    val age: Int,
    val email: String,
    val expired: Long, // 유효기간
    val updated: Instant,
    val created: Instant,
)