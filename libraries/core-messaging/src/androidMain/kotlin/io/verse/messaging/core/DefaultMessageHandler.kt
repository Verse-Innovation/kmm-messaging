@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.messaging.core

import androidx.core.R
import androidx.core.app.NotificationCompat
import io.tagd.langx.Callback
import io.tagd.langx.Context
import io.tagd.langx.IllegalValueException
import io.tagd.langx.ref.WeakReference
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler
import io.verse.messaging.core.notification.Notification
import io.verse.messaging.core.notification.NotificationPublisher
import io.verse.messaging.core.notification.PublishableNotification
import io.verse.messaging.core.notification.toPublishableNotification

/**
 * This is the default handler for [Message]
 *
 * // the responsibilities pattern
 * 0 . I'm a flow manager for 1 to 5 responsibilities. What I contains is the
 *  flow + flow step composition + flow step execution.
 * 0.1. If needed for flow step composition, I will be having flow step factory/builder
 * 0.2. If needed to perform all the flow steps I will be having flow step delegates
 *
 * 1. channel creation
 * 2. sm to nb conversion
 * 3. publish message
 * 4. ....
 * 5. ....
 *
 */
actual open class DefaultMessageHandler<T : Message>(
    override val handle: String,
    private val weakContext: WeakReference<Context>,
    private val notificationPublisher: NotificationPublisher,
) : ServiceDataObjectHandler<T, Unit> {

    protected val context: Context?
        get() = weakContext.get()

    override fun handle(dataObject: T, result: Callback<Unit>?, error: Callback<Throwable>?) {
        context?.let { context ->
            handle(context, dataObject)
            result?.invoke(Unit)
        } ?: kotlin.run {
            val e = IllegalValueException("context is null")
            error?.invoke(e) ?: throw e
        }
    }

    private fun handle(
        context: Context,
        message: T
    ) {

        val notification = newNotification(context, message)

        notificationPublisher.publish(
            notification = notificationPublisherData(notification)
        )
    }

    private fun notificationPublisherData(
        notification: Notification
    ): PublishableNotification {

        return notification.toPublishableNotification(
            channelId = channelId(),
            channelName = channelName(),
        )
    }

    protected open fun newNotification(context: Context, message: T): Notification {
        return notificationBuilder(context, message).build()
    }

    protected open fun notificationBuilder(
        context: Context,
        message: T
    ): NotificationCompat.Builder {

        return NotificationCompat.Builder(context, channelId())
            .setSmallIcon(getSmallIconBgResId())
            .setAutoCancel(true)
    }

    protected open fun getSmallIconBgResId() = R.drawable.notification_bg

    /**
     * Every module must define the channel id, otherwise defaults to [DEFAULT_CHANNEL_ID]
     */
    protected open fun channelId() = DEFAULT_CHANNEL_ID

    /**
     * Every module must define the channel name, otherwise defaults to [DEFAULT_CHANNEL_NAME]
     */
    protected open fun channelName() = DEFAULT_CHANNEL_NAME

    override fun canHandle(dataObject: T, result: Callback<Boolean>) {
        result.invoke(true)
    }

    override fun release() {
        // no-op
    }

    companion object {
        const val DEFAULT_CHANNEL_ID = "default_channel_id"
        const val DEFAULT_CHANNEL_NAME = "default channel"
    }

}