package com.aws.demo.dynamodb.controller

import com.aws.demo.dynamodb.dto.OrderDto
import com.aws.demo.dynamodb.dto.req.OrderAddRequest
import com.aws.demo.dynamodb.service.OrderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order")
class OrderController(
    private val service: OrderService
) {

    @PostMapping
    fun addOrder(@RequestBody request: OrderAddRequest): OrderDto {
        var dto = service.create(request)
        return dto
    }

    @GetMapping("/detail/{orderId}")
    fun getOrder(@PathVariable orderId: String): OrderDto {
        return service.getById(orderId)
    }

    @GetMapping("/customer/{customerId}")
    fun getOrderByCustomerId(
        @PathVariable customerId: String,
        @RequestParam beginDate: Long,
        @RequestParam endingDate: Long
    ): List<OrderDto> {
        return service.getByCustomerId(customerId, beginDate, endingDate);
    }

}