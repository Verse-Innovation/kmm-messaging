package io.verse.messaging.cloud.fcm

import io.verse.architectures.soa.provider.DefaultServiceProvider
import io.verse.architectures.soa.provider.UserProfileEnricher
import io.verse.messaging.core.push.PushMessageServiceGateway
import io.verse.messaging.core.push.PushMessageServiceProvider
import io.verse.messaging.core.push.PushMessageServiceProviderPartner

interface FirebasePushMessageServiceProvider : PushMessageServiceProvider

interface FirebasePushMessageServiceProviderPartner : PushMessageServiceProviderPartner,
    FirebasePushMessageServiceProvider

interface FirebasePushMessageServiceGateway : PushMessageServiceGateway<FirebaseCloudMessage>

open class FirebaseCloudMessageServiceProvider(
    genre: String = GENRE,
    userProfileEnricher: UserProfileEnricher? = null
) : DefaultServiceProvider(
    genre = genre,
    userProfileEnricher = userProfileEnricher
), FirebasePushMessageServiceProvider {

    companion object {
        const val SERVICE_NAME = "fcm"
        private const val GENRE = "messaging/push/$SERVICE_NAME"
    }

}