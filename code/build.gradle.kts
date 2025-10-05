plugins {
    id("java")
    id("com.diffplug.spotless") version Versions.spotless
    id("jacoco")
    id("org.springframework.boot") version Versions.spring_boot apply false
    id("io.spring.dependency-management") version Versions.spring_dependency_management apply false
    id("com.jeferro.plugins.api-first-generator") apply false
    id("com.jeferro.plugins.avro-generator") apply false
}


allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://packages.confluent.io/maven/")
        }
    }
}


subprojects {
    // Java
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    dependencies {
        implementation("org.apache.commons", "commons-lang3", Versions.commons_lang3)

        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        implementation("org.mapstruct", "mapstruct", Versions.mapstruct)
        annotationProcessor("org.mapstruct", "mapstruct-processor", Versions.mapstruct)
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    // Checkstyle
    apply(plugin = "com.diffplug.spotless")

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            googleJavaFormat(Versions.google_java_format)
            target("src/**/*.java")
        }
    }

    // Jacoco
    apply(plugin = "jacoco")

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
}

