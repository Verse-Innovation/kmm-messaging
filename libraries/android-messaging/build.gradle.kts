import java.io.FileInputStream
import java.util.Properties

plugins {
    id("io.verse.android.library")
    id("com.google.gms.google-services")
    id("com.huawei.agconnect")
}

apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

repositories {
    maven("https://developer.huawei.com/repo/")
}

android {
    namespace = "io.verse.messaging.android"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    val keystorePropertiesFile = rootProject.file("keystore.properties")
    val keystoreProperties = Properties()
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))

    signingConfigs {
        getByName("debug") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storefile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storefile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    api(project(":libraries:cloud-messaging"))
    api(project(":libraries:android-messaging:include-xiaomi-aar"))

    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.huawei.push.service)
    implementation(libs.verse.storage)

    testImplementation(libs.junit4)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.tagd.android.test)

    androidTestImplementation(libs.androidx.test.junit.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.tagd.android.test)  {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
    }
}