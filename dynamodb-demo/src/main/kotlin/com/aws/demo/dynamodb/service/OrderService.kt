package com.aws.demo.dynamodb.service

import com.aws.demo.dynamodb.config.DynamodbComponent
import com.aws.demo.dynamodb.dto.OrderDto
import com.aws.demo.dynamodb.dto.req.OrderAddRequest
import com.aws.demo.dynamodb.entity.CustomerOrderEntity
import com.aws.demo.dynamodb.entity.OrderEntity
import com.aws.demo.dynamodb.mapper.OrderMapper
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Service
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.*

@Service
class OrderService(
    val dynamodbComponent: DynamodbComponent,
    val enhancedClient: DynamoDbEnhancedClient,
    val mapper: OrderMapper,
) : ApplicationListener<ApplicationStartedEvent> {

    private lateinit var orderTable: DynamoDbTable<OrderEntity>
    private lateinit var customerOrderTable: DynamoDbTable<CustomerOrderEntity>

    companion object {
        const val MIN_UUID = "00000000-0000-0000-0000-000000000000"
        const val MAX_UUID = "ffffffff-ffff-ffff-ffff-ffffffffffff"
    }

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        orderTable = dynamodbComponent.generateDynamoDbTable(OrderEntity::class)
        customerOrderTable = dynamodbComponent.generateDynamoDbTable(CustomerOrderEntity::class)
    }

    fun create(request: OrderAddRequest): OrderDto {
        val entity = mapper.toEntity(request)
        orderTable.putItem(entity)
        return mapper.toDto(entity)
    }

    fun getById(orderId: String): OrderDto {
        val entity: OrderEntity = orderTable.getItem(Key.builder().partitionValue("ORDER#${orderId}").build())
        return mapper.toDto(entity)
    }

    fun getByCustomerId(customerId: String, beginDate: Long, endingDate: Long): List<OrderDto> {
        val key = Key.builder().partitionValue("CUSTOMER#$customerId").build()
        val request: QueryConditional = QueryConditional.keyEqualTo(key)

        var query = QueryConditional.sortBetween(
            Key.builder().partitionValue("CUSTOMER#$customerId").sortValue("ORDER#${beginDate}#${MIN_UUID}").build(),
            Key.builder().partitionValue("CUSTOMER#$customerId").sortValue("ORDER#${endingDate}#${MAX_UUID}").build()
        )
        QueryEnhancedRequest.builder()
            .build()
        orderTable.query(request)
        val result: PageIterable<CustomerOrderEntity> = customerOrderTable.query(query)
        val keysToGet: List<String> = result.items()
            .map { "ORDER#${it.sk.split("#")[2]}" }

        var readBatchBuilder = ReadBatch.builder(OrderEntity::class.java)
            .mappedTableResource(orderTable)
        for (orderId in keysToGet) {
            readBatchBuilder.addGetItem(Key.builder().partitionValue(orderId).sortValue(orderId).build())
        }
        var resultPages: BatchGetResultPageIterable =
            enhancedClient.batchGetItem { b -> b.readBatches(readBatchBuilder.build()) }
        return resultPages.resultsForTable(orderTable).map { mapper.toDto(it) }
    }
}