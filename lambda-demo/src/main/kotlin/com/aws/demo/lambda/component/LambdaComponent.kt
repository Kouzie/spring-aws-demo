package com.aws.demo.lambda.component

import com.aws.demo.credential.logger
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.lambda.LambdaClient
import software.amazon.awssdk.services.lambda.model.*
import sun.nio.cs.UTF_8


@Component
class LambdaComponent(
    val lambdaClient: LambdaClient,
    val objectMapper: ObjectMapper = ObjectMapper(),
) {

    fun invokeLambda(functionName: String, payload: String, type: InvocationType): String {
        var request = InvokeRequest.builder()
            .invocationType(type)
            .functionName(functionName)
            .payload(SdkBytes.fromUtf8String(objectMapper.writeValueAsString(mapOf("body" to payload))))
            .build()
        var response: InvokeResponse = lambdaClient.invoke(request)
        if (response.statusCode() in listOf(HttpStatus.OK.value(), HttpStatus.ACCEPTED.value())) {
            return response.payload().asUtf8String()
        } else {
            throw RuntimeException("failed to invoke lambda:${functionName}, status:${response.statusCode()}")
        }
    }
}