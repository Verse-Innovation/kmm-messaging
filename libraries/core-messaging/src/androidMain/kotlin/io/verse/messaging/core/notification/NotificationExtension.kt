package io.verse.messaging.core.notification

import android.app.Notification

fun Notification.toPublishableNotification(
    channelId: String,
    channelName: String,
): PublishableNotification {

    return PublishableNotification(
        notification = this,
        channelId = channelId,
        channelName = channelName
    )
}