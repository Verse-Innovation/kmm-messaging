@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.messaging.cloud.fcm

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher
import io.verse.architectures.soa.gateway.SubscribablePushServiceGateway
import io.verse.architectures.soa.provider.SubscriptionProfile
import io.verse.messaging.core.push.PushMessageServiceSpec
import io.verse.messaging.core.push.TokenHandlerSpec

actual open class FirebaseCloudMessagingServiceSpec(
    actual override val provider: FirebaseCloudMessageServiceProvider,
    override val subscriptionProfile: SubscriptionProfile?,
    override val dispatcher: ServiceDataObjectDispatcher<FirebaseCloudMessage, Unit>?,
    override val gateway: SubscribablePushServiceGateway<FirebaseCloudMessage, Unit>? = null,
    override val tokenHandler: TokenHandlerSpec
) : PushMessageServiceSpec<FirebaseCloudMessage> {

    override val name: String
        get() = "messaging/push/firebase/cloud-messaging"

    actual override val pullKey: String? = PULL_KEY

    actual fun subscribe(topic: String) {
        Firebase.messaging.subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d(TAG, "subscribing topic failed")
                }
            }
    }

    actual fun unsubscribe(topic: String) {
        Firebase.messaging.unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d(TAG, "unsubscribing topic failed")
                }
            }
    }

    override fun release() {
        // no-op
    }

    companion object {
        private const val TAG = "FcmServiceSpec"
        const val PULL_KEY = "fcm"
    }
}