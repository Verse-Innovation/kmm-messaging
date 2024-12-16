package io.verse.messaging.android.vendor.huawei

import android.content.ContextWrapper
import androidx.test.platform.app.InstrumentationRegistry
import io.tagd.langx.Callback
import io.tagd.langx.Context
import io.tagd.langx.IllegalValueException
import io.tagd.langx.ref.WeakReference
import io.verse.messaging.core.DefaultMessageHandler
import io.verse.messaging.core.Message
import io.verse.messaging.core.notification.NotificationPublisher
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class HuaweiCloudMessageHandlerTest {

    private val context = ContextWrapper(InstrumentationRegistry.getInstrumentation().targetContext)
    private val weakContext: WeakReference<Context> = WeakReference(context)
    private val notificationPublisher = mock<NotificationPublisher>()
    private val handler = DefaultMessageHandler<Message>(mock(), weakContext, notificationPublisher)

    @Test
    fun given_contextIsNull_then_handleShouldSend_errorCallback() {
        val error: Callback<Throwable> = mock()
        val errorCapture = argumentCaptor<Throwable>()
        weakContext.clear()

        handler.handle(dataObject = mock(), error = error)

        verify(error).invoke(errorCapture.capture())
        assert(errorCapture.firstValue is IllegalValueException)
        assertEquals("context is null", errorCapture.firstValue.message)
    }

    @Test(expected = IllegalValueException::class)
    fun given_contextIsNull_and_errorIsAlsoNull_then_handleShouldThrow_exception() {
        weakContext.clear()

        handler.handle(dataObject = mock())
    }

    @Test
    fun handleShouldCall_successCallback_forCorrectData() {
        val success: Callback<Unit> = mock()
        handler.handle(dataObject = mock(), result = success)

        verify(success).invoke(any())
    }

}