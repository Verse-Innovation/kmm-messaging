package io.verse.messaging.inapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform