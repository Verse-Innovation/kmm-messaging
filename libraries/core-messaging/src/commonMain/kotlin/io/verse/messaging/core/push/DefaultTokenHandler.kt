package io.verse.messaging.core.push

interface TokenHandlerSpec {
    fun onNewToken(token: String)
}

open class DefaultTokenHandler: TokenHandlerSpec {

    override fun onNewToken(token: String) {
        // no-op
    }
}