package io.verse.messaging.core.push

import io.tagd.arch.control.IApplication
import io.tagd.core.Dependencies
import io.tagd.core.Initializer
import io.tagd.di.Scope
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer

abstract class PushMessagingInitializerSpec : Initializer<PushMessaging> {

    override fun new(dependencies: Dependencies): PushMessaging {
        val application = dependencies.get<IApplication>(ARG_APPLICATION)!!
        val consumer = dependencies.get<ApplicationServiceConsumer>(ARG_CONSUMER)!!
        val config = dependencies.get<PushMessagingConfig>(ARG_CONFIG)!!
        val outerScope = dependencies.get<Scope>(ARG_OUTER_SCOPE)!!

        setupVendorPushMessagingServices(application, outerScope, consumer)

        return PushMessaging.Builder()
            .scope(outerScope)
            .consumer(consumer, PushMessageServiceProvider.GENRE)
            .pushConfig(config)
            .inject {library ->
                injectPlatformDependencies(application, library)
            }
            .build()
    }

    protected abstract fun setupVendorPushMessagingServices(
        application: IApplication,
        outerScope: Scope,
        consumer: ApplicationServiceConsumer
    )

    protected abstract fun Scope.injectPlatformDependencies(
        application: IApplication,
        library: PushMessaging
    )

    override fun release() {
        //no-op
    }

    companion object {
        const val ARG_APPLICATION = "application"
        const val ARG_CONSUMER = "consumer"
        const val ARG_CONFIG = "config"
        const val ARG_OUTER_SCOPE = "outer_scope"
    }
}