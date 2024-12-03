package com.aws.demo.lambda.controller

import com.aws.demo.lambda.component.LambdaComponent
import com.aws.demo.lambda.controller.dto.LambdaRequestDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import software.amazon.awssdk.services.lambda.model.InvocationType

@RestController
@RequestMapping("/lambda")
class LambdaController(
    val lambdaComponent: LambdaComponent
) {
    /**
     * @param requestDto.type
     *  Event: 비동기
     *  RequestResponse: 동기
     *  DryRun: 테스트호출, 실행하지 않고 request 가 유효한지만 확인
     * */
    @PostMapping("/invoke")
    fun invokeLambda(@RequestBody requestDto: LambdaRequestDto): String {
        return lambdaComponent.invokeLambda(
            requestDto.functionName,
            requestDto.payload,
            InvocationType.fromValue(requestDto.type)
        )
    }
}