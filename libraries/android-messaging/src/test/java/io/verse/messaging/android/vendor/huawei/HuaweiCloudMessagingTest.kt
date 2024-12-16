package io.verse.messaging.android.vendor.huawei

import io.verse.architectures.soa.dispatcher.DefaultServiceDataObjectDispatcher
import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandlerFactory
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock

class HuaweiCloudMessagingTest {

    private lateinit var library: HuaweiCloudMessaging

    @Test
    fun `builder should store name if present`() {
        val libraryName = "sample library"
        library = buildLibrary(name = libraryName)

        assertEquals(libraryName, library.name)
    }

    @Test
    fun `builder should use a default name if it is not present`() {
        library = HuaweiCloudMessaging.Builder()
            .service(mock())
            .build()

        assert(library.name.isNotBlank())
    }

    @Test
    fun `builder should store service`() {
        val service = mock<HuaweiCloudMessageServiceSpec>()
        library = buildLibrary(service = service)

        assertEquals(service, library.serviceSpec)
    }

    @Test(expected = Throwable::class)
    fun `builder should throw an error if service is not present`() {
        library = HuaweiCloudMessaging.Builder()
            .build()
    }

    @Test
    fun `register should store the handler`() {
        val handler = mock<HuaweiCloudMessageHandler>()
        val handlerKey = "handler"
        val serviceSpec = serviceSpec()
        library = buildLibrary(service = serviceSpec)

        library.register(handlerKey, handler)

        assertEquals(handler, library.get(handlerKey))
    }

    @Test
    fun `unregister should remove handler`() {
        val handler = mock<HuaweiCloudMessageHandler>()
        val handlerKey = "handler"
        val serviceSpec = serviceSpec()
        library = buildLibrary(service = serviceSpec)

        library.register(handlerKey, handler)
        assertEquals(handler, library.get(handlerKey))
        library.unregister(handlerKey)

        assertEquals(null, library.get(handlerKey))
    }

    private fun buildLibrary(
        name: String = "library",
        service: HuaweiCloudMessageServiceSpec = mock(),
    ): HuaweiCloudMessaging {

        return HuaweiCloudMessaging.Builder()
            .name(name)
            .service(service)
            .build()
    }

    private fun serviceSpec(): HuaweiCloudMessageServiceSpec {
        return HuaweiCloudMessageServiceSpec(
            provider = HuaweiCloudMessageServiceProvider(),
            subscriptionProfile = null,
            dispatcher = dispatcher(),
            hmsMessaging = mock(),
            tokenHandler = mock()
        )
    }

    private fun dispatcher(): ServiceDataObjectDispatcher<HuaweiCloudMessage, Unit> {
        return DefaultServiceDataObjectDispatcher(
            factory = ServiceDataObjectHandlerFactory()
        )
    }

}