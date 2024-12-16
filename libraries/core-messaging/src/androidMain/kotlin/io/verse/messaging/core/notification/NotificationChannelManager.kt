@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.messaging.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import io.tagd.core.Service

actual class NotificationChannelManager : Service {

    fun setupNotificationChannelIfRequired(
        notificationManager: NotificationManager,
        channelId: String,
        channelName: String,
        notificationPriority: Int? = null,
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                notificationPriority ?: NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun release() {
        // no-op
    }

}