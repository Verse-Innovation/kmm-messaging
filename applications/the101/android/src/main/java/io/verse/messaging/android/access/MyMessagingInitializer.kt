package io.verse.messaging.android.access

import io.tagd.arch.access.library
import io.tagd.arch.scopable.AbstractWithinScopableInitializer
import io.tagd.core.dependencies
import io.tagd.di.Scope
import io.tagd.langx.Callback
import io.verse.architectures.soa.Soa
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.messaging.android.AndroidMessagingInitializer
import io.verse.messaging.android.MyApplication
import io.verse.messaging.android.inAppMessaging.MyWidgetFactory
import io.verse.messaging.core.inapp.InAppMessaging
import io.verse.messaging.core.inapp.InAppMessagingInitializer

class MyMessagingInitializer(within: MyApplication) :
    AndroidMessagingInitializer<MyApplication>(within) {

    override fun initialize(callback: Callback<Unit>) {
        val soa = outerScope.library<Soa>()!!
        new(
            dependencies(
                AbstractWithinScopableInitializer.ARG_OUTER_SCOPE to within.thisScope,
                ARG_APPLICATION to within,
                ARG_CONSUMER to soa.consumer
            )
        )
        super.initialize(callback)
    }

    override fun newInAppMessaging(
        consumer: ApplicationServiceConsumer,
        outerScope: Scope
    ): InAppMessaging {

        val inAppDependencies = dependencies(
            AbstractWithinScopableInitializer.ARG_OUTER_SCOPE to within.thisScope,
            InAppMessagingInitializer.ARG_CONSUMER to consumer,
            InAppMessagingInitializer.ARG_WIDGET_FACTORY to MyWidgetFactory()
        )
        return MyInAppMessagingInitializer().new(inAppDependencies)
    }
}