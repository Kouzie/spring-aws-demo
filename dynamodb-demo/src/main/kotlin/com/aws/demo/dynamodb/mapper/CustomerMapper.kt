package com.aws.demo.dynamodb.mapper

import com.aws.demo.dynamodb.dto.CustomerDto
import com.aws.demo.dynamodb.dto.req.CustomerAddRequest
import com.aws.demo.dynamodb.entity.CustomerEntity
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class CustomerMapper {

    fun toEntity(request: CustomerAddRequest): CustomerEntity {
        return request.run {
            var now = Instant.now()
            CustomerEntity(
                pk = "CUSTOMER#${UUID.randomUUID()}",
                sk = "CUSTOMER#${UUID.randomUUID()}",
                type = "CUSTOMER",
                name = name,
                age = age,
                email = email,
                expired = -1,
                created = now,
                updated = now
            )
        }
    }

    fun toDto(entity: CustomerEntity): CustomerDto {
        val dto = CustomerDto(
            customerId = entity.pk.split("#")[1],
            name = entity.name,
            age = entity.age,
            email = entity.email,
            expired = entity.expired,
            created = entity.created.toString(),
            updated = entity.updated.toString(),
        )
        return dto;
    }
}