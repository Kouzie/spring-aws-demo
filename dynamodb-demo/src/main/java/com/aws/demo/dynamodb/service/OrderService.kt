package com.aws.demo.dynamodb.service

import com.aws.demo.dynamodb.DynamodbComponent
import com.aws.demo.dynamodb.dto.OrderDto
import com.aws.demo.dynamodb.dto.req.OrderAddRequest
import com.aws.demo.dynamodb.entity.OrderEntity
import com.aws.demo.dynamodb.mapper.OrderMapper
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Service
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key

@Service
class OrderService(
    val dynamodbComponent: DynamodbComponent,
    val mapper: OrderMapper,
) : ApplicationListener<ApplicationStartedEvent> {

    private lateinit var table: DynamoDbTable<OrderEntity>

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        table = dynamodbComponent.generateDynamoDbTable(OrderEntity::class)
    }

    fun create(request: OrderAddRequest): OrderDto {
        val entity = mapper.toEntity(request)
        table.putItem(entity)
        return mapper.toDto(entity)
    }

    fun getById(orderId: String): OrderDto {
        val entity = table.getItem(Key.builder().partitionValue("ORDER#${orderId}").build())
        return mapper.toDto(entity)
    }

}