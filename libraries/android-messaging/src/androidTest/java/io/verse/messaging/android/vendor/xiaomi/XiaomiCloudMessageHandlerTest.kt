package io.verse.messaging.android.vendor.xiaomi

import android.content.ContextWrapper
import androidx.test.platform.app.InstrumentationRegistry
import io.tagd.langx.Callback
import io.tagd.langx.Context
import io.tagd.langx.IllegalValueException
import io.tagd.langx.ref.WeakReference
import io.verse.messaging.core.notification.NotificationChannelManager
import io.verse.messaging.core.notification.NotificationIdGenerator
import io.verse.messaging.core.notification.NotificationPublisher
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class XiaomiCloudMessageHandlerTest {

    private val context = ContextWrapper(InstrumentationRegistry.getInstrumentation().targetContext)
    private val weakContext: WeakReference<Context> = WeakReference(context)
    private val notificationPublisher = getNotificationPublisher(context)
    private val handler = XiaomiCloudMessageHandler(mock(), weakContext, notificationPublisher)

    @Test
    fun given_contextIsNull_then_handleShouldSend_errorCallback() {
        val error: Callback<Throwable> = mock()
        val errorCapture = argumentCaptor<Throwable>()
        weakContext.clear()

        handler.handle(dataObject = getTransparentDataObject(), error = error)

        verify(error).invoke(errorCapture.capture())
        assert(errorCapture.firstValue is IllegalValueException)
        assertEquals("context is null", errorCapture.firstValue.message)
    }

    @Test
    fun given_contextIsNullAndErrorIsAlsoNull_withNotificationData_then_Exception_is_not_thrown() {
        weakContext.clear()

        try {
            handler.handle(dataObject = getNotificationDataObject())
            println("Passed")
            assert(true)
        } catch (e: Exception) {
            println("Failed")
            assert(false)
        }
    }

    @Test
    fun given_contextIsNullAndErrorIsAlsoNull_withTransparent_then_handleShouldThrow_exception() {
        weakContext.clear()

        try {
            handler.handle(dataObject = getTransparentDataObject())
            Assert.fail("Should have thrown IllegalValueException when context is null")
        } catch (e: Exception) {
            assert(e is IllegalValueException)
        }
    }

    @Test
    fun handleShouldCall_successCallback_forCorrectNotificationData() {
        val success: Callback<Unit> = mock()
        handler.handle(dataObject = getNotificationDataObject(), result = success)

        verify(success).invoke(any())
    }


    @Test
    fun handleShouldCall_successCallback_forCorrectTransparentData() {
        val success: Callback<Unit> = mock()
        handler.handle(dataObject = getTransparentDataObject(), result = success)

        verify(success).invoke(any())
    }

    private fun getNotificationDataObject() = XiaomiCloudMessage(
        handle = "handle",
        payload = XiaomiPayload(
            data = emptyMap(),
            notification = XiaomiNotification()
        ),
        meta = XiaomiMeta(
            messageType = XiaomiMessageType.TOPIC,
            passThrough = XiaomiMeta.PASS_THROUGH_NOTIFICATION
        )
    )

    private fun getTransparentDataObject() = XiaomiCloudMessage(
        handle = "handle",
        payload = XiaomiPayload(
            data = emptyMap(),
            notification = XiaomiNotification()
        ),
        meta = XiaomiMeta(
            messageType = XiaomiMessageType.TOPIC,
            passThrough = XiaomiMeta.PASS_THROUGH_TRANSPARENT
        )
    )

    private fun getNotificationPublisher(context: Context): NotificationPublisher {
        return NotificationPublisher(
            weakContext = WeakReference(context),
            notificationChannelManager = NotificationChannelManager(),
            notificationIdGenerator = NotificationIdGenerator()
        )
    }

}