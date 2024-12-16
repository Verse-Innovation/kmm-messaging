package io.verse.messaging.cloud.fcm.fake

import io.verse.architectures.soa.gateway.SubscribableServiceGateway
import io.verse.architectures.soa.provider.DefaultServiceProvider
import io.verse.architectures.soa.provider.ServiceProvider
import io.verse.architectures.soa.provider.ServiceProviderProfile
import io.verse.architectures.soa.provider.SubscriptionProfile
import io.verse.architectures.soa.service.SubscribableService

class FakeServiceProviderProfile(override val provider: ServiceProvider?) : ServiceProviderProfile

class FakeSubscribableService(override val name: String) : SubscribableService {

    override val gateway: SubscribableServiceGateway? = null

    override val provider: ServiceProvider = DefaultServiceProvider("genre")

    override val subscriptionProfile: SubscriptionProfile? = null

    override fun release() {}
}

class FakeSubscribableServiceGateway : SubscribableServiceGateway {

    override val service: SubscribableService? = null

    override fun release() {}
}