package io.verse.messaging.core.inapp

import io.tagd.arch.access.bind
import io.tagd.arch.scopable.library.Library
import io.verse.architectures.soa.Soa
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


class InAppMessagingTest {

    init {
        bind<Library, Soa>(instance = Mockito.mock())
    }

    @Test(expected = Exception::class)
    fun `given library builder without consumer then verify build throws exception`() {
        InAppMessaging.Builder().build()
    }

    @Test
    fun `given library builder with consumer then verify build stores the consumer`() {
        // given
        val consumer: ApplicationServiceConsumer = mock()

        // then
        val library = InAppMessaging.Builder()
            .consumer(consumer, "genre")
            .build()

        // verify
        Assert.assertEquals(consumer, library.consumer)
    }

    @Test
    fun `given library builder without name then verify build stores non-empty default name`() {
        // given
        val library = InAppMessaging.Builder()
            .consumer(mock(), "genre")
            .build()

        // verify
        Assert.assertTrue(library.name.isNotEmpty())
    }

    @Test
    fun `given name then verify build stores it in the Library`() {
        // given
        val libraryName = "sample library"

        // then
        val library = InAppMessaging.Builder()
            .name(libraryName)
            .consumer(mock(), "genre")
            .build()

        // verify
        Assert.assertEquals(libraryName, library.name)
    }

    @Test
    fun `given library builder with widgetFactory then verify build stores the factory`() {
        // given
        val widgetFactory: InAppMessageWidgetFactory = mock()

        // then
        val library = InAppMessaging.Builder()
            .consumer(mock(), "genre")
            .widgetFactory(widgetFactory)
            .build()

        // verify
        Assert.assertEquals(widgetFactory, library.widgetFactory)
    }

    @Test
    fun `given library then verify put stores the InAppMessageServiceProvider`() {
        // given
        val library = InAppMessaging.Builder()
            .consumer(mock(), "genre")
            .build()

        // then
        val givenServiceProvider: InAppMessageServiceProvider = mock()
        library.put(givenServiceProvider)

        // verify
        val returnedServiceProvider = library.get(givenServiceProvider::class)
        Assert.assertEquals(givenServiceProvider, returnedServiceProvider)
    }

    @Test
    fun `given library with serviceProvider then verify remove clears the saved provider`() {
        // given
        val library = InAppMessaging.Builder()
            .consumer(mock(), "genre")
            .build()
        library.put(mock())

        // then
        val givenServiceProvider: InAppMessageServiceProvider = mock()
        library.remove(givenServiceProvider::class)

        // verify
        val returnedServiceProvider = library.get(givenServiceProvider::class)
        Assert.assertEquals(null, returnedServiceProvider)
    }

    @Test
    fun `given library then verify service returns expected ServiceSpec`() {
        // given
        val library = InAppMessaging.Builder()
            .consumer(mock(), "genre")
            .build()
        val inAppServiceProvider: InAppMessageServiceProvider = mock()
        val givenInAppMessageServiceSpec: InAppMessageServiceSpec = mock()
        whenever(inAppServiceProvider.service(givenInAppMessageServiceSpec::class)).thenReturn(
            givenInAppMessageServiceSpec
        )
        library.put(inAppServiceProvider)

        // then
        val returnedServiceSpec = library.service<InAppMessageServiceSpec>(
            inAppServiceProvider::class,
            givenInAppMessageServiceSpec::class
        )

        // verify
        Assert.assertEquals(givenInAppMessageServiceSpec, returnedServiceSpec)
    }

    @Test
    fun `given library then verify gateway returns expected ServiceSpec`() {
        // given
        val library = InAppMessaging.Builder()
            .consumer(mock(), "genre")
            .build()
        val inAppServiceProvider: InAppMessageServiceProvider = mock()
        val inAppMessageServiceSpec: InAppMessageServiceSpec = mock()
        val givenInAppMessageServiceGateway: InAppMessageServiceGateway = mock()
        whenever(inAppServiceProvider.service(inAppMessageServiceSpec::class)).thenReturn(
            inAppMessageServiceSpec
        )
        whenever(inAppMessageServiceSpec.gateway<InAppMessageServiceGateway>()).thenReturn(
            givenInAppMessageServiceGateway
        )
        library.put(inAppServiceProvider)

        // then
        val returnedServiceGateway = library.gateway<InAppMessageServiceGateway>(
            inAppServiceProvider::class,
            inAppMessageServiceSpec::class
        )

        // verify
        Assert.assertEquals(givenInAppMessageServiceGateway, returnedServiceGateway)
    }

}