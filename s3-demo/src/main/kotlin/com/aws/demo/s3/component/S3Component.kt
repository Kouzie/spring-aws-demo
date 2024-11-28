package com.aws.demo.s3.component

import com.aws.demo.s3.logger
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import java.nio.file.Paths


@Component
class S3Component(
    val s3Client: S3Client
) {
    @PostConstruct
    fun init() {
        val bucketName = "demo-bucket"

        if (!isBucketExists(bucketName)) {
            s3Client.createBucket(
                CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build()
            )
        } else {
            logger.info("Bucket '$bucketName' already exists.")
        }

        // 객체를 30일 후 삭제하도록 설정
        val prefixDeleteRule = LifecycleRule.builder()
            .id("PrefixDeleteRule")
            .filter(
                LifecycleRuleFilter.builder()
                    .prefix("temp/")
                    .build()
            ) // temp/로 시작하는 객체에 적용
            .expiration(
                LifecycleExpiration.builder()
                    .days(30) // 30일 후 만료
                    .build()
            )
            .status("Enabled")
            .build()

        val deleteRuleList: MutableList<LifecycleRule> = mutableListOf();
        // Tag 를 가진 객체 삭제하도록 설정
        for (i in listOf(10, 15, 30)) {
            deleteRuleList.add(
                LifecycleRule.builder()
                    .id("TagDeleteRule_${i}")
                    .filter(
                        LifecycleRuleFilter.builder()
                            .tag(Tag.builder().key("DELETE_TAG").value("DELETE_${i}").build())
                            .build()
                    )
                    .expiration(
                        LifecycleExpiration.builder()
                            .days(i) // 30일 후 만료
                            .build()
                    )
                    .status("Enabled")
                    .build()
            )
        }

        // 객체를 90일 후 Glacier로 이동하도록 설정
        val transitionRule: LifecycleRule = LifecycleRule.builder()
            .id("TransitionRule")
            .filter(LifecycleRuleFilter.builder().prefix("logs/").build()) // logs/로 시작하는 객체에 적용
            .transitions(
                Transition.builder()
                    .days(90) // 90일 후 Glacier로 전환
                    .storageClass(TransitionStorageClass.GLACIER)
                    .build()
            )
            .status("Enabled")
            .build()

        deleteRuleList.add(prefixDeleteRule)
        deleteRuleList.add(transitionRule)
        // Lifecycle 규칙 설정
        val configuration = BucketLifecycleConfiguration.builder()
            .rules(deleteRuleList)
            .build()

        // S3에 Lifecycle 설정 적용
        s3Client.putBucketLifecycleConfiguration(
            PutBucketLifecycleConfigurationRequest.builder()
                .bucket(bucketName)
                .lifecycleConfiguration(configuration)
                .build()
        )
    }

    private fun isBucketExists(bucketName: String): Boolean {
        return try {
            s3Client.headBucket(
                HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build()
            )
            true // 버킷이 존재하면 true 반환
        } catch (e: NoSuchBucketException) {
            false // 버킷이 없으면 false 반환
        }
    }

    fun uploadFile(bucketName: String, key: String, filePath: String) {
        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build(),
            Paths.get(filePath)
        )
    }

    fun listFiles(bucketName: String): List<S3Object> {
        return s3Client.listObjectsV2(ListObjectsV2Request.builder().bucket(bucketName).build())
            .contents()
    }
}