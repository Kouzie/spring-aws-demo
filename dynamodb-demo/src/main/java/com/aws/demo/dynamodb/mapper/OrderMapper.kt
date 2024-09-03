package com.aws.demo.dynamodb.mapper

import com.aws.demo.dynamodb.dto.OrderDto
import com.aws.demo.dynamodb.dto.req.OrderAddRequest
import com.aws.demo.dynamodb.entity.OrderEntity
import org.springframework.stereotype.Component

@Component
class OrderMapper {
    fun toEntity(request: OrderAddRequest): OrderEntity {
        return request.run {
            OrderEntity(
                pk = "ORDER#${orderId}",
                sk = "TIMESTAMP#${timestamp}",
                customerId = "",
                amount = "",
                orderItem = "",
            )
        }
    }

    fun toDto(entity: OrderEntity): OrderDto {
        TODO("Not yet implemented")
    }

}
