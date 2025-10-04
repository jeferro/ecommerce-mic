plugins {
    java
    `java-gradle-plugin`
    id("org.openapi.generator") version "7.5.0"
}

repositories {
    mavenLocal()
    mavenCentral()
}


dependencies {
    implementation("org.apache.avro", "avro-compiler", "1.11.3")
    api("org.apache.commons", "commons-compress", "1.25.0")
    // Dependency necessary to solve conflict between avro and spring boot plugin

    implementation("org.openapitools", "openapi-generator-gradle-plugin", "7.5.0")
}

gradlePlugin {
    plugins {
        create("api-first-generator") {
            id = "com.jeferro.plugins.api-first-generator"
            implementationClass = "com.jeferro.plugins.api_first_generator.ApiFirstGeneratorPlugin"
        }

        create("avro-generator") {
            id = "com.jeferro.plugins.avro-generator"
            implementationClass = "com.jeferro.plugins.avro_generator.AvroGeneratorPlugin"
        }
    }
}
