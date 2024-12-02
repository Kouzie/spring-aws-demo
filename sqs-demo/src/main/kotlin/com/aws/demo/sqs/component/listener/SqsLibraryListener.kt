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
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest

@Profile("library")
@Service
class SqsLibraryListener(
    private val sqsComponent: SqsComponent,
    private val sqsClient: SqsClient,
) {
    @Value("\${sqs.polling.threads:5}")
    private lateinit var concurrentCount: String
    private var running = true // 작업 실행 상태 플래그

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob()) // 병렬 작업을 위한 스코프

    @PostConstruct
    fun startPolling() {
        val coroutineCount = concurrentCount.toInt()
        println("Starting $coroutineCount coroutine workers for SQS polling...")
        scope.launch {
            while (running) { // 공통 루프에서 관리
                val jobs = List(coroutineCount) { workerId ->
                    launch {
                        try {
                            pollMessages(workerId)
                        } catch (e: Exception) {
                            println("Worker-$workerId encountered an error: ${e.message}")
                            delay(1000L) // 에러 발생 시 잠시 대기
                        }
                    }
                }
                jobs.joinAll() // 모든 작업자 완료 대기
            }
        }
    }

    @PreDestroy
    fun stopPolling() {
        println("Stopping SQS polling...")
        running = false
        scope.cancel() // 모든 코루틴 중단
    }

    suspend fun pollMessages(workerId: Int) {
        logger.info("poll message start, workerId:${workerId}")
        val receiveMessageRequest = ReceiveMessageRequest.builder()
            .queueUrl(sqsComponent.queueUrl)
            .maxNumberOfMessages(10) // 한 번에 가져올 메시지 수
            .waitTimeSeconds(20) // Long polling 시간
            .build()

        val messages = sqsClient.receiveMessage(receiveMessageRequest).messages()

        for (message in messages) {
            println("Received message: ${message.body()}")
            // 메시지 처리 로직 추가
            sqsClient.deleteMessage(
                DeleteMessageRequest.builder()
                    .queueUrl(sqsComponent.queueUrl)
                    .receiptHandle(message.receiptHandle())
                    .build()
            )
        }
        logger.info("poll message end, workerI:${workerId}")
    }

}