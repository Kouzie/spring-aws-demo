package com.aws.demo.dynamodb

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
import kotlin.reflect.KClass

@Component
class DynamodbComponent(
    val dynamoDbClient: DynamoDbClient,
    val dynamoDbEnhancedClient: DynamoDbEnhancedClient
) {
    @Value("\${ddb.table-name}")
    private lateinit var tableName: String

    /**
     * single table 전략
     * pk sk 필수
     * expired 가 있는 데이터는 자동 삭제됨
     * */
    @PostConstruct
    private fun createTableIfNotExists() {
        val tableNames = dynamoDbClient.listTables().tableNames()
        if (!tableNames.contains(tableName)) {
            dynamoDbClient.createTable {
                it.tableName(tableName)
                it.keySchema(
                    KeySchemaElement.builder().attributeName("pk").keyType(KeyType.HASH).build(),
                    KeySchemaElement.builder().attributeName("sk").keyType(KeyType.RANGE).build()
                )
                it.attributeDefinitions(
                    AttributeDefinition.builder().attributeName("pk").attributeType(ScalarAttributeType.S).build(),
                    AttributeDefinition.builder().attributeName("sk").attributeType(ScalarAttributeType.S).build()
                )
                it.provisionedThroughput {
                    it.readCapacityUnits(5)
                    it.writeCapacityUnits(5)
                }
            }
            println("Created table $tableName")
        }
        val describeTTLRequest = DescribeTimeToLiveRequest.builder()
            .tableName(tableName)
            .build()
        val describeTTLResponse: DescribeTimeToLiveResponse = dynamoDbClient.describeTimeToLive(describeTTLRequest)
        val currentTTLStatus: TimeToLiveStatus = describeTTLResponse.timeToLiveDescription().timeToLiveStatus()

        if (currentTTLStatus == TimeToLiveStatus.DISABLED) {
            // TTL 설정
            val ttlAttributeName = "expired"
            val ttlReq = UpdateTimeToLiveRequest.builder()
                .tableName(tableName)
                .timeToLiveSpecification(
                    TimeToLiveSpecification.builder()
                        .attributeName(ttlAttributeName)
                        .enabled(true)
                        .build()
                )
                .build()
            dynamoDbClient.updateTimeToLive(ttlReq)
            println("Updated TTL Config $tableName $ttlAttributeName")
        }
    }

    fun <T : Any> generateDynamoDbTable(entityClass: KClass<T>): DynamoDbTable<T> {
        return dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(entityClass.java))
    }
}