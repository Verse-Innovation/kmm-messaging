package io.verse.messaging.android

import io.tagd.arch.domain.crosscutting.codec.SerializedName

data class TokenRefreshRequest(
    @SerializedName("token")
    val token: String,
    @SerializedName("serviceProvider")
    val serviceProvider: String,
)

data class TokenRefreshResponse(
    val count: Int
)