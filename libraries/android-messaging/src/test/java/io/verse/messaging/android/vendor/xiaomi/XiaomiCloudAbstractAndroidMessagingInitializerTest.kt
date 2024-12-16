package io.verse.messaging.android.vendor.xiaomi

import io.tagd.core.dependencies
import org.junit.Test

class XiaomiCloudAbstractAndroidMessagingInitializerTest {

    private val initializer = XiaomiCloudMessagingInitializer()

    @Test(expected = Throwable::class)
    fun `new should throw an error if configXiaomiCloudMessagingConfig is not present`() {
        initializer.new(dependencies())
    }

}
