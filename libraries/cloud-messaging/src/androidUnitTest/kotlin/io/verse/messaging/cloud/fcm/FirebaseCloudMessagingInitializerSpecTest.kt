package io.verse.messaging.cloud.fcm

import io.tagd.core.dependencies
import io.tagd.langx.IllegalValueException
import io.verse.messaging.cloud.fcm.FirebaseCloudMessagingInitializer.Companion.ARG_CONFIG
import org.junit.Test

class FirebaseCloudMessagingInitializerSpecTest {

    private val initializer = FirebaseCloudMessagingInitializer()

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