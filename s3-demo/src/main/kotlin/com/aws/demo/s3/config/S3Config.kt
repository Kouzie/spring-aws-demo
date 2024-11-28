package com.aws.demo.s3.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import java.net.URI

@Configuration
class S3Config {

    /**
     * 실행후 아래 명령어 수행
     * awslocal s3api get-bucket-lifecycle-configuration --bucket demo-bucket
     * */
    @Bean
    fun s3Client(credentialsProvider: AwsCredentialsProvider): S3Client {
        return S3Client.builder()
            .endpointOverride(URI.create("http://localhost:4566")) // LocalStack 엔드포인트
            .credentialsProvider(credentialsProvider)
            .region(Region.US_EAST_1)
            .serviceConfiguration(
                S3Configuration.builder()
                    .pathStyleAccessEnabled(true) // Path-Style Access 활성화, LocalStack 을 사용함으로 demo-bucket.localhost 와 같은 url 접근 불가
                    .build()
            )
            .build();

    }
}