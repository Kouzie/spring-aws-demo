package com.aws.demo.sqs.component.listener

import com.aws.demo.sqs.component.SqsComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest

@Profile("library")
@Service
class SqsLibraryListener(
    private val sqsComponent: SqsComponent,
    private val sqsClient: SqsClient
) {

    suspend fun pollMessages() = withContext(Dispatchers.IO) {
        val receiveMessageRequest = ReceiveMessageRequest.builder()
            .queueUrl(sqsComponent.queueUrl)
            .maxNumberOfMessages(10) // 한 번에 가져올 메시지 수
            .waitTimeSeconds(20) // Long polling 시간
            .build()

        val messages = sqsClient.receiveMessage(receiveMessageRequest).messages()

        for (message in messages) {
            println("Received message: ${message.body()}")
            // 메시지 처리 로직 추가
            sqsClient.deleteMessage(DeleteMessageRequest.builder()
                .queueUrl(sqsComponent.queueUrl)
                .receiptHandle(message.receiptHandle())
                .build())
        }
    }

}