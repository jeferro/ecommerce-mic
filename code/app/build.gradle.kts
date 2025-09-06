plugins {
    id("java-library")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.jeferro.plugins.avro-generator")
    id("com.jeferro.plugins.api-first-generator")
    id("jacoco")

}

dependencies {
    // General
    implementation(project(":lib-shared"))

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    annotationProcessor("org.mapstruct", "mapstruct-processor", Versions.mapstruct)

    testImplementation("com.approvaltests", "approvaltests", Versions.approval_tests)

    // Test
    testImplementation("org.springframework.boot", "spring-boot-starter-test")

    testImplementation("com.tngtech.archunit", "archunit", Versions.arch_unit)

    // Rest
    api("jakarta.validation", "jakarta.validation-api", Versions.jakarta_validation_api)
    implementation("org.openapitools", "jackson-databind-nullable", Versions.jackson_databind_nullable)
}

tasks.withType<Test> {
    useJUnitPlatform()
}


// Mapstruct
tasks.withType<JavaCompile> {
    options.compilerArgs = listOf(
        "-Amapstruct.unmappedTargetPolicy=ERROR",
    )
}


// Rest
apiFirstGenerator {
    basePackage = "com.jeferro.products.products.infrastructure.rest_api.dtos"
    specFile = file("${projectDir}/../../apis/rest/v1/openapi.yml")
    targetDir = file("${projectDir}/build/generated-resources/rest/v1")
}


// Avro
avroGenerator {
    schemaDir = file("${projectDir}/../../apis/avro/v1")
    targetDir = file("${projectDir}/build/generated/sources/avro/v1")
}


// Jacoco
jacoco {
    toolVersion = Versions.jacoco
}

tasks.withType<JacocoReport> {
    afterEvaluate {
        classDirectories.setFrom(
            files(classDirectories.files.map {
                fileTree(it).apply {
                    exclude(
                        "**/Application*",
                        "**/*Configuration*",
                        "**/dtos/**",
                        "**/daos/**",
                        "**/params/**",
                        "**/mappers/**"
                    )
                }
            })
        )
    }
}


