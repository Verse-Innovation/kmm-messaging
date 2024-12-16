package io.verse.messaging.core.inapp


import io.tagd.arch.access.bind
import io.tagd.arch.scopable.library.Library
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DefaultInAppMessageServiceGatewayTest {

    private lateinit var inAppMessageServiceGateway: InAppMessageServiceGateway
    private lateinit var widgetFactory: InAppMessageWidgetFactory

    @Before
    fun setUp() {
        val library: InAppMessaging = mock()
        bind<Library, InAppMessaging>(instance = library)
        
        widgetFactory = mock()
        whenever(library.widgetFactory).thenReturn(widgetFactory)

        inAppMessageServiceGateway = DefaultInAppMessageServiceGateway()
    }

    @Test
    fun `given message then verify publish finds and shows the expected widget with message`() {
        // given
        val message: InAppMessage<InAppMessageWidget> = mock()
        val widget: InAppMessageWidget = mock()
        whenever(widgetFactory.newWidget(message)).thenReturn(widget)

        // then
        inAppMessageServiceGateway.publish(message)

        // verify
        verify(widget).show()
    }
}