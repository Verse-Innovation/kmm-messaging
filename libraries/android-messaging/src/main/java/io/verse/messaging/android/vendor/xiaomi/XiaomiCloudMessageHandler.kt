package io.verse.messaging.android.vendor.xiaomi

import android.app.Notification
import io.tagd.arch.scopable.Scopable
import io.tagd.langx.Callback
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.messaging.cloud.fcm.FirebaseCloudMessage
import io.verse.messaging.core.DefaultMessageHandler
import io.verse.messaging.core.notification.NotificationPublisher

class XiaomiCloudMessageHandler(
    handle: String,
    weakContext: WeakReference<Context>,
    notificationPublisher: NotificationPublisher,
) : DefaultMessageHandler<XiaomiCloudMessage>(
    handle = handle,
    weakContext = weakContext,
    notificationPublisher = notificationPublisher,
) {

    override fun handle(
        dataObject: XiaomiCloudMessage,
        result: Callback<Unit>?,
        error: Callback<Throwable>?,
    ) {

        val isNotificationMessage = dataObject.meta.isNotificationMessage
        if (!isNotificationMessage) {
            // make super call that will trigger the notification
            super.handle(dataObject, result, error)
        } else {
            result?.invoke(Unit)
            // notification will be shown by Xiaomi SDK directly
            // todo - signal that sdk itself shown the notification
        }
    }

    override fun newNotification(
        context: Context,
        message: XiaomiCloudMessage,
    ): Notification {

        val notificationBuilder = notificationBuilder(context, message)
        message.payload.notification.let { notification ->
            notification.title?.let {
                notificationBuilder.setContentTitle(it)
            }
            notification.description?.let {
                notificationBuilder.setContentText(it)
            }
            notification.category?.let {
                notificationBuilder.setCategory(it)
            }
        }
        return notificationBuilder.build()
    }

    companion object {
        const val XIAOMI_HANDLER = "xiaomi_handler"
    }
}