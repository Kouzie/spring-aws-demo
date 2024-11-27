package com.aws.demo.dynamodb.service

import com.aws.demo.credential.logger
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

    /**
     * CustomerOrderEntity 와 OrderEntity 를 동시에 저장
     * */
    fun create(request: OrderAddRequest): OrderDto {
        val orderEntity = mapper.toEntity(request)
        val orderId = orderEntity.pk.split("#")[1]
        val customerOrderEntity = mapper.toCustomerOrderEntity(request, orderId)
        val transactWriteItemsEnhancedRequest = TransactWriteItemsEnhancedRequest.builder()
            .addPutItem(orderTable, orderEntity)
            .addPutItem(customerOrderTable, customerOrderEntity)
            .build()
        enhancedClient.transactWriteItems(transactWriteItemsEnhancedRequest)
        logger.info("order create success")
        return mapper.toDto(orderEntity)
    }

    fun getById(orderId: String): OrderDto {
        val entity: OrderEntity = orderTable.getItem(Key.builder().partitionValue("ORDER#${orderId}").build())
        return mapper.toDto(entity)
    }

    /**
     * 기간내 구매기록같은 query 는 batch read 가 불가능하여 따로따로 검색해야함
     * */
    fun getByCustomerId(customerId: String, beginDate: Long, endingDate: Long): List<OrderDto> {
        val key = Key.builder().partitionValue("CUSTOMER#${customerId}").build()
        val request: QueryConditional = QueryConditional.keyEqualTo(key)

        val query = QueryConditional.sortBetween(
            Key.builder().partitionValue("CUSTOMER#${customerId}").sortValue("ORDER#${beginDate}#${MIN_UUID}").build(),
            Key.builder().partitionValue("CUSTOMER#${customerId}").sortValue("ORDER#${endingDate}#${MAX_UUID}").build()
        )
        orderTable.query(request)
        val result: PageIterable<CustomerOrderEntity> = customerOrderTable.query(query)
        val keysToGet: List<String> = result.items()
            .map { "ORDER#${it.sk.split("#")[2]}" }

        val readBatchBuilder = ReadBatch.builder(OrderEntity::class.java)
            .mappedTableResource(orderTable)
        for (orderId in keysToGet) {
            readBatchBuilder.addGetItem(Key.builder().partitionValue(orderId).sortValue(orderId).build())
        }
        var resultPages: BatchGetResultPageIterable =
            enhancedClient.batchGetItem { b -> b.readBatches(readBatchBuilder.build()) }
        return resultPages.resultsForTable(orderTable).map { mapper.toDto(it) }
    }
}