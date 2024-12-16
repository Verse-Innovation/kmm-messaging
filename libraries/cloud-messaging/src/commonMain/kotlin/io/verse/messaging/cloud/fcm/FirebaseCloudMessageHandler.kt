@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.messaging.cloud.fcm

import io.verse.messaging.core.DefaultMessageHandler

expect class FirebaseCloudMessageHandler: DefaultMessageHandler<FirebaseCloudMessage>