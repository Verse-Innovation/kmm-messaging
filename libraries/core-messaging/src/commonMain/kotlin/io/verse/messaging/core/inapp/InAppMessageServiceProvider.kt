package io.verse.messaging.core.inapp

import io.verse.architectures.soa.provider.ServiceProviderPartner
import io.verse.architectures.soa.service.SubscribableService
import io.verse.messaging.core.MessageServiceGateway
import io.verse.messaging.core.MessageServiceProvider

interface InAppMessageServiceProvider : MessageServiceProvider {

    companion object {
        const val GENRE = "messaging/in-app"
    }
}

interface InAppMessageServiceProviderPartner : InAppMessageServiceProvider, ServiceProviderPartner

interface InAppMessageServiceSpec : SubscribableService {

    fun <W : InAppMessageWidget> publish(message: InAppMessage<W>)
}

interface InAppMessageServiceGateway : MessageServiceGateway {

    fun <W : InAppMessageWidget> publish(message: InAppMessage<W>)
}

