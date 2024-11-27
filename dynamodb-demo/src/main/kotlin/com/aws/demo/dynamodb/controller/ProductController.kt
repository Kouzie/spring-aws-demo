package com.aws.demo.dynamodb.controller

import com.aws.demo.dynamodb.dto.ProductDto
import com.aws.demo.dynamodb.dto.req.ProductAddRequest
import com.aws.demo.dynamodb.service.ProductService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product")
class ProductController(
    val service: ProductService
) {
    @PostMapping
    fun create(request: ProductAddRequest): ProductDto {
        return service.create(request)
    }
}