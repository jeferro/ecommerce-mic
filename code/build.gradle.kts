plugins {
    id("java-library")
    id("checkstyle")
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
    apply(plugin = "java")
    apply(plugin = "checkstyle")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    checkstyle {
        toolVersion = "10.12"
        configFile = file("config/checkstyle/checkstyle-java-google-style.xml")
    }

    tasks.named("check") {
        dependsOn("checkstyleMain", "checkstyleTest")
    }
}

