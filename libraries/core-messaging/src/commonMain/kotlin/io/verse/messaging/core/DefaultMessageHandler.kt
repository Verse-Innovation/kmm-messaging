@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.messaging.core

import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler

expect open class DefaultMessageHandler<T : Message> : ServiceDataObjectHandler<T, Unit>