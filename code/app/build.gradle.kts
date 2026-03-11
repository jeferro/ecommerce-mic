import com.jeferro.plugins.api_first_generator.ApiFirstGeneratorSpec

plugins {
    id("java-library")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.jeferro.plugins.avro-generator")
    id("com.jeferro.plugins.api-first-generator")
    id("info.solidsoft.pitest")
}

dependencies {
    // General
    implementation(project(":lib-shared"))

    testImplementation("com.approvaltests", "approvaltests", Versions.approval_tests)

    // Test
    testImplementation("org.springframework.boot", "spring-boot-starter-test")

    testImplementation("com.tngtech.archunit", "archunit", Versions.arch_unit)

    // Rest
    api("jakarta.validation", "jakarta.validation-api", Versions.jakarta_validation_api)
    implementation("org.openapitools", "jackson-databind-nullable", Versions.jackson_databind_nullable)
}

// Java
tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}


// Mapstruct
tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Amapstruct.unmappedTargetPolicy=ERROR")
}

tasks.withType<Checkstyle> {
    exclude("**/generated/**", "**/generated-resources/**", "**/build/**")
}


// Mutation Test
pitest {
    junit5PluginVersion = Versions.pitest_junit5_plugin
    targetClasses = setOf("com.jeferro.ecommerce.*")
    excludedClasses = setOf(
        "com.jeferro.ecommerce.**.*Application*",
        "com.jeferro.ecommerce.**.*Configuration*",
        "com.jeferro.ecommerce.**.dtos.*",
        "com.jeferro.ecommerce.**.daos.*",
        "com.jeferro.ecommerce.**.params.*",
        "com.jeferro.ecommerce.**.mappers.*"
    )
    outputFormats = setOf("HTML", "XML")
    timestampedReports = false
    threads = Runtime.getRuntime().availableProcessors()
}


// Rest
apiFirstGenerator {
    buildDir = file("${projectDir}/build/generated-resources/")
    specs = listOf(
        ApiFirstGeneratorSpec().apply {
            name = "users-v1"
            basePackage = "com.jeferro.ecommerce.users.users.infrastructure.rest_api.v1"
            specFile = file("${projectDir}/../../apis/rest/v1/users/users.yml")
        },
        ApiFirstGeneratorSpec().apply {
            name = "products-v1"
            basePackage = "com.jeferro.ecommerce.products.product_versions.infrastructure.rest_api.v1"
            specFile = file("${projectDir}/../../apis/rest/v1/product_versions/product_versions.yml")
        },
        ApiFirstGeneratorSpec().apply {
            name = "reviews-v1"
            basePackage = "com.jeferro.ecommerce.products.reviews.infrastructure.rest_api.v1"
            specFile = file("${projectDir}/../../apis/rest/v1/reviews/reviews.yml")
        }
    )
}


// Avro
avroGenerator {
    schemaDir = file("${projectDir}/../../apis/avro/v1")
    targetDir = file("${projectDir}/build/generated/sources/avro")
}





