plugins {
    id("com.android.application")
    kotlin("android")
}

apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

repositories {
    google()
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
    maven("https://developer.huawei.com/repo/")

    maven {
        url = uri("https://maven.pkg.github.com/pavan2you/kmm-clean-architecture")

        credentials {
            username = extra["githubUser"] as? String
            password = extra["githubToken"] as? String
        }
    }
}

android {
    namespace = "io.verse.messaging.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "io.verse.messaging.android"
        minSdk = 21
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api(project(":libraries:android-messaging"))

    api(libs.tagd.android)
    api(libs.verse.app)
    api(libs.verse.system)
    api(libs.verse.latch)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.activity.compose)
}