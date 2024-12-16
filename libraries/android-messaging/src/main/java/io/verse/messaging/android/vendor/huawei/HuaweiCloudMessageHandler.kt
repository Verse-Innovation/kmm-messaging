package io.verse.messaging.android.vendor.huawei

import android.app.Notification
import io.tagd.arch.scopable.Scopable
import io.tagd.arch.scopable.module.Module
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.messaging.cloud.fcm.FirebaseCloudMessage
import io.verse.messaging.core.DefaultMessageHandler
import io.verse.messaging.core.notification.NotificationPublisher

/**
 * Each [Module] will provide their own [ServiceMessageHandler] for handling [ServiceMessage]
 */
class HuaweiCloudMessageHandler(
    handle: String,
    weakContext: WeakReference<Context>,
    notificationPublisher: NotificationPublisher,
) : DefaultMessageHandler<HuaweiCloudMessage>(
    handle = handle,
    weakContext = weakContext,
    notificationPublisher = notificationPublisher,
) {

    override fun newNotification(
        context: Context,
        message: HuaweiCloudMessage,
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
        const val HUAWEI_PUSH_HANDLER = "huawei_push_handler"
    }

}