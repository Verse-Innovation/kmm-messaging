@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.messaging.core.notification

import io.tagd.core.Service

actual class NotificationPublisher : Service {

    actual fun publish(notification: PublishableNotification) {
        TODO("Not yet implemented")
    }

    override fun release() {
        TODO("Not yet implemented")
    }

}