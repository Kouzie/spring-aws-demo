package com.aws.demo.dynamodb.mapper

import com.aws.demo.dynamodb.dto.CustomerDto
import com.aws.demo.dynamodb.dto.req.CustomerAddRequest
import com.aws.demo.dynamodb.entity.CustomerEntity
import org.springframework.stereotype.Component

@Component
class CustomerMapper {

    fun toEntity(request: CustomerAddRequest): CustomerEntity {
        return request.run {
            CustomerEntity(
                pk = "CUSTOMER#${customerId}",
                sk = "TIMESTAMP#${timestamp}",
                name = name,
                age = age,
                email = email,
            )
        }
    }

    fun toDto(entity: CustomerEntity): CustomerDto {
        TODO("Not yet implemented")
    }
}