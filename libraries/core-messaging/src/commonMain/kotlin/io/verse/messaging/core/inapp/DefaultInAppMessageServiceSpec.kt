package io.verse.messaging.core.inapp

import io.verse.architectures.soa.provider.SubscriptionProfile

open class DefaultInAppMessageServiceSpec(
    override val name: String = NAME,
    override val gateway: InAppMessageServiceGateway?,
    override val provider: InAppMessageServiceProvider,
    override val subscriptionProfile: SubscriptionProfile?,
) : InAppMessageServiceSpec {

    override fun <W : InAppMessageWidget> publish(message: InAppMessage<W>) {
        gateway?.publish(message)
    }

    override fun release() {
        // no-op
    }

    companion object {
        const val NAME = "messaging/in-app/service"
    }
}