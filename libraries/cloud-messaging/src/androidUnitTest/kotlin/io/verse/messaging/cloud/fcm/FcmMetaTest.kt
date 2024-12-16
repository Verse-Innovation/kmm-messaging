package io.verse.messaging.cloud.fcm

import io.tagd.core.ValidateException
import org.junit.Test

class FcmMetaTest {

    private lateinit var meta: FcmMeta

    @Test(expected = ValidateException::class)
    fun `if sentTime is less than 0 then validate must throw exception`() {
        meta = FcmMeta(
            sentTime = -1,
            ttl = 0,
            originalPriority = 0,
            priority = 0,
        )

        meta.validate()
    }

    @Test(expected = ValidateException::class)
    fun `if ttl is less than 0 then validate must throw exception`() {
        meta = FcmMeta(
            sentTime = 0,
            ttl = -1,
            originalPriority = 0,
            priority = 0,
        )

        meta.validate()
    }

}