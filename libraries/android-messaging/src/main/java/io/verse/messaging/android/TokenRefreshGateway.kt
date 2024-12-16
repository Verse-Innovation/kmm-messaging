package io.verse.messaging.android

import io.tagd.arch.access.library
import io.tagd.arch.data.gateway.Gateway
import io.tagd.arch.domain.crosscutting.async.AsyncContext
import io.tagd.arch.domain.crosscutting.async.cancelAsync
import io.tagd.arch.domain.crosscutting.async.networkIO
import io.tagd.langx.Callback
import io.tagd.arch.scopable.library.dao
import io.tagd.core.BidirectionalDependentOn
import io.tagd.langx.IllegalAccessException
import io.tagd.langx.IllegalValueException
import io.tagd.langx.reflection.nativeTypeOf
import io.verse.latch.core.ExecutionException
import io.verse.latch.core.InterceptorGateway
import io.verse.latch.core.Latch
import io.verse.latch.core.Request
import io.verse.latch.core.RequestPayloadBody
import io.verse.latch.core.ResultContext
import io.verse.latch.core.newHttpsPostRequestBuilder
import io.verse.latch.core.newRequestPayloadBody
import io.verse.messaging.core.push.PushMessaging
import io.verse.messaging.core.push.PushMessagingConfig

interface TokenRefreshGatewaySpec: Gateway, AsyncContext,
    BidirectionalDependentOn<PushMessaging> {

    fun refresh(
        request: TokenRefreshRequest,
        success: Callback<TokenSet>? = null,
        failure: Callback<Throwable>? = null
    )
}

class TokenRefreshGateway: InterceptorGateway<
    TokenRefreshRequest,
    TokenRefreshResponse,
    TokenSet
>(), TokenRefreshGatewaySpec {

    private var dao: TokenRefreshDaoSpec? = null
    private val latch: Latch?
        get() = library<Latch>()
    private val pushConfig: PushMessagingConfig?
        get() = library<PushMessaging>()?.pushConfig

    override fun injectBidirectionalDependent(other: PushMessaging) {
        dao = other.dao()
    }

    override fun refresh(
        request: TokenRefreshRequest,
        success: Callback<TokenSet>?,
        failure: Callback<Throwable>?,
    ) {

        val requestBodyPayload = newLatchRequestPayload(request)
        networkIO {
            val latchRequest = pushConfig?.tokenRefreshUrl?.let {
                newHttpsPostRequest(it, requestBodyPayload)
            }
            latchRequest?.let {
                fire(it, success, failure)
            } ?: throw IllegalAccessException("failed to create request")
        }
    }

    override fun success(
        context: ResultContext<TokenRefreshRequest, TokenRefreshResponse>,
        result: TokenRefreshResponse
    ) {

        val requestContext = requestContext(context.identifier)
        requestContext?.let {
            update(
                request = it.request.payload.body?.content,
                success = it.success,
                failure = it.failure
            )
        }
    }

    override fun failure(exception: ExecutionException) {
        val requestContext = requestContext(exception.identifier)
        requestContext?.failure?.invoke(exception)
    }

    private fun update(
        request: TokenRefreshRequest?,
        success: Callback<TokenSet>?,
        failure: Callback<Throwable>?
    ) {

        request?.let {
            dao?.readEntityAsync(
                success = {
                    val updatedData = updateTokenSet(request, it.serviceProviderToTokenMap)
                    success?.invoke(updatedData)
                },
                failure = {
                    val updatedData = updateTokenSet(request)
                    success?.invoke(updatedData)
                }
            )
        } ?: failure?.invoke(IllegalValueException("request is null"))
    }


    private fun updateTokenSet(
        request: TokenRefreshRequest,
        cachedServiceProviderToTokenMap: Map<String, String> = mapOf(),
    ): TokenSet {

        val filableObject = newTokenSet(request, cachedServiceProviderToTokenMap)
        dao?.writeEntityAsync(filableObject)
        return filableObject
    }

    private fun newTokenSet(
        request: TokenRefreshRequest,
        cachedServiceProviderToTokenMap: Map<String, String>,
    ): TokenSet {

        val serviceProviderToTokenMap = cachedServiceProviderToTokenMap.toMutableMap()
        serviceProviderToTokenMap[request.serviceProvider] = request.token

        return TokenSet(
            serviceProviderToTokenMap = serviceProviderToTokenMap
        )
    }

    private fun newLatchRequestPayload(
        request: TokenRefreshRequest
    ): RequestPayloadBody<TokenRefreshRequest> {

        return newRequestPayloadBody(
            content = request,
            type = nativeTypeOf<TokenRefreshRequest>()
        )
    }

    private fun newHttpsPostRequest(
        url: String,
        request: RequestPayloadBody<TokenRefreshRequest>,
    ): Request<TokenRefreshRequest, TokenRefreshResponse>? {

        return latch?.newHttpsPostRequestBuilder(url, request, this)
            ?.build()
    }

    override fun release() {
        dao = null
        cancelAsync()
        super.release()
    }
}