package com.aws.demo.dynamodb.mapper

import com.aws.demo.dynamodb.dto.req.ProductAddRequest
import com.aws.demo.dynamodb.dto.ProductDto
import com.aws.demo.dynamodb.entity.ProductEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProductMapper {

    fun toEntity(request: ProductAddRequest): ProductEntity {
        return request.run {
            ProductEntity(
                pk = "PRODUCT#${UUID.randomUUID()}",
                sk = "TIMESTAMP#${System.currentTimeMillis()}",
                type = "PRODUCT",
                name = name,
                price = price,
                expired = -1,
            )
        }
    }

    fun toDto(entity: ProductEntity): ProductDto {
        return ProductDto(
            productId = entity.pk.split("#")[1],
            name = entity.name,
            price = entity.price,
            expired = entity.expired,
        )
    }
}
