package com.aws.demo.dynamodb.service

import com.aws.demo.dynamodb.DynamodbComponent
import com.aws.demo.dynamodb.dto.req.ProductAddRequest
import com.aws.demo.dynamodb.dto.ProductDto
import com.aws.demo.dynamodb.entity.ProductEntity
import com.aws.demo.dynamodb.mapper.ProductMapper
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Service
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import java.time.OffsetDateTime

@Service
class ProductService(
    private val dynamodbComponent: DynamodbComponent,
    private val mapper: ProductMapper,
) : ApplicationListener<ApplicationStartedEvent>
{
    private lateinit var table: DynamoDbTable<ProductEntity>

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        table = dynamodbComponent.generateDynamoDbTable(ProductEntity::class)
    }

    fun create(request: ProductAddRequest): ProductDto {
        val entity = mapper.toEntity(request)
        table.putItem(entity)
        return mapper.toDto(entity)
    }

    fun getByCreateTimeBetween(
        productId: String,
        startTime: OffsetDateTime,
        endTime: OffsetDateTime
    ): List<ProductDto> {
        val pk = "PRODUCT#${productId}"
        val startSortKey: Key =
            Key.builder().partitionValue(pk).sortValue("TIMESTAMP#${startTime.toInstant().toEpochMilli()}").build()
        val endSortKey: Key =
            Key.builder().partitionValue(pk).sortValue("TIMESTAMP#${endTime.toInstant().toEpochMilli()}").build()
        var result: PageIterable<ProductEntity> =
            table.query(QueryConditional.sortBetween(startSortKey, endSortKey))
        return result.items().map { mapper.toDto(it) }
    }
}