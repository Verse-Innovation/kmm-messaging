package io.verse.messaging.core

import io.verse.architectures.soa.gateway.SubscribableServiceGateway
import io.verse.architectures.soa.io.ServiceDataObject
import io.verse.architectures.soa.provider.ServiceProvider
import io.verse.architectures.soa.provider.ServiceProviderPartner

/**
 * A `messaging` genre's Service Provider
 * ex - FCM
 */
interface MessageServiceProvider : ServiceProvider

/**
 * A `messaging` genre's Service Provider Partner
 * ex - CleverTap
 */
interface MessageServiceProviderPartner : MessageServiceProvider, ServiceProviderPartner

/**
 * Acts as a bridge / channel between message service consumer and message service provider
 * Ex - FirebasePushMessageService, XiaomiPushService
 *
 * It utilized the [io.verse.architectures.soa.dispatcher.DefaultServiceDataObjectDispatcher] to dispatch the
 * processed [Message]
 */
interface MessageServiceGateway : SubscribableServiceGateway

open class Message(handle: String) : ServiceDataObject() {

    init {
        initHandle(handle)
    }

    private fun initHandle(handle: String) {
        this.handle = handle
    }
}