package io.verse.messaging.android.pull

import io.tagd.arch.access.library
import io.verse.architectures.soa.provider.SubscriptionProfile
import io.verse.messaging.android.vendor.huawei.HuaweiCloudMessaging
import io.verse.messaging.android.vendor.xiaomi.XiaomiCloudMessaging
import io.verse.messaging.cloud.fcm.FirebaseCloudMessaging
import io.verse.messaging.core.pull.PullMessagingInitializerSpec
import io.verse.messaging.core.pull.DefaultPullMessageServiceProvider

class AndroidPullMessagingInitializer : PullMessagingInitializerSpec() {

    private val firebaseCloudMessaging: FirebaseCloudMessaging?
        get() = library()

    private val huaweiCloudMessaging: HuaweiCloudMessaging?
        get() = library()

    private val xiaomiCloudMessaging: XiaomiCloudMessaging?
        get() = library()

    override fun newPullMessagingServiceProvider(): DefaultPullMessageServiceProvider {
        return DefaultPullMessageServiceProvider().also {
            it.putService(pullMessagingServiceSpec(it))
        }
    }

    private fun pullMessagingServiceSpec(
        pullMessagingServiceProvider: DefaultPullMessageServiceProvider,
    ): DefaultPullMessageServiceSpec {

        return DefaultPullMessageServiceSpec(
            gateway = pullMessagingServiceGateway(),
            provider = pullMessagingServiceProvider,
            subscriptionProfile = SubscriptionProfile(pullMessagingServiceProvider),
        )
    }

    private fun pullMessagingServiceGateway(): DefaultPullMessageServiceGateway {
        return DefaultPullMessageServiceGateway().also { pullGateway ->
            firebaseCloudMessaging?.serviceSpec?.let {
                pullGateway.put(it)
            }
            huaweiCloudMessaging?.serviceSpec?.let {
                pullGateway.put(it)
            }
            xiaomiCloudMessaging?.serviceSpec?.let {
                pullGateway.put(it)
            }
        }
    }

    override fun release() {
        //no-op
    }

    companion object {
        const val ARG_CONSUMER = "consumer"
        const val ARG_OUTER_SCOPE = "outer_scope"
    }
}