@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.messaging.cloud.fcm

import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher
import io.verse.architectures.soa.gateway.SubscribablePushServiceGateway
import io.verse.architectures.soa.provider.SubscriptionProfile
import io.verse.messaging.core.push.PushMessageServiceSpec
import io.verse.messaging.core.push.TokenHandlerSpec

actual open class FirebaseCloudMessagingServiceSpec(
    override val name: String,
    override val dispatcher: ServiceDataObjectDispatcher<FirebaseCloudMessage, Unit>?,
    override val gateway: SubscribablePushServiceGateway<FirebaseCloudMessage, Unit>?,
    actual override val provider: FirebaseCloudMessageServiceProvider,
    override val subscriptionProfile: SubscriptionProfile?,
    actual override val pullKey: String?,
) : PushMessageServiceSpec<FirebaseCloudMessage> {

    override val tokenHandler: TokenHandlerSpec
        get() = TODO("Not yet implemented")

    actual fun subscribe(topic: String) {
        TODO("Not yet implemented")
    }

    actual fun unsubscribe(topic: String) {
        TODO("Not yet implemented")
    }

    override fun release() {
        TODO("Not yet implemented")
    }

}