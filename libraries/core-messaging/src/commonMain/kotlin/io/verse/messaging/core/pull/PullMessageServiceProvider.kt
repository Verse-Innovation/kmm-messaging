package io.verse.messaging.core.pull

import io.verse.architectures.soa.gateway.SubscribablePullServiceGateway
import io.verse.architectures.soa.io.PullServiceResponse
import io.verse.architectures.soa.service.SubscribablePullService
import io.verse.messaging.core.Message
import io.verse.messaging.core.MessageServiceGateway
import io.verse.messaging.core.MessageServiceProvider
import io.verse.messaging.core.MessageServiceProviderPartner

interface PullMessageServiceProvider : MessageServiceProvider {

    companion object {
        const val GENRE = "messaging/pull"
    }
}

interface PullMessageServiceProviderPartner : PullMessageServiceProvider,
    MessageServiceProviderPartner

interface PullMessageServiceSpec<P : Any> : SubscribablePullService<P, Message>

interface PullMessageServiceGateway<P : Any> :
    MessageServiceGateway,
    SubscribablePullServiceGateway<P, PullServiceResponse<Message>, Message>
