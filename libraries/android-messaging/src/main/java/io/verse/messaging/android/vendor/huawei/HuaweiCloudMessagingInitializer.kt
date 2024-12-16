package io.verse.messaging.android.vendor.huawei

import com.huawei.hms.push.HmsMessaging
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
import io.verse.messaging.core.push.DefaultTokenHandler
import io.verse.messaging.core.push.PushMessageServiceProvider
import io.verse.messaging.core.push.TokenHandlerSpec

open class HuaweiCloudMessagingInitializer : Initializer<HuaweiCloudMessaging> {

    override fun new(dependencies: Dependencies): HuaweiCloudMessaging {
        val config = dependencies.get<HuaweiCloudMessagingConfig>(ARG_CONFIG)
            ?: kotlin.run {
                throw IllegalValueException("config must not be null")
            }

        val provider = newServiceProvider(config.consumer)
        val service = newService(provider, config).also {
            it.autoInit()
        }
        val outerScope = dependencies.get<Scope>(ARG_OUTER_SCOPE)!!

        return HuaweiCloudMessaging.Builder()
            .scope(outerScope)
            .service(service)
            .build().apply {
                register(
                    handle = ServiceDataObjectHandler.DEFAULT_HANDLE,
                    handler = newDefaultHuaweiPushMessageHandler(
                        context = config.context,
                        module = config.defaultModule
                    )
                )
            }
    }

    protected open fun newServiceProvider(
        consumer: ApplicationServiceConsumer,
    ): HuaweiCloudMessageServiceProvider {

        return HuaweiCloudMessageServiceProvider(
            userProfileEnricher = object : UserProfileEnricher {

                override fun enrich(
                    context: ServiceProvider,
                    `object`: UserProfile,
                ): UserProfile {

                    return `object`
                }
            },
        ).also {
            it.consumer = consumer
            consumer.putServiceProvider(PushMessageServiceProvider.GENRE, it)
        }
    }

    protected open fun newService(
        provider: HuaweiCloudMessageServiceProvider,
        config: HuaweiCloudMessagingConfig,
    ): HuaweiCloudMessageServiceSpec {

        val service = HuaweiCloudMessageServiceSpec(
            provider = provider,
            subscriptionProfile = newSubscriptionProfile(provider, config),
            dispatcher = newHuaweiPushDispatcher(),
            hmsMessaging = HmsMessaging.getInstance(config.context),
            tokenHandler = newTokenHandler()
        )
        provider.putService(service)
        return service
    }

    protected open fun newTokenHandler(): TokenHandlerSpec {
        return DefaultTokenHandler()
    }

    protected open fun newSubscriptionProfile(
        provider: HuaweiCloudMessageServiceProvider,
        config: HuaweiCloudMessagingConfig,
    ): SubscriptionProfile {

        return SubscriptionProfile(provider).also {
            it.set(PROFILE_ATTR_CP_ID, config.cpId)
            it.set(PROFILE_ATTR_APP_ID, config.appId)
        }
    }

    protected open fun newHuaweiPushDispatcher(
    ): ServiceDataObjectDispatcher<HuaweiCloudMessage, Unit> {

        return DefaultServiceDataObjectDispatcher(factory = ServiceDataObjectHandlerFactory())
    }

    protected open fun newDefaultHuaweiPushMessageHandler(
        context: Context,
        module: Module,
    ): HuaweiCloudMessageHandler {

        return HuaweiCloudMessageHandler(
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
        const val ARG_OUTER_SCOPE = "outer_scope"
        const val ARG_CONFIG = "config"
        const val PROFILE_ATTR_CP_ID = "cp_id"
        const val PROFILE_ATTR_APP_ID = "app_id"
    }

}