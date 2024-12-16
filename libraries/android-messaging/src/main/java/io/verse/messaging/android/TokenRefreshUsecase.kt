package io.verse.messaging.android

import io.tagd.arch.domain.crosscutting.async.AsyncContext
import io.tagd.arch.domain.crosscutting.async.cancelAsync
import io.tagd.arch.domain.crosscutting.async.compute
import io.tagd.arch.domain.usecase.Args
import io.tagd.arch.domain.usecase.LiveUseCase
import io.tagd.arch.scopable.library.repository
import io.tagd.core.BidirectionalDependentOn
import io.verse.messaging.core.push.PushMessaging

class TokenRefreshUsecase : LiveUseCase<TokenSet>(),
    AsyncContext,
    BidirectionalDependentOn<PushMessaging> {

    private var repository: TokenRefreshRepositorySpec? = null

    override fun injectBidirectionalDependent(other: PushMessaging) {
        repository = other.repository()
    }

    override fun trigger(args: Args) {
        compute {
            repository?.refresh(
                request = newTokenRefreshRequest(args),
                success = {
                    setValue(args, it)
                },
                failure = {
                    setError(args, it)
                }
            )
        }
    }

    private fun newTokenRefreshRequest(args: Args): TokenRefreshRequest {
        val token = args.get<String>(ARG_TOKEN)!!
        val serviceProvider = args.get<String>(ARG_SERVICE_PROVIDER)!!

        return TokenRefreshRequest(
            token = token,
            serviceProvider = serviceProvider
        )
    }

    override fun release() {
        cancelAsync()
        super.release()
    }

    companion object {
        const val ARG_TOKEN = "token"
        const val ARG_SERVICE_PROVIDER = "service_provider"
    }
}