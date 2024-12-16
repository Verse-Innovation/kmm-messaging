@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.messaging.cloud.fcm

import io.tagd.arch.scopable.module.Module
import io.tagd.core.Dependencies
import io.tagd.core.Initializer
import io.tagd.di.Global
import io.tagd.di.Scope
import io.tagd.langx.Context
import io.tagd.langx.IllegalValueException
import io.tagd.langx.ref.WeakReference
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.architectures.soa.dispatcher.DefaultServiceDataObjectDispatcher
import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandlerFactory
import io.verse.architectures.soa.provider.ServiceProvider
import io.verse.architectures.soa.provider.SubscriptionProfile
import io.verse.architectures.soa.provider.UserProfile
import io.verse.architectures.soa.provider.UserProfileEnricher
import io.verse.messaging.core.notification.NotificationChannelManager
import io.verse.messaging.core.notification.NotificationIdGenerator
import io.verse.messaging.core.notification.NotificationPublisher
import io.verse.messaging.core.push.PushMessageServiceProvider
import io.verse.messaging.core.push.DefaultTokenHandler
import io.verse.messaging.core.push.TokenHandlerSpec

actual open class FirebaseCloudMessagingInitializer : Initializer<FirebaseCloudMessaging> {

    override fun new(dependencies: Dependencies): FirebaseCloudMessaging {
        val config = dependencies
            .get<FirebaseCloudMessagingConfig>(ARG_CONFIG)
            ?: kotlin.run {
                throw IllegalValueException("config must not be null")
            }

        val provider = newServiceProvider(config.consumer) //todo use firebase-sp
        val service = newService(provider)
        val outerScope = dependencies.get<Scope>(ARG_OUTER_SCOPE)!!

        return FirebaseCloudMessaging.Builder()
            .scope(outerScope)
            .service(service)
            .build().apply {
                register(
                    handle = ServiceDataObjectHandler.DEFAULT_HANDLE,
                    handler = newDefaultFirebaseCloudMessageHandler(
                        context = config.context,
                        module = config.defaultModule
                    )
                )
            }
    }

    protected open fun newServiceProvider(
        consumer: ApplicationServiceConsumer,
    ): FirebaseCloudMessageServiceProvider {

        return FirebaseCloudMessageServiceProvider(
            userProfileEnricher = object : UserProfileEnricher {

                override fun enrich(context: ServiceProvider, `object`: UserProfile): UserProfile {
                    return `object`
                }
            }
        ).also {
            it.consumer = consumer
            consumer.putServiceProvider(PushMessageServiceProvider.GENRE, it)
        }
    }

    protected open fun newService(
        provider: FirebaseCloudMessageServiceProvider,
    ): FirebaseCloudMessagingServiceSpec {

        val service = FirebaseCloudMessagingServiceSpec(
            provider = provider,
            subscriptionProfile = newSubscriptionProfile(provider),
            dispatcher = newFirebaseCloudMessageDispatcher(),
            tokenHandler = newTokenHandler()
        )
        provider.putService(service)
        return service
    }

    protected open fun newTokenHandler(): TokenHandlerSpec {
        return DefaultTokenHandler()
    }

    protected open fun newSubscriptionProfile(
        provider: FirebaseCloudMessageServiceProvider,
    ): SubscriptionProfile {

        return SubscriptionProfile(provider)
    }


    protected open fun newFirebaseCloudMessageDispatcher(
    ): ServiceDataObjectDispatcher<FirebaseCloudMessage, Unit> {

        return DefaultServiceDataObjectDispatcher(factory = ServiceDataObjectHandlerFactory())
    }

    protected open fun newDefaultFirebaseCloudMessageHandler(
        context: Context,
        module: Module,
    ): FirebaseCloudMessageHandler {

        return FirebaseCloudMessageHandler(
            handle = module.name,
            weakContext = WeakReference(context),
            notificationPublisher = NotificationPublisher(
                weakContext = WeakReference(context),
                notificationChannelManager = NotificationChannelManager(),
                notificationIdGenerator = NotificationIdGenerator()
            )
        )
    }

    override fun release() {
        // no-op
    }

    companion object {
        const val ARG_CONFIG = "config"
        const val ARG_OUTER_SCOPE = "outer_scope"
    }

}