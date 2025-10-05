import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java-library")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    // Spring
    implementation("org.springframework.boot", "spring-boot-starter")
    api("org.springframework.boot", "spring-boot-starter-security")

    // Rest
    api("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-actuator")

    implementation("com.auth0", "java-jwt", Versions.jwt)

    // Mongo
    api("org.springframework.boot", "spring-boot-starter-data-mongodb")

    // Kafka
    api("org.springframework.kafka", "spring-kafka", Versions.spring_kafka)

    api("org.apache.avro", "avro", Versions.avro)
    api("org.apache.commons", "commons-compress", "1.25.0")
    implementation("io.confluent", "kafka-avro-serializer", Versions.kafka_avro_serializer)
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
