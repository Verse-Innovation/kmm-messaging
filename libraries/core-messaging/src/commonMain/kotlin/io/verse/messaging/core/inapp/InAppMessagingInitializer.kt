package io.verse.messaging.core.inapp

import io.tagd.core.Dependencies
import io.tagd.core.Initializer
import io.tagd.di.Global
import io.tagd.di.Scope
import io.tagd.langx.IllegalValueException
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.architectures.soa.provider.SubscriptionProfile

open class InAppMessagingInitializer : Initializer<InAppMessaging> {

    override fun new(dependencies: Dependencies): InAppMessaging {
        val consumer = dependencies.get<ApplicationServiceConsumer>(ARG_CONSUMER)
            ?: kotlin.run {
                throw IllegalValueException("ApplicationServiceConsumer must not be null")
            }
        val widgetFactory = dependencies.get<InAppMessageWidgetFactory>(ARG_WIDGET_FACTORY)
            ?: InAppMessageWidgetFactory()
        val outerScope = dependencies.get<Scope>(ARG_OUTER_SCOPE)!!

        return InAppMessaging.Builder()
            .scope(outerScope)
            .consumer(consumer, InAppMessageServiceProvider.GENRE)
            .widgetFactory(widgetFactory)
            .build().also { inAppMessaging ->
                inAppMessaging.put(inAppMessagingServiceProvider())
                inAppMessaging.widgetFactory?.let { registerWidgets(it) }
            }
    }

    protected open fun inAppMessagingServiceProvider(): DefaultInAppMessageServiceProvider {
        return DefaultInAppMessageServiceProvider().also {
            it.putService(inAppMessagingServiceSpec(it))
        }
    }

    protected open fun inAppMessagingServiceSpec(
        inAppMessagingServiceProvider: DefaultInAppMessageServiceProvider,
    ): DefaultInAppMessageServiceSpec {

        return DefaultInAppMessageServiceSpec(
            gateway = inAppMessagingServiceGateway(),
            provider = inAppMessagingServiceProvider,
            subscriptionProfile = SubscriptionProfile(inAppMessagingServiceProvider),
        )
    }

    protected open fun inAppMessagingServiceGateway(): InAppMessageServiceGateway {
        return DefaultInAppMessageServiceGateway()
    }

    protected open fun registerWidgets(widgetFactory: InAppMessageWidgetFactory) {
        // no-op
    }

    override fun release() {
        //no-op
    }

    companion object {
        const val ARG_CONSUMER = "consumer"
        const val ARG_OUTER_SCOPE = "outer_scope"
        const val ARG_WIDGET_FACTORY = "widget_factory"
    }
}