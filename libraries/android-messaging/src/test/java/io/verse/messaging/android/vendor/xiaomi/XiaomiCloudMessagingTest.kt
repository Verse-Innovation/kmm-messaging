package io.verse.messaging.android.vendor.xiaomi

import io.verse.architectures.soa.dispatcher.DefaultServiceDataObjectDispatcher
import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandlerFactory
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock

class XiaomiCloudMessagingTest {

    private lateinit var library: XiaomiCloudMessaging

    @Test
    fun `builder should store name if present`() {
        val libraryName = "sample library"
        library = buildLibrary(name = libraryName)

        assertEquals(libraryName, library.name)
    }

    @Test
    fun `builder should use a default name if it is not present`() {
        library = XiaomiCloudMessaging.Builder()
            .service(mock())
            .build()

        assert(library.name.isNotBlank())
    }

    @Test
    fun `builder should store service`() {
        val service = mock<XiaomiCloudMessageServiceSpec>()
        library = buildLibrary(service = service)

        assertEquals(service, library.serviceSpec)
    }

    @Test(expected = Throwable::class)
    fun `builder should throw an error if service is not present`() {
        library = XiaomiCloudMessaging.Builder()
            .build()
    }

    @Test
    fun `register should store the handler`() {
        val handler = mock<XiaomiCloudMessageHandler>()
        val handlerKey = "handler"
        val serviceSpec = serviceSpec()
        library = buildLibrary(service = serviceSpec)

        library.register(handlerKey, handler)

        assertEquals(handler, library.get(handlerKey))
    }

    @Test
    fun `unregister should remove handler`() {
        val handler = mock<XiaomiCloudMessageHandler>()
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
        service: XiaomiCloudMessageServiceSpec = mock(),
    ): XiaomiCloudMessaging {

        return XiaomiCloudMessaging.Builder()
            .name(name)
            .service(service)
            .build()
    }

    private fun serviceSpec(): XiaomiCloudMessageServiceSpec {
        return XiaomiCloudMessageServiceSpec(
            provider = XiaomiCloudMessageServiceProvider(),
            subscriptionProfile = null,
            dispatcher = dispatcher(),
            gateway = mock(),
            tokenHandler = mock()
        )
    }

    private fun dispatcher(): ServiceDataObjectDispatcher<XiaomiCloudMessage, Unit> {
        return DefaultServiceDataObjectDispatcher(
            factory = ServiceDataObjectHandlerFactory()
        )
    }

}
