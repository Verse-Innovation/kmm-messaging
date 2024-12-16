package io.verse.messaging.android.vendor.huawei

import android.util.Log
import com.huawei.hms.push.HmsMessaging
import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher
import io.verse.architectures.soa.provider.SubscriptionProfile
import io.verse.messaging.core.push.PushMessageServiceSpec
import io.verse.messaging.core.push.TokenHandlerSpec

class HuaweiCloudMessageServiceSpec(
    override val provider: HuaweiCloudMessageServiceProvider,
    override val subscriptionProfile: SubscriptionProfile?,
    override val dispatcher: ServiceDataObjectDispatcher<HuaweiCloudMessage, Unit>?,
    override val gateway: HuaweiCloudMessagingServiceGateway? = null,
    private val hmsMessaging: HmsMessaging,
    override val tokenHandler: TokenHandlerSpec
) : PushMessageServiceSpec<HuaweiCloudMessage> {

    override val name: String
        get() = "messaging/push/vendor/huawei/messaging"

    override val pullKey: String = PULL_KEY

    fun autoInit() {
        hmsMessaging.isAutoInitEnabled = true
    }

    fun turnOnPushNotifications() {
        hmsMessaging.turnOnPush()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d(TAG, "turning on notification failed")
                }
            }
    }

    fun turnOffPushNotifications() {
        hmsMessaging.turnOffPush()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d(TAG, "turning off notification failed")
                }
            }
    }

    fun subscribe(topic: String) {
        try {
            hmsMessaging.subscribe(topic)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d(TAG, "subscribing topic failed")
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun unsubscribe(topic: String) {
        try {
            hmsMessaging.unsubscribe(topic)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d(TAG, "unsubscribing topic failed")
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun release() {
        //no-op
    }

    companion object {
        private const val TAG = "HCMServiceSpec"
        const val PULL_KEY = "hms"
    }

}