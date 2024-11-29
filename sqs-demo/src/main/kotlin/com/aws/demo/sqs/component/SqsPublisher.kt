package com.aws.demo.sqs.component

import com.aws.demo.credential.logger
import org.springframework.stereotype.Service
import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue
import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeNameForSends
import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeValue
import software.amazon.awssdk.services.sqs.model.SendMessageRequest

@Service
class SqsPublisher(
    private val sqsClient: SqsClient,
    private val sqsComponent: SqsComponent,
) {
    fun sendMessage(payload: String): String {
        val message = SendMessageRequest.builder()
            // .messageDeduplicationId() // FIFO 에서만 사용 가능
            // .messageGroupId() // FIFO 에서만 사용 가능, 메시지 중복 제거를 위한 고유 식별자
            .queueUrl(sqsComponent.queueUrl)
            .delaySeconds(0) // default 0, 0 ~ 900
            .messageBody(payload) // 0 ~ 256kb, FIFO 에선 사용 불가능
            .messageAttributes( // 메시지 사용자 정의 속성을 설정
                mapOf(
                    "demoAttr" to MessageAttributeValue.builder()
                        .dataType("String") //  String, Number, Binary
                        .stringValue("demoAttrValue")
                        .build()
                )
            )
            .messageSystemAttributes( // 메시지 시스템 속성을 설정, X-Ray 통합에 유일하게 사용되고 있음
                mapOf(
                    MessageSystemAttributeNameForSends.AWS_TRACE_HEADER to MessageSystemAttributeValue.builder()
                        .dataType("String")
                        .stringValue("Root=1-5759e988-bd862e3fe1be46a994272793")
                        .build()
                )
            )
            .overrideConfiguration(
                AwsRequestOverrideConfiguration.builder()
                    .putHeader("demoKey", "demoValue")
                    .build()
            )
            .build()
        var response = sqsClient.sendMessage(message)
        logger.info("sqs send message success, ${response.messageId()}")
        return response.messageId();
    }
}