package com.aws.demo.dynamodb.service

import com.aws.demo.dynamodb.config.DynamodbComponent
import com.aws.demo.dynamodb.dto.req.CustomerAddRequest
import com.aws.demo.dynamodb.dto.CustomerDto
import com.aws.demo.dynamodb.entity.CustomerEntity
import com.aws.demo.dynamodb.mapper.CustomerMapper
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Service
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Expression
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.time.OffsetDateTime

@Service
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

    fun getById(customerId: String): CustomerDto {
        val pk = "CUSTOMER#${customerId}"
        var key: Key = Key.builder().partitionValue(pk).build()
        var entity: CustomerEntity = table.getItem(key)
        return mapper.toDto(entity)
    }

    /**
     * 전체조회 발생
     * partition 을 위한 key 를 기준으로 검색할 수 없음
     * 아래와 같은 GSI 를 만들어야 하는데 싱글테이블에서 GSI 를 만드는것은 부담이다.
     * PK#ALL, TIMESTAMP#{timestamp}
     *
     * */
    fun getByCreateTimeBetween(
        startTime: Long,
        endTime: Long
    ): List<CustomerDto> {
        val scanRequest = ScanEnhancedRequest.builder()
            .filterExpression(
                Expression.builder()
                    .expression("created BETWEEN :startTime AND :endTime")
                    .expressionValues(
                        mapOf(
                            ":startTime" to AttributeValue.builder().s("TIMESTAMP#$startTime").build(),
                            ":endTime" to AttributeValue.builder().s("TIMESTAMP#$endTime").build()
                        )
                    )
                    .build()
            )
            .build()
        val result = table.scan(scanRequest)
        return result.items().map { mapper.toDto(it) }
    }
}