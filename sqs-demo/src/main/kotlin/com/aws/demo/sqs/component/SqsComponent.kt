package com.aws.demo.sqs.component

import com.aws.demo.credential.logger
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.*

@Component
class SqsComponent(
    private val sqsClient: SqsClient
) {

    @Value("\${demo.aws.sqs.queue-name}")
    lateinit var queueName: String
    lateinit var queueUrl: String

    @PostConstruct
    fun init() {
        try {
            val response: GetQueueUrlResponse = sqsClient.getQueueUrl(
                GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build()
            )
            logger.info("Queue already exists: $queueName");
            queueUrl = response.queueUrl()
        } catch (e: QueueDoesNotExistException) {
            logger.info("queue does not exist: $queueName")
            val createQueueRequest = CreateQueueRequest.builder()
                .queueName(queueName)
                .build()

            val response: CreateQueueResponse = sqsClient.createQueue(createQueueRequest);
            queueUrl = response.queueUrl()
        }
        // connect queue url success, http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/demo-queue
        // 000000000000: account id
        logger.info("connect queue url success, $queueUrl")
    }

    private fun deleteMessage(receiptHandle: String) {
        val deleteMessageRequest = DeleteMessageRequest.builder()
            .queueUrl(queueUrl)
            .receiptHandle(receiptHandle)
            .build()

        sqsClient.deleteMessage(deleteMessageRequest)
        println("Message deleted.")
    }
}