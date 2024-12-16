package io.verse.messaging.core.notification

import io.tagd.core.Service
import io.tagd.langx.System

/**
 * class for generating notification id
 */
open class NotificationIdGenerator : Service {

    fun generateId(): Int {
        return System.millis().toInt()
    }

    override fun release() {
        // no-op
    }

}