@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.messaging.cloud.fcm

import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.messaging.core.DefaultMessageHandler
import io.verse.messaging.core.notification.NotificationPublisher

actual class FirebaseCloudMessageHandler(
    handle: String,
    weakContext: WeakReference<Context>,
    notificationPublisher: NotificationPublisher,
) : DefaultMessageHandler<FirebaseCloudMessage>(handle, weakContext, notificationPublisher)