@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.messaging.cloud.fcm

import android.app.Notification
import io.tagd.arch.scopable.module.Module
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.messaging.core.DefaultMessageHandler
import io.verse.messaging.core.Message
import io.verse.messaging.core.notification.NotificationPublisher

/**
 * Each [Module] will provide their own [io.verse.architectures.soa.ServiceDataObjectHandler] for
 * handling [Message]
 */
actual class FirebaseCloudMessageHandler(
    handle: String,
    weakContext: WeakReference<Context>,
    notificationPublisher: NotificationPublisher,
) : DefaultMessageHandler<FirebaseCloudMessage>(
    handle = handle,
    weakContext = weakContext,
    notificationPublisher = notificationPublisher,
) {

    override fun newNotification(
        context: Context,
        message: FirebaseCloudMessage,
    ): Notification {

        val notificationBuilder = notificationBuilder(context, message)
        message.payload.notification?.let { notification ->
            notification.title?.let {
                notificationBuilder.setContentTitle(it)
            }
            notification.body?.let {
                notificationBuilder.setContentTitle(it)
            }
        }
        return notificationBuilder.build()
    }

    companion object {
        const val FCM_HANDLER = "fcm_handler"
    }

}