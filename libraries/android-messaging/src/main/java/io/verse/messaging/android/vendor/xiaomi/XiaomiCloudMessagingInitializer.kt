package io.verse.messaging.android.vendor.xiaomi

import android.app.Application
import io.tagd.androidx.app.isMainProcess
import io.tagd.arch.scopable.module.Module
import io.tagd.core.Dependencies
import io.tagd.core.Initializer
import io.tagd.di.Global
import io.tagd.di.Scope
import io.tagd.langx.Context
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
import io.verse.messaging.core.push.DefaultTokenHandler
import io.verse.messaging.core.push.PushMessageServiceProvider
import io.verse.messaging.core.push.TokenHandlerSpec

open class XiaomiCloudMessagingInitializer : Initializer<XiaomiCloudMessaging> {

    override fun new(dependencies: Dependencies): XiaomiCloudMessaging {
        val config = dependencies.get<XiaomiCloudMessagingConfig>(ARG_CONFIG)!!

        val provider = newServiceProvider(config.consumer) //todo use xiaomi-sp
        val service = newService(config, provider)
        val newHandler = newDefaultXiaomiCloudMessageHandler(config.context, config.defaultModule)
        val outerScope = dependencies.get<Scope>(ARG_OUTER_SCOPE)!!

        return XiaomiCloudMessaging.Builder()
            .scope(outerScope)
            .service(service)
            .build().apply {
                register(ServiceDataObjectHandler.DEFAULT_HANDLE, newHandler)
                registerXiaomiPush(this, config)
            }
    }

    protected open fun newServiceProvider(
        consumer: ApplicationServiceConsumer
    ): XiaomiCloudMessageServiceProvider {

        return XiaomiCloudMessageServiceProvider(
            userProfileEnricher = object : UserProfileEnricher {
                override fun enrich(context: ServiceProvider, `object`: UserProfile): UserProfile {
                    return `object`
                }
            }
        ).also {
            consumer.putServiceProvider(PushMessageServiceProvider.GENRE, it)
        }
    }

    protected open fun newService(
        config: XiaomiCloudMessagingConfig,
        provider: XiaomiCloudMessageServiceProvider
    ): XiaomiCloudMessageServiceSpec {

        val service = XiaomiCloudMessageServiceSpec(
            provider = provider,
            subscriptionProfile = SubscriptionProfile(provider).also {
                it.set(PROFILE_ATTR_APP_ID , config.xiaomiPushAppId)
                it.set(PROFILE_ATTR_APP_KEY, config.xiaomiPushAppKey)
                it.set(PROFILE_ATTR_REGION , config.xiaomiPushRegion)
            },
            dispatcher = newXiaomiCloudMessageDispatcher(),
            tokenHandler = newTokenHandler()
        )
        provider.putService(service)
        return service
    }

    protected open fun newTokenHandler(): TokenHandlerSpec {
        return DefaultTokenHandler()
    }

    protected open fun newXiaomiCloudMessageDispatcher(
    ): ServiceDataObjectDispatcher<XiaomiCloudMessage, Unit> {

        return DefaultServiceDataObjectDispatcher(factory = ServiceDataObjectHandlerFactory())
    }

    protected open fun newDefaultXiaomiCloudMessageHandler(
        context: Context,
        module: Module,
    ): XiaomiCloudMessageHandler {

        return XiaomiCloudMessageHandler(
            handle = module.name,
            weakContext = WeakReference(context),
            notificationPublisher = notificationPublisher(context),
        )
    }

    protected open fun notificationPublisher(context: Context): NotificationPublisher {
        return NotificationPublisher(
            weakContext = WeakReference(context),
            notificationChannelManager = NotificationChannelManager(),
            notificationIdGenerator = NotificationIdGenerator()
        )
    }


    protected open fun registerXiaomiPush(
        library: XiaomiCloudMessaging,
        config: XiaomiCloudMessagingConfig
    ) {
        val application = config.context.applicationContext as? Application
        if (application?.isMainProcess() == true) {
            library.serviceSpec.registerPush(
                context = config.context,
                appId = config.xiaomiPushAppId,
                appKey = config.xiaomiPushAppKey,
                region = config.xiaomiPushRegion
            )
        }
    }

    override fun release() {
        // no-op
    }

    companion object {
        const val ARG_OUTER_SCOPE = "outer_scope"
        const val ARG_CONFIG = "config"

        const val PROFILE_ATTR_APP_ID = "app_id"
        const val PROFILE_ATTR_APP_KEY = "app_key"
        const val PROFILE_ATTR_REGION = "region"
    }
}

