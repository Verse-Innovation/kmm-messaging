@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.messaging.cloud.fcm

import io.verse.messaging.core.push.PushMessageServiceSpec

expect open class FirebaseCloudMessagingServiceSpec : PushMessageServiceSpec<FirebaseCloudMessage> {

    override val provider: FirebaseCloudMessageServiceProvider

    override val pullKey: String?

    fun subscribe(topic: String)

    fun unsubscribe(topic: String)

}