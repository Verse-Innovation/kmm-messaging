package io.verse.messaging.android.pull

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.tagd.arch.access.bind
import io.tagd.arch.scopable.library.Library
import io.verse.architectures.soa.service.SubscribablePushService
import io.verse.messaging.core.Message
import io.verse.messaging.core.pull.DefaultPullMessageServiceProvider
import io.verse.messaging.core.pull.PullMessaging
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DefaultPullMessageServiceGatewayTest {

    private val service = mock<DefaultPullMessageServiceSpec>()
    private val gateway = DefaultPullMessageServiceGateway()

    @Before
    fun setup() {
        val pullMessaging = mock<PullMessaging>().also {
            bind<Library, PullMessaging>(instance = it)
        }
        val serviceProvider = mock<DefaultPullMessageServiceProvider>()

        whenever(pullMessaging.get(DefaultPullMessageServiceProvider::class))
            .thenReturn(serviceProvider)
        whenever(serviceProvider.service(DefaultPullMessageServiceSpec::class))
            .thenReturn(service)
    }

    @Test
    fun `gateway should be able to store and fetch push service correctly`() {
        val pushService = mock<SubscribablePushService<Message, Unit>>()
        val pullKey = "pull_key"
        whenever(pushService.pullKey).thenReturn(pullKey)

        gateway.put(pushService)

        assertEquals(
            pushService,
            gateway.get<Message, Unit, SubscribablePushService<Message, Unit>>(pullKey)
        )
    }

}