package com.aws.demo.dynamodb.controller

import com.aws.demo.dynamodb.dto.CustomerDetailDto
import com.aws.demo.dynamodb.dto.CustomerDto
import com.aws.demo.dynamodb.dto.req.CustomerAddRequest
import com.aws.demo.dynamodb.service.CustomerService
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/customer")
class CustomerController(
    private val service: CustomerService
) {

    @PostMapping
    fun addCustomer(@RequestBody request: CustomerAddRequest): CustomerDetailDto {
        val dto: CustomerDetailDto = service.create(request)
        return dto
    }

    @GetMapping("/{customerId}")
    fun getCustomerById(@PathVariable customerId: String): CustomerDetailDto {
        return service.getById(customerId)
    }

    /**
     * @param beginDate: 생성 시간 시작(epoch seconds UTC)
     * @param endingDate: 생성 시간 종료(epoch seconds UTC)
     * */
    @GetMapping
    fun getCustomer(@RequestParam beginDate: Long, @RequestParam endingDate: Long): List<CustomerDto> {
        return service.getByCreateTimeBetween(Instant.ofEpochSecond(beginDate), Instant.ofEpochSecond(endingDate))
    }
}