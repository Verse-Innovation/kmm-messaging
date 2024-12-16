plugins {
    id("io.verse.kmm.library")
}

kotlin {
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "io.verse.messaging.inapp"
}

pomBuilder {
    description.set("Messaging inapp library")
}