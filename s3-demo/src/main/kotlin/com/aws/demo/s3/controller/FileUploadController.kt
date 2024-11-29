package com.aws.demo.s3.controller

import com.aws.demo.s3.component.S3Component
import com.aws.demo.s3.controller.dto.S3ObjectDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class FileUploadController(
    private val s3Component: S3Component
) {
    @GetMapping()
    fun getFileList(@RequestParam("prefix", defaultValue = "") prefix: String): List<S3ObjectDto> {
        return s3Component.listFiles(prefix)
            .map {
                S3ObjectDto(
                    key = it.key(),
                    size = it.size(),
                    lastModified = it.lastModified().toString(),
                    eTag = it.eTag(),
                    storageClass = it.key(),
                )
            }
    }

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile, prefix: String): String {
        if (file.isEmpty) {
            throw IllegalArgumentException("file is empty")
        }
        val fileUrl: String = s3Component.uploadFile(prefix, file)
        return fileUrl
    }

    @DeleteMapping()
    fun deleteFile(@RequestParam key: String) {
        s3Component.removeFile(key)
    }


}