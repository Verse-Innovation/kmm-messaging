package io.verse.messaging.android

import io.tagd.arch.data.repo.Repository
import io.tagd.arch.domain.crosscutting.async.AsyncContext
import io.tagd.langx.Callback
import io.tagd.arch.scopable.library.gateway
import io.tagd.core.BidirectionalDependentOn
import io.verse.messaging.core.push.PushMessaging

interface TokenRefreshRepositorySpec: Repository, AsyncContext,
    BidirectionalDependentOn<PushMessaging> {

    fun refresh(
        request: TokenRefreshRequest,
        success: Callback<TokenSet>? = null,
        failure: Callback<Throwable>? = null
    )
}

class TokenRefreshRepository: TokenRefreshRepositorySpec {

    private var gateway: TokenRefreshGatewaySpec? = null

    override fun injectBidirectionalDependent(other: PushMessaging) {
        gateway = other.gateway()
    }

    override fun refresh(
        request: TokenRefreshRequest,
        success: Callback<TokenSet>?,
        failure: Callback<Throwable>?,
    ) {

        gateway?.refresh(request, success, failure)
    }

    override fun release() {
        gateway = null
    }
}