package io.verse.messaging.android.vendor.huawei

import io.tagd.core.dependencies
import io.tagd.langx.IllegalValueException
import io.verse.messaging.android.vendor.huawei.HuaweiCloudMessagingInitializer.Companion.ARG_CONFIG
import org.junit.Test

class HuaweiCloudAbstractAndroidMessagingInitializerTest {

    private val initializer = HuaweiCloudMessagingInitializer()

    @Test(expected = IllegalValueException::class)
    fun `new(dependencies) must throw an IllegalValueException if dependencies is not present`() {
        initializer.new(dependencies())
    }

    @Test(expected = ClassCastException::class)
    fun `new(dependencies) must throw an CastException if dependencies is not of correct type`() {
        initializer.new(
            dependencies = dependencies(
                ARG_CONFIG to "random dependency",
            )
        )
    }

}