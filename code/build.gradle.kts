plugins {
    id("java")
    id("jacoco")
    id("org.springframework.boot") version Versions.spring_boot apply false
    id("io.spring.dependency-management") version Versions.spring_dependency_management apply false
    id("com.jeferro.plugins.api-first-generator") apply false
    id("com.jeferro.plugins.avro-generator") apply false
    id("info.solidsoft.pitest") version Versions.pitest_plugin apply false
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
            languageVersion = JavaLanguageVersion.of(Versions.java)
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

    // Jacoco
    apply(plugin = "jacoco")

    jacoco {
        toolVersion = Versions.jacoco
    }

    tasks.withType<JacocoReport> {
        afterEvaluate {
            classDirectories.setFrom(
                files(classDirectories.files.map {
                    fileTree().apply {
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

