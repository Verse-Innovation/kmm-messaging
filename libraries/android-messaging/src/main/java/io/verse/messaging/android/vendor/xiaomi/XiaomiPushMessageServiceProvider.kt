package io.verse.messaging.android.vendor.xiaomi

import io.verse.architectures.soa.provider.DefaultServiceProvider
import io.verse.architectures.soa.provider.UserProfileEnricher
import io.verse.messaging.core.push.PushMessageServiceGateway
import io.verse.messaging.core.push.PushMessageServiceProvider
import io.verse.messaging.core.push.PushMessageServiceProviderPartner

interface XiaomiPushMessageServiceProvider : PushMessageServiceProvider

@Suppress("unused")
interface XiaomiPushMessageServiceProviderPartner : PushMessageServiceProviderPartner,
    XiaomiPushMessageServiceProvider

interface XiaomiPushMessageServiceGateway : PushMessageServiceGateway<XiaomiCloudMessage>

class XiaomiCloudMessageServiceProvider(
    genre: String = GENRE,
    userProfileEnricher: UserProfileEnricher? = null
) : DefaultServiceProvider(
    genre = genre,
    userProfileEnricher = userProfileEnricher
), XiaomiPushMessageServiceProvider {

    companion object {
        const val SERVICE_NAME = "xiaomi"
        private const val GENRE = "messaging/push/vendor/$SERVICE_NAME"
    }
}
