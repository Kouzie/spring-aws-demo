package com.aws.demo.dynamodb.dto

data class CustomerWithOrderDto(
    val customerId: String,
    var name: String,
    var age: Int,
    var email: String,
    var expired: Long, // 유효기간
    var updated: String,
    var created: String,
    val orders: List<CustomerOrderDto>
)

data class CustomerOrderDto(
    var orderId: String,
    var amount: Int, // 결제금액
    var title: String, // 주문제목 ex: 삼다수 외 1건
)
