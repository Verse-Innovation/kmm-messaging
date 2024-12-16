@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.messaging.core.notification

import android.app.NotificationManager
import io.tagd.androidx.content.weakNotificationManager
import io.tagd.arch.domain.crosscutting.async.AsyncContext
import io.tagd.arch.domain.crosscutting.async.present
import io.tagd.core.Service
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference

/**
 * This class is used to publish notifications
 */
actual open class NotificationPublisher(
    private var weakContext: WeakReference<Context>,
    private val notificationChannelManager: NotificationChannelManager,
    private val notificationIdGenerator: NotificationIdGenerator,
) : Service, AsyncContext {

    private val context: Context?
        get() = weakContext.get()

    private val weakNotificationManager
        get() = context?.weakNotificationManager()

    private val notificationManager: NotificationManager?
        get() = weakNotificationManager?.get()

    actual open fun publish(notification: PublishableNotification) {

        notificationManager?.let { notificationManager ->
            notificationChannelManager.setupNotificationChannelIfRequired(
                notificationManager = notificationManager,
                channelId = notification.channelId,
                channelName = notification.channelName,
            )

            present {
                notificationManager.notify(
                    notificationIdGenerator.generateId(),
                    notification.notification
                )
            }
        }
    }

    override fun release() {
        // no-op
    }

}