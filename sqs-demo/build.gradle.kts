dependencies {

    implementation("software.amazon.awssdk:sqs")
    implementation("io.awspring.cloud:spring-cloud-aws-starter")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs")
    // 코루틴 코어 라이브러리
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    // 코루틴을 Spring과 함께 사용하려면 추가
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")
}
