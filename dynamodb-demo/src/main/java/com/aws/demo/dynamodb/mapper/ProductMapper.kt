package com.aws.demo.dynamodb.mapper

import com.aws.demo.dynamodb.dto.req.ProductAddRequest
import com.aws.demo.dynamodb.dto.ProductDto
import com.aws.demo.dynamodb.entity.ProductEntity
import org.springframework.stereotype.Component

@Component
class ProductMapper {

    fun toEntity(request: ProductAddRequest): ProductEntity {
        return request.run {
            ProductEntity(
                pk = "PRODUCT#${productId}",
                sk = "TIMESTAMP#${timestamp}",
                name = name,
                price = price,
            )
        }
    }

    fun toDto(entity: ProductEntity): ProductDto {
        TODO("Not yet implemented")
    }
}
