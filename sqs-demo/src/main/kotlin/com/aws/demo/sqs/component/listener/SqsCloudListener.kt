package com.aws.demo.sqs.component.listener

import com.aws.demo.credential.logger
import io.awspring.cloud.sqs.annotation.SqsListener
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement
import org.springframework.context.annotation.Profile
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Profile("cloud")
@Service
class SqsCloudListener {

    /**
     * https://docs.awspring.io/spring-cloud-aws/docs/3.0.1/apidocs/io/awspring/cloud/sqs/listener/ContainerOptionsBuilder.html#maxMessagesPerPoll(int)
     * listener container bean 을 별도로 생성하고, 해당 bean 이 @SqsListener 메서드를 호출하는 방식
     * queueNames(values): queue 이름
     * factory: listener container bean 생성전략
     * id: listener container bean id 지정
     * maxConcurrentMessages: listener bean 내부 워커스레드 개수 지정, default 10,
     * pollTimeoutSeconds: SQS Long Polling 시간(초 단위) default 10
     * maxMessagesPerPoll: polling 당 가져올 메세지 수 default 10
     * messageVisibilitySeconds: 메세지 처리 보장시간 default 30, 시간내에 메세지를 확인처리하지 않으면 다시 소비가능한 상태로 돌아간다.
     * acknowledgementMode: 메세지 확인후 삭제 모드 default ON_SUCCESS
     *  - ON_SUCCESS: 오류 없이 메서드 완료시 확인 및 삭제처리
     *  - ALWAYS: 오류 상관 없이 확인 및 삭제처리
     *  - MANUAL: 수동 확인 및 삭제처리
     * */
    @SqsListener(
        queueNames = ["\${demo.aws.sqs.queue-name}"],
        maxConcurrentMessages = "10",
        pollTimeoutSeconds = "20",
        maxMessagesPerPoll = "5",
        messageVisibilitySeconds = "10",
        acknowledgementMode = "MANUAL",
    )
    fun receiveMessage(
        message: String,
        @Header("demoAttr") demoAttrValue: String,
        ack: Acknowledgement,
    ) {
        logger.info("header: ${demoAttrValue}, payload:${message}")
        ack.acknowledge()
    }

}