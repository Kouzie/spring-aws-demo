package com.aws.demo.dynamodb.service

import com.aws.demo.dynamodb.DynamodbComponent
import com.aws.demo.dynamodb.dto.req.CustomerAddRequest
import com.aws.demo.dynamodb.dto.CustomerDto
import com.aws.demo.dynamodb.entity.CustomerEntity
import com.aws.demo.dynamodb.mapper.CustomerMapper
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import java.time.OffsetDateTime

class CustomerService(
    private val dynamodbComponent: DynamodbComponent,
    private val mapper: CustomerMapper,
) : ApplicationListener<ApplicationStartedEvent> {
    private lateinit var table: DynamoDbTable<CustomerEntity>

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        table = dynamodbComponent.generateDynamoDbTable(CustomerEntity::class)
    }

    fun create(request: CustomerAddRequest): CustomerDto {
        val entity = mapper.toEntity(request)
        table.putItem(entity)
        return mapper.toDto(entity)
    }

    fun getByCreateTimeBetween(
        customerId: String,
        startTime: OffsetDateTime,
        endTime: OffsetDateTime
    ): List<CustomerDto> {
        val pk = "CUSTOMER#${customerId}"
        val startSortKey: Key =
            Key.builder().partitionValue(pk).sortValue("TIMESTAMP#${startTime.toInstant().toEpochMilli()}").build()
        val endSortKey: Key =
            Key.builder().partitionValue(pk).sortValue("TIMESTAMP#${endTime.toInstant().toEpochMilli()}").build()
        val result: PageIterable<CustomerEntity> =
            table.query(QueryConditional.sortBetween(startSortKey, endSortKey))
        return result.items().map { mapper.toDto(it) }
    }
}