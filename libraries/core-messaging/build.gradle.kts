plugins {
    id("io.verse.kmm.library")
}

apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.tagd.arch)
                api(libs.verse.soa)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                api(libs.tagd.arch.test)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.core.ktx)
                api(libs.gson)
                api(libs.tagd.android)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.tagd.android)
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.mockito.android)
                implementation(libs.mockito.kotlin)
            }
        }
    }
}

android {
    namespace = "io.verse.messaging.core"
}

//artifactBuilder {
//    artifactIdName.set("messaging-core")
//}

pomBuilder {
    description.set("Messaging core library")
}