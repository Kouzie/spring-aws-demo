package com.aws.demo.dynamodb.mapper

import com.aws.demo.dynamodb.dto.OrderDto
import com.aws.demo.dynamodb.dto.OrderItemDto
import com.aws.demo.dynamodb.dto.req.OrderAddRequest
import com.aws.demo.dynamodb.entity.CustomerOrderEntity
import com.aws.demo.dynamodb.entity.OrderEntity
import com.aws.demo.dynamodb.entity.OrderItem
import org.springframework.stereotype.Component
import java.util.*

@Component
class OrderMapper {
    fun toEntity(request: OrderAddRequest): OrderEntity {
        return request.run {
            OrderEntity(
                pk = "ORDER#${UUID.randomUUID()}",
                sk = "TIMESTAMP#${System.currentTimeMillis()}",
                type = "ORDER",
                customerId = request.customerId,
                amount = request.getAmount(),
                orderItems = request.orderItem.map { req -> OrderItem(price = req.price, productId = req.productId) },
                expired = -1,
            )
        }
    }

    fun toCustomerOrderEntity(request: OrderAddRequest, orderId: String): CustomerOrderEntity {
        return CustomerOrderEntity(
            pk = "CUSTOMER#${request.customerId}",
            sk = "ORDER#${System.currentTimeMillis()}#${orderId}",
            type = "CUSTOMER_ORDER",
            amount = request.getAmount(), // // 결제금액
            title = "temp title", // // 주문제목 ex: 삼다수 외 1건
        )
    }

    fun toDto(entity: OrderEntity): OrderDto {
        return OrderDto(
            orderId = entity.pk.split("#")[1],
            customerId = entity.customerId,
            amount = entity.amount,
            expired = entity.expired,
            orderItems = entity.orderItems.map { item ->
                OrderItemDto(
                    price = item.price,
                    productId = item.productId
                )
            },
        )
    }

}
