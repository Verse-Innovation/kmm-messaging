package io.verse.messaging.android.vendor.huawei

import com.huawei.hms.push.RemoteMessage
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

class HuaweiCloudMessagingServiceGatewayTest {

    private val serviceSpec: HuaweiCloudMessageServiceSpec = mock()
    private val dispatcher: ServiceDataObjectDispatcher<HuaweiCloudMessage, Unit> = mock()
    private lateinit var gateway: HuaweiCloudMessagingServiceGateway

    @Before
    fun setup() {
        FakeInjector.inject()
        whenever(serviceSpec.dispatcher).thenReturn(dispatcher)
        bindHms()
        gateway = HuaweiCloudMessagingServiceGateway()
    }

    private fun bindHms() {
        HuaweiCloudMessaging.Builder()
            .service(serviceSpec)
            .build()
    }

    @After
    fun tearDown() {
        FakeInjector.release()
    }

    @Test
    fun `onMessage should call dispatcher's dispatch`() {
        val message: RemoteMessage = mock()
        gateway.onMessageReceived(message)

        verify(serviceSpec).dispatcher
        verify(dispatcher).dispatch(any(), any(), any())
        verifyNoMoreInteractions(serviceSpec)
    }

    @Test
    fun `onReceive should call consumer's consumer`() {
        val message: HuaweiCloudMessage = mock()
        gateway.onReceive(message)

        verify(serviceSpec).dispatcher
        verify(dispatcher).dispatch(any(), any(), any())
        verifyNoMoreInteractions(serviceSpec)
    }

    @Test
    fun `release should release resources`() {
        gateway.release()

        assertEquals(null, gateway.service)
        assertEquals(null, gateway.dispatcher)
    }

}