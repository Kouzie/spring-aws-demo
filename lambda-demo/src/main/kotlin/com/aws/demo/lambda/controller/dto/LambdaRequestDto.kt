package com.aws.demo.lambda.controller.dto

data class LambdaRequestDto(
    val functionName: String,
    val type: String,
    val payload: String,
)
