package io.verse.messaging.android.vendor.xiaomi

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.xiaomi.mipush.sdk.MiPushMessage
import io.tagd.arch.test.FakeInjector
import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class XiaomiCloudMessagingServiceGatewayTest {

    private var serviceSpec: XiaomiCloudMessageServiceSpec = mock()
    private var dispatcher: ServiceDataObjectDispatcher<XiaomiCloudMessage, Unit> = mock()
    private lateinit var gateway: XiaomiCloudMessagingServiceGateway

    @Before
    fun setup() {
        FakeInjector.inject()
        whenever(serviceSpec.dispatcher).thenReturn(dispatcher)
        buildXiaomiCloudMessaging()
        gateway = XiaomiCloudMessagingServiceGateway()
    }

    private fun buildXiaomiCloudMessaging() {
        XiaomiCloudMessaging.Builder()
            .service(serviceSpec)
            .build()
    }

    @After
    fun tearDown() {
        FakeInjector.release()
    }

    @Test
    fun onReceivePassThroughMessage_shouldCall_dispatchersDispatch() {
        val message: MiPushMessage = mock()
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        gateway.onReceivePassThroughMessage(context, message)

        verify(serviceSpec).dispatcher
        verify(dispatcher).dispatch(any(), any(), any())
        verifyNoMoreInteractions(serviceSpec)
    }

    @Test
    fun onNotificationMessageArrived_shouldCall_ConsumersConsumer() {
        val message: MiPushMessage = mock()
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        gateway.onNotificationMessageArrived(context, message)

        verify(serviceSpec).dispatcher
        verify(dispatcher).dispatch(any(), any(), any())
        verifyNoMoreInteractions(serviceSpec)
    }


    @Test
    fun release_shouldRelease_resources() {
        gateway.release()

        assertEquals(null, gateway.service)
        assertEquals(null, gateway.dispatcher)
    }

}