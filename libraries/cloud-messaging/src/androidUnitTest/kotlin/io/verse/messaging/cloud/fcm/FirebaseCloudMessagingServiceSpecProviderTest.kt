package io.verse.messaging.cloud.fcm

import io.tagd.di.typeOf
import io.verse.architectures.soa.provider.ServiceProviderProfile
import io.verse.architectures.soa.service.SubscribableService
import io.verse.messaging.cloud.fcm.fake.FakeServiceProviderProfile
import io.verse.messaging.cloud.fcm.fake.FakeSubscribableService
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class FirebaseCloudMessagingServiceSpecProviderTest {

    private val provider = FirebaseCloudMessageServiceProvider()

    @Test
    fun `putProfile should insert profile correctly`() {
        val profile = FakeServiceProviderProfile(mock())
        provider.putProfile(profile)

        val fetchedProfile = provider.profile(typeOf<FakeServiceProviderProfile>())

        assertEquals(profile, fetchedProfile)
    }

    @Test
    fun `given a profile class then profile method should return its correct instance`() {
        val serviceProviderProfile = FakeServiceProviderProfile(mock())
        provider.putProfile(serviceProviderProfile)

        val data1 = provider.profile(serviceProviderProfile::class)
        val data2 = provider.profile(serviceProviderProfile.javaClass.kotlin)
        val data3 = provider.profile(typeOf<FakeServiceProviderProfile>())
        val data4 = provider.profile(FakeServiceProviderProfile::class)

        val data5 = provider.profile(typeOf())
        val data6 = provider.profile(ServiceProviderProfile::class)

        assertEquals(serviceProviderProfile, data1)
        assertEquals(serviceProviderProfile, data2)
        assertEquals(serviceProviderProfile, data3)
        assertEquals(serviceProviderProfile, data4)

        assertEquals(null, data5)
        assertEquals(null, data6)
    }

    @Test
    fun `putService should insert service correctly`() {
        val service = mock<FakeSubscribableService>()
        provider.putService(service = service)

        val fetchedService = provider.service(service::class)

        assertEquals(service, fetchedService)
    }

    @Test
    fun `service method should return correct instance of SubscribableService`() {
        val service = mock<FakeSubscribableService>()
        provider.putService(service = service)

        val data1 = provider.service(service.javaClass.kotlin)
        val data2 = provider.service(typeOf<FakeSubscribableService>())
        val data3 = provider.service(FakeSubscribableService::class)

        val data4 = provider.service(typeOf())
        val data5 = provider.service(SubscribableService::class)

        assertEquals(service, data1)
        assertEquals(service, data2)
        assertEquals(service, data3)

        assertEquals(null, data4)
        assertEquals(null, data5)
    }

}