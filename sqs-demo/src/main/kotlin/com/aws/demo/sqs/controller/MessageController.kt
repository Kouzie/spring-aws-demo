package com.aws.demo.sqs.controller

import com.aws.demo.sqs.component.SqsPublisher
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/message")
class MessageController(
    private val sqsPublisher: SqsPublisher
) {
    @PostMapping
    fun sendMessage(@RequestBody payload: String): String {
        var messageId = sqsPublisher.sendMessage(payload)
        return messageId
    }
}