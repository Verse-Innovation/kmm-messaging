package io.verse.messaging.core.notification

import io.tagd.arch.datatype.DataObject

/**
 * This class is a wrapper around [Notification]
 */
class PublishableNotification(
    val notification: Notification,
    val channelId: String,
    val channelName: String,
) : DataObject() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PublishableNotification

        if (notification != other.notification) return false
        if (channelId != other.channelId) return false
        if (channelName != other.channelName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = notification.hashCode()
        result = 31 * result + channelId.hashCode()
        result = 31 * result + channelName.hashCode()
        return result
    }

    override fun toString(): String {
        return "[notification=$notification, channelId='$channelId', channelName='$channelName']"
    }

}