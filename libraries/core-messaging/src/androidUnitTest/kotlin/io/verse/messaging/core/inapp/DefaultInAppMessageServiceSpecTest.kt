package io.verse.messaging.core.inapp

import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify


class DefaultInAppMessageServiceSpecTest {

    private lateinit var inAppMessageServiceSpec: InAppMessageServiceSpec
    private lateinit var inAppMessageServiceGateway: InAppMessageServiceGateway

    @Test
    fun `given InAppMessageServiceSpec then verify publish delegates gateway to publish`() {
        // given
        inAppMessageServiceGateway = mock()
        inAppMessageServiceSpec = DefaultInAppMessageServiceSpec(
            provider = mock(),
            subscriptionProfile = mock(),
            gateway = inAppMessageServiceGateway
        )

        // then
        val inAppMessage: InAppMessage<InAppMessageWidget> = mock()
        inAppMessageServiceSpec.publish(inAppMessage)

        // verify
        verify(inAppMessageServiceGateway).publish(inAppMessage)
    }
}