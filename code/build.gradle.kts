plugins {
    id("java-library")
    id("checkstyle")
    id("jacoco")
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
    // Java
    tasks.withType<Test> {
        useJUnitPlatform()
    }

    // Checkstyle
    apply(plugin = "checkstyle")

    checkstyle {
        toolVersion = Versions.checkstyle
        configFile = file("${rootDir}config/checkstyle/checkstyle-java-google-style.xml")
    }

    tasks.named("check") {
        dependsOn("checkstyleMain", "checkstyleTest")
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

