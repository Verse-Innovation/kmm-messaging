package io.verse.messaging.core

import io.tagd.arch.control.IApplication
import io.tagd.arch.scopable.AbstractWithinScopableInitializer
import io.tagd.arch.scopable.Scopable
import io.tagd.core.Dependencies
import io.tagd.core.dependencies
import io.tagd.di.Global
import io.tagd.di.Scope
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.messaging.core.inapp.InAppMessaging
import io.verse.messaging.core.inapp.InAppMessagingInitializer
import io.verse.messaging.core.pull.PullMessaging
import io.verse.messaging.core.pull.PullMessagingInitializerSpec
import io.verse.messaging.core.push.PushMessaging
import io.verse.messaging.core.push.PushMessagingConfig
import io.verse.messaging.core.push.PushMessagingInitializerSpec

abstract class MessagingInitializerSpec<S : Scopable>(within: S) :
    AbstractWithinScopableInitializer<S, Messaging>(within) {

    override fun new(dependencies: Dependencies): Messaging {
        val application = dependencies.get<IApplication>(ARG_APPLICATION)!!
        val consumer = dependencies.get<ApplicationServiceConsumer>(ARG_CONSUMER)!!
        val outerScope = dependencies.get<Scope>(ARG_OUTER_SCOPE)!!

        val pushMessaging = newPushMessaging(application, consumer, outerScope)
        val pullMessaging = newPullMessaging(consumer, outerScope)
        val inAppMessaging = newInAppMessaging(consumer, outerScope)

        return Messaging.Builder()
            .pushMessaging(pushMessaging)
            .pullMessaging(pullMessaging)
            .inAppMessaging(inAppMessaging)
            .build()
    }

    protected open fun newPushMessaging(
        application: IApplication,
        consumer: ApplicationServiceConsumer,
        outerScope: Scope
    ): PushMessaging {

        val pushDependencies = dependencies(
            PushMessagingInitializerSpec.ARG_APPLICATION to application,
            PushMessagingInitializerSpec.ARG_CONSUMER to consumer,
            PushMessagingInitializerSpec.ARG_CONFIG to newPushMessagingConfig(),
            PushMessagingInitializerSpec.ARG_OUTER_SCOPE to outerScope
        )
        return newPushMessaging(pushDependencies)
    }

    abstract fun newPushMessaging(dependencies: Dependencies): PushMessaging

    protected open fun newPushMessagingConfig() = PushMessagingConfig()

    protected open fun newPullMessaging(
        consumer: ApplicationServiceConsumer,
        outerScope: Scope
    ): PullMessaging {

        val dependencies = dependencies(
            PullMessagingInitializerSpec.ARG_CONSUMER to consumer,
            PullMessagingInitializerSpec.ARG_OUTER_SCOPE to outerScope
        )
        return newPullMessaging(dependencies)
    }

    abstract fun newPullMessaging(dependencies: Dependencies): PullMessaging

    protected open fun newInAppMessaging(
        consumer: ApplicationServiceConsumer,
        outerScope: Scope
    ): InAppMessaging {

        val dependencies = dependencies(
            InAppMessagingInitializer.ARG_CONSUMER to consumer,
            InAppMessagingInitializer.ARG_OUTER_SCOPE to outerScope
        )
        return InAppMessagingInitializer().new(dependencies)
    }

    companion object {
        //todo - break application into context and module then we can move this class to library
        //however we've to make it abstract to leave clients to inject service specific config
        const val ARG_APPLICATION = "application"
        const val ARG_CONSUMER = "consumer"
        const val ARG_OUTER_SCOPE = "outer_scope"
    }
}