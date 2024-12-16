package io.verse.messaging.core.inapp

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import kotlin.test.assertEquals

class InAppMessageWidgetFactoryTest {

    private lateinit var inAppMessageWidgetFactory: InAppMessageWidgetFactory


    @Before
    fun setUp() {
        inAppMessageWidgetFactory = InAppMessageWidgetFactory()
    }


    @Test
    fun `given widgetBuilder for a model class then verify newWidget returns the widget`() {
        // given
        val modelClass: InAppMessage<InAppMessageWidget> = mock()
        val expectedWidget: InAppMessageWidget = mock()
        inAppMessageWidgetFactory.put(
            modelClass = modelClass::class,
            builder = { _, _ -> expectedWidget }
        )

        // then
        val actualWidget = inAppMessageWidgetFactory.newWidget(modelClass)

        // verify
        assertEquals(expectedWidget, actualWidget)
    }

    @Test
    fun `given non empty widget registry then verify after release newWidget returns the null`() {
        // given
        val modelClass: InAppMessage<InAppMessageWidget> = mock()
        inAppMessageWidgetFactory.put(
            modelClass = modelClass::class,
            builder = { _, _ -> mock() }
        )

        // then
        inAppMessageWidgetFactory.release()
        val actualWidget = inAppMessageWidgetFactory.newWidget(modelClass)

        // verify
        assertEquals(null, actualWidget)
    }

    @After
    fun tearDown() {
        inAppMessageWidgetFactory.release()
    }
}