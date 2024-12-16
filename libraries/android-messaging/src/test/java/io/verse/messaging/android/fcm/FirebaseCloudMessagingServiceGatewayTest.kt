package io.verse.messaging.android.fcm

import com.google.firebase.messaging.RemoteMessage
import io.tagd.arch.test.FakeInjector
import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher
import io.verse.messaging.cloud.fcm.FirebaseCloudMessage
import io.verse.messaging.cloud.fcm.FirebaseCloudMessaging
import io.verse.messaging.cloud.fcm.FirebaseCloudMessagingServiceSpec
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class FirebaseCloudMessagingServiceGatewayTest {

    private val serviceSpec: FirebaseCloudMessagingServiceSpec = mock()
    private val dispatcher: ServiceDataObjectDispatcher<FirebaseCloudMessage, Unit> = mock()
    private lateinit var gateway: FirebaseCloudMessagingServiceGateway

    @Before
    fun setup() {
        FakeInjector.inject()
        whenever(serviceSpec.dispatcher).thenReturn(dispatcher)
        bindFcm()
        gateway = FirebaseCloudMessagingServiceGateway()
    }

    private fun bindFcm() {
        FirebaseCloudMessaging.Builder()
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
        val message: FirebaseCloudMessage = mock()
        gateway.onReceive(message)

        verify(serviceSpec).dispatcher
        verify(dispatcher).dispatch(any(), any(), any())
        verifyNoMoreInteractions(serviceSpec)
    }

    @Test
    fun `release should release resources`() {
        gateway.release()

        Assert.assertEquals(null, gateway.service)
        Assert.assertEquals(null, gateway.dispatcher)
    }

}