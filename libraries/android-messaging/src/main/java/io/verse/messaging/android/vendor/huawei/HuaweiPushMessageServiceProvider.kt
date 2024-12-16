package io.verse.messaging.android.vendor.huawei

import io.verse.architectures.soa.provider.DefaultServiceProvider
import io.verse.architectures.soa.provider.UserProfileEnricher
import io.verse.messaging.core.push.PushMessageServiceGateway
import io.verse.messaging.core.push.PushMessageServiceProvider
import io.verse.messaging.core.push.PushMessageServiceProviderPartner

interface HuaweiPushServiceProvider : PushMessageServiceProvider

interface HuaweiPushServiceProviderPartner : HuaweiPushServiceProvider,
    PushMessageServiceProviderPartner

interface HuaweiPushMessageServiceGateway : PushMessageServiceGateway<HuaweiCloudMessage>

class HuaweiCloudMessageServiceProvider(
    genre: String = GENRE,
    userProfileEnricher: UserProfileEnricher? = null,
) : DefaultServiceProvider(
    genre = genre,
    userProfileEnricher = userProfileEnricher
), HuaweiPushServiceProvider {

    companion object {
        const val SERVICE_NAME = "hms"
        private const val GENRE = "messaging/push/vendor/$SERVICE_NAME"
    }
}
