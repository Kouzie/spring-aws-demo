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
    @PostMapping("/invoke")
    fun invokeLambda(@RequestBody requestDto: LambdaRequestDto): String {
        return lambdaComponent.invokeLambda(
            requestDto.functionName,
            requestDto.payload,
            InvocationType.fromValue(requestDto.type)
        )
    }
}