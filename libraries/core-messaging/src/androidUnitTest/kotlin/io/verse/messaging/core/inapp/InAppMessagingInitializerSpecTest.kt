package io.verse.messaging.core.inapp

import io.tagd.arch.access.bind
import io.tagd.arch.scopable.library.Library
import io.tagd.core.Dependencies
import io.tagd.di.Global
import io.tagd.langx.IllegalValueException
import io.verse.architectures.soa.Soa
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class InAppMessagingInitializerSpecTest {

    private lateinit var dependencies: Dependencies
    private var hasRegisteredWidgets = false

    private var initializer = object : InAppMessagingInitializer() {
        override fun registerWidgets(widgetFactory: InAppMessageWidgetFactory) {
            hasRegisteredWidgets = true
        }
    }

    @Before
    fun setup() {
        dependencies = Dependencies()
        hasRegisteredWidgets = false
    }

    @Test(expected = IllegalValueException::class)
    fun `given empty dependencies then verify new should throw exception`() {
        initializer.new(dependencies)
    }

    @Test
    fun `given only mandatory dependencies then verify new uses the default without exceptions`() {
        initDependencies()

        val library = try {
            initializer.new(dependencies)
        } catch (e: IllegalValueException) {
            e.printStackTrace()
            null
        }

        // verify
        assert(library != null)
        assert(library!!.widgetFactory != null)
    }

    @Test
    fun `given all dependencies then verify new should not throw exception`() {
        initDependencies(widgetFactory = mock())

        val noExceptionThrown = try {
            initializer.new(dependencies)
            true
        } catch (e: IllegalValueException) {
            e.printStackTrace()
            false
        }

        // verify
        assert(noExceptionThrown)
    }

    @Test
    fun `given all dependencies then verify new should register InAppMessageServiceProvider`() {
        // given
        initDependencies()

        // then
        val data = initializer.new(dependencies)

        // verify
        assert(data.get(DefaultInAppMessageServiceProvider::class) != null)
    }

    @Test
    fun `given all dependencies then verify new should register WidgetFactories`() {
        // given
        initDependencies()

        // then
        initializer.new(dependencies)

        // verify
        assert(hasRegisteredWidgets)
    }

    @Test
    fun `given serviceProvider then verify it should be registered with InAppMessageServiceSpec`() {
        // given
        initDependencies()

        // then
        val inAppMessaging = initializer.new(dependencies)
        val serviceProvider = inAppMessaging.get(DefaultInAppMessageServiceProvider::class)!!

        // verify
        assert(serviceProvider.service(DefaultInAppMessageServiceSpec::class) != null)
    }

    private fun initDependencies(
        widgetFactory: InAppMessageWidgetFactory? = null,
    ) {
        bind<Library, Soa>(instance = mock())

        val consumer: ApplicationServiceConsumer = mock()
        dependencies.put(InAppMessagingInitializer.ARG_CONSUMER, consumer)
        dependencies.put(InAppMessagingInitializer.ARG_WIDGET_FACTORY, widgetFactory)
        dependencies.put(InAppMessagingInitializer.ARG_OUTER_SCOPE, Global)
    }

    @After
    fun tearDown() {
        dependencies.clear()
        initializer.release()
    }
}