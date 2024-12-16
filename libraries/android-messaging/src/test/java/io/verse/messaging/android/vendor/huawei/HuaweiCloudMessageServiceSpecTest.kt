package io.verse.messaging.android.vendor.huawei

import com.huawei.hms.push.HmsMessaging
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class HuaweiCloudMessageServiceSpecTest {

    private val hmsMessaging = mock<HmsMessaging>()
    private lateinit var serviceSpec: HuaweiCloudMessageServiceSpec

    @Before
    fun setup() {
        serviceSpec = HuaweiCloudMessageServiceSpec(
            provider = mock(),
            subscriptionProfile = mock(),
            dispatcher = mock(),
            gateway = mock(),
            hmsMessaging = hmsMessaging,
            tokenHandler = mock()
        )
    }

    @Test
    fun `autoInit should set hmsMessaging's autoInit to true`() {
        serviceSpec.autoInit()

        verify(hmsMessaging).isAutoInitEnabled = true
    }

    @Test
    fun `turnOnPushNotifications should call hmsMessaging's turnOnPush`() {
        whenever(hmsMessaging.turnOnPush()).thenReturn(mock())
        serviceSpec.turnOnPushNotifications()

        verify(hmsMessaging).turnOnPush()
    }

    @Test
    fun `turnOffPushNotifications should call hmsMessaging's turnOffPush`() {
        whenever(hmsMessaging.turnOffPush()).thenReturn(mock())
        serviceSpec.turnOffPushNotifications()

        verify(hmsMessaging).turnOffPush()
    }

    @Test
    fun `subscribe should call hmsMessaging's subscribe`() {
        val topic = "topic"
        serviceSpec.subscribe(topic)

        verify(hmsMessaging).subscribe(topic)
    }

    @Test
    fun `unsubscribe should call hmsMessaging's unsubscribe`() {
        val topic = "topic"
        serviceSpec.unsubscribe(topic)

        verify(hmsMessaging).unsubscribe(topic)
    }

}