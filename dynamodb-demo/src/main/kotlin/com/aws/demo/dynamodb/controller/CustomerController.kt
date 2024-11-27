package com.aws.demo.dynamodb.controller

import com.aws.demo.dynamodb.dto.CustomerDto
import com.aws.demo.dynamodb.dto.req.CustomerAddRequest
import com.aws.demo.dynamodb.service.CustomerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/customer")
class CustomerController(
    private val service: CustomerService
) {

    @PostMapping
    fun addCustomer(@RequestBody request: CustomerAddRequest): CustomerDto {
        val dto: CustomerDto = service.create(request)
        return dto
    }

    @GetMapping("/{customerId}")
    fun getCustomerById(@PathVariable customerId: String): CustomerDto {
        return service.getById(customerId)
    }

    @GetMapping
    fun getCustomer(@RequestParam beginDate: Long, @RequestParam endingDate: Long): List<CustomerDto> {
        return service.getByCreateTimeBetween(beginDate, endingDate)
    }
}