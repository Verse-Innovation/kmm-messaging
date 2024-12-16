package io.verse.messaging.cloud.fcm

import io.verse.architectures.soa.dispatcher.DefaultServiceDataObjectDispatcher
import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandlerFactory
import io.verse.messaging.core.push.DefaultTokenHandler
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock

class FirebaseCloudMessagingTest {

    private lateinit var library: FirebaseCloudMessaging

    @Test
    fun `builder should store name if present`() {
        val libraryName = "sample library"
        library = buildLibrary(name = libraryName)

        assertEquals(libraryName, library.name)
    }

    @Test
    fun `builder should use a default name if it is not present`() {
        library = FirebaseCloudMessaging.Builder()
            .service(mock())
            .build()

        assert(library.name.isNotBlank())
    }

    @Test
    fun `builder should store service`() {
        val service = mock<FirebaseCloudMessagingServiceSpec>()
        library = buildLibrary(service = service)

        assertEquals(service, library.serviceSpec)
    }

    @Test(expected = Throwable::class)
    fun `builder should throw an error if service is not present`() {
        library = FirebaseCloudMessaging.Builder()
            .build()
    }

    @Test
    fun `register should store the handler`() {
        val handler = mock<FirebaseCloudMessageHandler>()
        val handlerKey = "handler"
        val serviceSpec = serviceSpec()
        library = buildLibrary(service = serviceSpec)

        library.register(handlerKey, handler)

        assertEquals(handler, library.get(handlerKey))
    }

    @Test
    fun `unregister should remove handler`() {
        val handler = mock<FirebaseCloudMessageHandler>()
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
        service: FirebaseCloudMessagingServiceSpec = mock(),
    ): FirebaseCloudMessaging {

        return FirebaseCloudMessaging.Builder()
            .name(name)
            .service(service)
            .build()
    }

    private fun serviceSpec(): FirebaseCloudMessagingServiceSpec {
        return FirebaseCloudMessagingServiceSpec(
            provider = FirebaseCloudMessageServiceProvider(),
            subscriptionProfile = null,
            dispatcher = dispatcher(),
            tokenHandler = DefaultTokenHandler()
        )
    }

    private fun dispatcher(): ServiceDataObjectDispatcher<FirebaseCloudMessage, Unit> {
        return DefaultServiceDataObjectDispatcher(
            factory = ServiceDataObjectHandlerFactory()
        )
    }

}