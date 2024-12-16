plugins {
    id("io.verse.kmm.library")
}

apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":libraries:core-messaging"))
                api(libs.verse.latch)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.firebase.messaging)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.junit4)
                implementation(libs.mockito.core)
                implementation(libs.mockito.inline)
                implementation(libs.mockito.kotlin)
            }
        }
    }
}

android {
    namespace = "io.verse.messaging.cloud"
}

pomBuilder {
    description.set("Cloud Messaging library")
}