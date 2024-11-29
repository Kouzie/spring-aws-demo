package com.aws.demo.s3.controller.dto

import java.time.Instant


data class S3ObjectDto(
    val key: String,
    val size: Long?,
    val lastModified: String?,
    val eTag: String?, // MD5 해시 값을 조합한 커스텀 태그
    val storageClass: String?,
)
