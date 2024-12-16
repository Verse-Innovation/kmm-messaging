package io.verse.messaging.core

import android.content.ContextWrapper
import androidx.core.R
import androidx.core.app.NotificationCompat
import androidx.test.platform.app.InstrumentationRegistry
import io.tagd.androidx.content.weakNotificationManager
import io.tagd.arch.test.FakeInjector
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.messaging.core.notification.NotificationChannelManager
import io.verse.messaging.core.notification.NotificationIdGenerator
import io.verse.messaging.core.notification.NotificationPublisher
import io.verse.messaging.core.notification.PublishableNotification
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class NotificationPublisherTest {

    private val context = ContextWrapper(InstrumentationRegistry.getInstrumentation().targetContext)
    private val weakContext: WeakReference<Context> = WeakReference(context)
    private val channelManager = NotificationChannelManager()
    private val notificationIdGenerator = NotificationIdGenerator()
    private val publisher = NotificationPublisher(
        weakContext = weakContext,
        notificationChannelManager = channelManager,
        notificationIdGenerator = notificationIdGenerator
    )

    @Before
    fun setup() {
        FakeInjector.inject()
    }

    @After
    fun tearDown() {
        FakeInjector.release()
    }

    @Test
    fun publish_shouldSendNotification() {
        val channelId = "default"
        val channelName = "default"
        val notification = notification(channelId)
        val data = PublishableNotification(
            notification = notification,
            channelId = channelId,
            channelName = channelName
        )
        val notificationManager = context.weakNotificationManager()
        publisher.publish(data)

        val activeNotificationsCount = notificationManager?.get()?.activeNotifications?.size
        assertEquals(1, activeNotificationsCount)
    }

    private fun notification(channelId: String) =
        NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notification_bg)
            .build()

}