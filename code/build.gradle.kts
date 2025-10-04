plugins {
    id("java-library")
    id("org.springframework.boot") version Versions.spring_boot apply false
    id("io.spring.dependency-management") version Versions.spring_dependency_management apply false
    id("com.jeferro.plugins.api-first-generator") apply false
    id("com.jeferro.plugins.avro-generator") apply false
}


allprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://packages.confluent.io/maven/")
        }
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }
}


subprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

