pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://developer.huawei.com/repo/")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://developer.huawei.com/repo/")
    }
}

rootProject.name = "messaging"
include(":applications:the101:android")

include(":libraries:core-messaging")
include(":libraries:cloud-messaging")
include(":libraries:inapp-messaging")
include(":libraries:android-messaging")
include(":libraries:android-messaging:include-xiaomi-aar")
