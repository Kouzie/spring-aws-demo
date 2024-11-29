package com.aws.demo.s3.component

import com.aws.demo.credential.logger
import com.aws.demo.s3.config.S3Config
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*


@Component
class S3Component(
    val bucketName: String = "demo-bucket",
    val s3Client: S3Client,
) {

    private fun isBucketExists(): Boolean {
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

    @PostConstruct
    fun init() {
        if (!isBucketExists()) {
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
            .expiration(LifecycleExpiration.builder().days(30).build()) // 30일 후 만료
            .status(ExpirationStatus.ENABLED)
            .build()

        val lifecycleRules: MutableList<LifecycleRule> = mutableListOf();
        // Tag 를 가진 객체 삭제하도록 설정
        for (i in listOf(1, 10, 15, 30)) {
            lifecycleRules.add(
                LifecycleRule.builder()
                    .id("TagDeleteRule_${i}")
                    .filter(
                        LifecycleRuleFilter.builder()
                            .tag(Tag.builder().key("DELETE_TAG").value("DELETE_${i}").build())
                            .build()
                    )
                    .expiration(LifecycleExpiration.builder().days(i).build()) // i일 후 만료
                    .status(ExpirationStatus.ENABLED)
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
            .status(ExpirationStatus.ENABLED)
            .build()

        lifecycleRules.add(prefixDeleteRule)
        lifecycleRules.add(transitionRule)
        // Lifecycle 규칙 설정
        val configuration = BucketLifecycleConfiguration.builder()
            .rules(lifecycleRules)
            .build()

        // S3에 Lifecycle 설정 적용
        s3Client.putBucketLifecycleConfiguration(
            PutBucketLifecycleConfigurationRequest.builder()
                .bucket(bucketName)
                .lifecycleConfiguration(configuration)
                .build()
        )
    }

    fun uploadFile(prefix: String, file: MultipartFile): String {
        val fileName = UUID.randomUUID().toString()
        val key = "${prefix}/${fileName}"
        logger.info(key)
        val tempFile = Files.createTempFile("upload-", fileName) // tempfile prefix 에는 '/' 입력 불가
        file.inputStream.use { inputStream -> Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING) }
        val result: PutObjectResponse = s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .tagging(
                    Tagging.builder().tagSet(
                        Tag.builder().key("DELETE_TAG").value("DELETE_1").build()
                    ).build()
                )
                .build(),
            tempFile
        )
        Files.deleteIfExists(tempFile)
        return "${S3Config.S3_URL}/${bucketName}/${key}"
    }

    fun removeFile(key: String) {
        val result: DeleteObjectResponse = s3Client.deleteObject(
            DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build(),
        )
    }

    fun listFiles(prefix: String): List<S3Object> {
        return s3Client.listObjectsV2(
            ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build()
        )
            .contents()
    }
}