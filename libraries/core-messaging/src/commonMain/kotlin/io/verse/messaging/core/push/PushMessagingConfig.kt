package io.verse.messaging.core.push

import io.tagd.arch.datatype.DataObject

data class PushMessagingConfig(
    val tokenRefreshUrl: String? = null
): DataObject()