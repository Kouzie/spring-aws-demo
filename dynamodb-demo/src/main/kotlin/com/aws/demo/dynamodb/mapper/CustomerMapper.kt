package com.aws.demo.dynamodb.mapper

import com.aws.demo.dynamodb.dto.CustomerDetailDto
import com.aws.demo.dynamodb.dto.CustomerDto
import com.aws.demo.dynamodb.dto.req.CustomerAddRequest
import com.aws.demo.dynamodb.entity.CustomerEntity
import com.aws.demo.dynamodb.entity.CustomerInfoEntity
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class CustomerMapper {

    fun toEntity(request: CustomerAddRequest, customerId: String, now: Instant): CustomerEntity {
        return request.run {
            CustomerEntity(
                pk = "CUSTOMER#${customerId}",
                sk = "CUSTOMER#${customerId}",
                type = "CUSTOMER",
                username = username,
                password = password,
                expired = -1,
                created = now,
                updated = now
            )
        }
    }

    fun toCustomerInfoEntity(request: CustomerAddRequest, customerId: String, now: Instant): CustomerInfoEntity {
        return request.run {
            CustomerInfoEntity(
                pk = "CUSTOMER#${customerId}",
                sk = "CUSTOMER_INFO#${customerId}",
                type = "CUSTOMER_INFO",
                age = age,
                email = email,
                nickname = nickname,
                intro = intro,
                expired = -1,
                updated = now,
                created = now,
            )
        }
    }

    fun toDto(entity: CustomerEntity): CustomerDto {
        return entity.run {
            CustomerDto(
                customerId = pk.split("#")[1],
                username = username,
                expired = expired,
                updated = updated,
                created = created,
            )
        }
    }

    fun toDetailDto(entity: CustomerEntity, customerInfoEntity: CustomerInfoEntity): CustomerDetailDto {
        val dto = CustomerDetailDto(
            customerId = entity.pk.split("#")[1],
            username = entity.username,
            password = entity.password,
            nickname = customerInfoEntity.nickname,
            intro = customerInfoEntity.intro,
            age = customerInfoEntity.age,
            email = customerInfoEntity.email,
            expired = entity.expired,
            updated = entity.updated,
            created = entity.created,
        )
        return dto;
    }
}