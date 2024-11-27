package com.aws.demo.dynamodb.dto

import java.time.Instant

data class CustomerDto (
    var customerId: String, // customer#{customerId}
    val username: String,
    val expired: Long,
    val updated: Instant,
    val created: Instant,
)
