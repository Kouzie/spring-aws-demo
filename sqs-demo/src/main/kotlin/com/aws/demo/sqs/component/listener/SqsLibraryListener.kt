package com.aws.demo.sqs.component.listener

import com.aws.demo.credential.logger
import com.aws.demo.sqs.component.SqsComponent
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.Message
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import java.util.concurrent.Executors

@Profile("library")
@Service
class SqsLibraryListener(
    private val sqsComponent: SqsComponent,
    private val sqsClient: SqsClient,
) {
    private var running = true // 작업 실행 상태 플래그

    companion object {
        const val maxNumberOfMessages = 10;
        const val waitTimeSeconds = 10;
    }

    /**
     * 과도한 흐름을 제어하기 위해 별도의 Thread Pool 적용
     * 하나의 scope 로 관리할 경우 과도한 메세지가 들어오면 가장 밭깥의 while 문만 돌고 delete Message 는 되지 않아 악순환반복됨으로 스코프를 나눔
     * 일종의 우선순위처럼 동작 가능
     * */
    private val pollMessageScope = CoroutineScope(
        Executors.newFixedThreadPool(1).asCoroutineDispatcher() + SupervisorJob()
    )
    private val processMessageScope = CoroutineScope(
        Executors.newFixedThreadPool(2).asCoroutineDispatcher() + SupervisorJob()
    )

    @PreDestroy
    fun stopPolling() {
        logger.info("Stopping SQS polling...")
        running = false
        pollMessageScope.cancel() // 모든 코루틴 중단
        processMessageScope.cancel() // 모든 코루틴 중단
    }

    @PostConstruct
    fun startPolling() {
        pollMessageScope.launch {
            while (running) { // 공통 루프에서 관리
                pollMessages()
            }
        }
    }

    private suspend fun pollMessages() {
        val receiveMessageRequest = ReceiveMessageRequest.builder()
            .queueUrl(sqsComponent.queueUrl)
            .maxNumberOfMessages(maxNumberOfMessages) // 한 번에 가져올 메시지 수
            .waitTimeSeconds(waitTimeSeconds) // Long polling 시간
            .build()

        val messages = sqsClient.receiveMessage(receiveMessageRequest).messages()
        logger.info("${messages.size} messages received")
        var jobs = messages.mapIndexed() { idx, message ->
            processMessageScope.launch {
                val workerName = Thread.currentThread().name
                processMessage(workerName, idx, message)
                logger.info("Polling cycle complete, workerId: $workerName")
            }
        }
    }

    private suspend fun processMessage(workerName: String, idx: Int, message: Message) {
        logger.info("Worker($workerName) processing index(${idx}) message: ${message.body()}")
        delay(10000L)
        // 메시지 처리 로직 추가
        deleteMessage(message)
        logger.info("Worker($workerName) finished index(${idx}) message: ${message.body()}")
    }

    private fun deleteMessage(message: Message) {
        sqsClient.deleteMessage(
            DeleteMessageRequest.builder()
                .queueUrl(sqsComponent.queueUrl)
                .receiptHandle(message.receiptHandle())
                .build()
        )
    }
}