package com.aws.demo.dynamodb.service

import com.aws.demo.credential.logger
import com.aws.demo.dynamodb.config.DynamodbComponent
import com.aws.demo.dynamodb.dto.CustomerDetailDto
import com.aws.demo.dynamodb.dto.CustomerDto
import com.aws.demo.dynamodb.dto.req.CustomerAddRequest
import com.aws.demo.dynamodb.entity.CustomerEntity
import com.aws.demo.dynamodb.entity.CustomerInfoEntity
import com.aws.demo.dynamodb.entity.CustomerOrderEntity
import com.aws.demo.dynamodb.mapper.CustomerMapper
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Service
import software.amazon.awssdk.enhanced.dynamodb.*
import software.amazon.awssdk.enhanced.dynamodb.model.*
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.time.Instant
import java.util.*
import java.util.function.Consumer


@Service
class CustomerService(
    private val dynamodbComponent: DynamodbComponent,
    private val enhancedClient: DynamoDbEnhancedClient,
    private val mapper: CustomerMapper,
) : ApplicationListener<ApplicationStartedEvent> {
    private lateinit var customerTable: DynamoDbTable<CustomerEntity>
    private lateinit var customerOrderTable: DynamoDbTable<CustomerOrderEntity>
    private lateinit var customerInfoTable: DynamoDbTable<CustomerInfoEntity>

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        customerTable = dynamodbComponent.generateDynamoDbTable(CustomerEntity::class)
        customerOrderTable = dynamodbComponent.generateDynamoDbTable(CustomerOrderEntity::class)
        customerInfoTable = dynamodbComponent.generateDynamoDbTable(CustomerInfoEntity::class)
    }

    fun create(request: CustomerAddRequest): CustomerDetailDto {
        val customerId = UUID.randomUUID().toString()
        val now = Instant.now()
        val customerEntity = mapper.toEntity(request, customerId, now)
        val customerInfoEntity = mapper.toCustomerInfoEntity(request, customerId, now)
        enhancedClient.transactWriteItems(
            TransactWriteItemsEnhancedRequest.builder()
                .addPutItem(customerTable, customerEntity)
                .addPutItem(customerInfoTable, customerInfoEntity)
                .build()
        )

        return mapper.toDetailDto(customerEntity, customerInfoEntity)
    }

    fun getById(customerId: String): CustomerDetailDto {
        val pk = "CUSTOMER#${customerId}"
        val infoSk = "CUSTOMER_INFO#${customerId}"
        val customerRead: ReadBatch = ReadBatch.builder(CustomerEntity::class.java)
            .mappedTableResource(customerTable)
            .addGetItem(Key.builder().partitionValue(pk).sortValue(pk).build())
            .build()

        val customerInfoRead: ReadBatch = ReadBatch.builder(CustomerInfoEntity::class.java)
            .mappedTableResource(customerInfoTable)
            .addGetItem(Key.builder().partitionValue(pk).sortValue(infoSk).build())
            .build()

        val resultPages: BatchGetResultPageIterable = enhancedClient.batchGetItem(
            BatchGetItemEnhancedRequest.builder()
                .readBatches(customerRead, customerInfoRead)
                .build()
        )

        // results 를 반복하면 일치하는 값 필터링
        val entity: CustomerEntity = resultPages.resultsForTable(customerTable)
            // .map { logger.info(it.toString()); it }
            .first { it.type == "CUSTOMER" }
        val customerInfoEntity: CustomerInfoEntity = resultPages.resultsForTable(customerInfoTable)
            .first { it.type == "CUSTOMER_INFO" }
        return mapper.toDetailDto(entity, customerInfoEntity)
    }

    /**
     * 전체조회 발생
     * partition 을 위한 key 를 기준으로 검색할 수 없음
     * 아래와 같은 GSI 를 만들어야 하는데 싱글테이블에서 GSI 를 만드는것은 부담이다.
     * PK#ALL, TIMESTAMP#{timestamp}
     *
     * */
    fun getByCreateTimeBetween(startTime: Instant, endTime: Instant): List<CustomerDto> {
        val scanRequest = ScanEnhancedRequest.builder()
            .filterExpression(
                Expression.builder()
                    .expression("created BETWEEN :startTime AND :endTime")
                    .expressionValues(
                        mapOf(
                            ":startTime" to AttributeValue.builder().s(startTime.toString()).build(),
                            ":endTime" to AttributeValue.builder().s(endTime.toString()).build()
                        )
                    )
                    .build()
            )
            .build()
        val result: PageIterable<CustomerEntity> = customerTable.scan(scanRequest)
        return result.items()
            .filter { it.type == "CUSTOMER"}
            .map { mapper.toDto(it) }
    }
}