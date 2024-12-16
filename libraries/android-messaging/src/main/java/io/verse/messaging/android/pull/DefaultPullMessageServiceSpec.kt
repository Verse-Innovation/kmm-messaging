package io.verse.messaging.android.pull

import io.tagd.arch.access.library
import io.verse.architectures.soa.gateway.SubscribablePullServiceGateway
import io.verse.architectures.soa.io.PullServiceResponse
import io.verse.architectures.soa.provider.SubscriptionProfile
import io.verse.architectures.soa.service.DefaultSubscribablePullService
import io.verse.messaging.core.Message
import io.verse.messaging.core.pull.DefaultPullMessageServiceProvider
import io.verse.messaging.core.pull.PullMessageServiceProvider
import io.verse.messaging.core.pull.PullMessageServiceSpec
import io.verse.messaging.core.pull.PullMessaging

open class DefaultPullMessageServiceSpec(
    override var gateway: SubscribablePullServiceGateway<
            PullMessageRequest,
            PullServiceResponse<Message>,
            Message>? = null,
    override val provider: PullMessageServiceProvider,
    subscriptionProfile: SubscriptionProfile?,
) : DefaultSubscribablePullService<PullMessageRequest, Message>(
        name = NAME,
        provider = provider,
        subscriptionProfile = subscriptionProfile
    ), PullMessageServiceSpec<PullMessageRequest> {

    companion object {
        const val NAME = "messaging/pull/service"
    }
}

fun defaultPullService(): DefaultPullMessageServiceSpec? {
    val library = library<PullMessaging>()
    return library?.service<PullMessageRequest, DefaultPullMessageServiceSpec>(
        DefaultPullMessageServiceProvider::class,
        DefaultPullMessageServiceSpec::class
    )
}