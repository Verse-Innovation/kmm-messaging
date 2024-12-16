@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.messaging.core

import io.tagd.langx.Callback
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler
import io.verse.messaging.core.notification.NotificationPublisher

actual open class DefaultMessageHandler<T : Message>(
    override val handle: String,
    private val weakContext: WeakReference<Context>,
    private val notificationPublisher: NotificationPublisher,
) : ServiceDataObjectHandler<T, Unit> {

    override fun canHandle(dataObject: T, result: Callback<Boolean>) {
        TODO("Not yet implemented")
    }

    override fun release() {
        TODO("Not yet implemented")
    }

    override fun handle(dataObject: T, result: Callback<Unit>?, error: Callback<Throwable>?) {
        TODO("Not yet implemented")
    }

}