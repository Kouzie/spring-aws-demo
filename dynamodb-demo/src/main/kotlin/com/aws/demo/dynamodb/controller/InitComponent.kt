package com.aws.demo.dynamodb.controller

import com.aws.demo.dynamodb.dto.req.CustomerAddRequest
import com.aws.demo.dynamodb.dto.req.OrderAddRequest
import com.aws.demo.dynamodb.dto.req.OrderItemAddRequest
import com.aws.demo.dynamodb.dto.req.ProductAddRequest
import com.aws.demo.dynamodb.service.CustomerService
import com.aws.demo.dynamodb.service.OrderService
import com.aws.demo.dynamodb.service.ProductService
import com.thedeanda.lorem.LoremIpsum
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(Int.MAX_VALUE)
@Component
class InitComponent(
    val customerService: CustomerService,
    val orderService: OrderService,
    val productService: ProductService,
) : ApplicationListener<ApplicationStartedEvent> {


    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        val lorem = LoremIpsum.getInstance()
        // 1. Create random products
        val products = (1..10).map {
            productService.create(
                ProductAddRequest(
                    name = lorem.getWords(2, 4), // Random product name
                    price = (100..1000).random() // Random price between 100 and 1000
                )
            )
        }
        // 2. Create random customers
        val customers = (1..10).map {
            customerService.create(
                CustomerAddRequest(
                    username = lorem.getWords(10),
                    password = lorem.getWords(18),
                    nickname = lorem.name,
                    intro = lorem.getWords(10, 20),
                    age = (18..60).random(), // Random age between 18 and 60
                    email = lorem.getEmail(), // Random email
                )
            )
        }

        // 3. Create random orders for customers
        customers.forEach { customer ->
            val selectedProducts = products.shuffled().take((1..3).random()) // 1 to 3 random products
            orderService.create(
                OrderAddRequest(
                    customerId = customer.customerId,
                    orderItem = selectedProducts.map {
                        OrderItemAddRequest(price = it.price, productId = it.productId)
                    }
                )
            )
        }
    }

}