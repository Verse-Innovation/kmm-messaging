package io.verse.messaging.core.push

import io.verse.architectures.soa.gateway.SubscribablePushServiceGateway
import io.verse.architectures.soa.io.ServiceDataObject
import io.verse.architectures.soa.service.SubscribablePushService
import io.verse.messaging.core.Message
import io.verse.messaging.core.MessageServiceGateway
import io.verse.messaging.core.MessageServiceProvider
import io.verse.messaging.core.MessageServiceProviderPartner

interface PushMessageServiceProvider : MessageServiceProvider {

    companion object {
        const val GENRE = "messaging/push"
    }
}

interface PushMessageServiceProviderPartner : PushMessageServiceProvider,
    MessageServiceProviderPartner

interface PushMessageServiceSpec<T : ServiceDataObject> : SubscribablePushService<T, Unit> {

    val tokenHandler: TokenHandlerSpec
}

interface PushMessageServiceGateway<T : Message> : MessageServiceGateway,
    SubscribablePushServiceGateway<T, Unit>
