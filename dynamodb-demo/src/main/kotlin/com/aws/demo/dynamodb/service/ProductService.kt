package com.aws.demo.dynamodb.service

import com.aws.demo.dynamodb.config.DynamodbComponent
import com.aws.demo.dynamodb.dto.req.ProductAddRequest
import com.aws.demo.dynamodb.dto.ProductDto
import com.aws.demo.dynamodb.entity.ProductEntity
import com.aws.demo.dynamodb.mapper.ProductMapper
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Service
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable

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
}