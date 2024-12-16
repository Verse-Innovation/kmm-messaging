package io.verse.messaging.cloud.fcm

import io.tagd.arch.datatype.DataObject
import io.tagd.core.ValidateException
import io.verse.messaging.core.Message

open class FirebaseCloudMessage(
    handle: String,
    val payload: FcmPayload,
    val meta: FcmMeta,
) : Message(handle = handle) {

    override fun validate() {
        super.validate()
        payload.validate()
        meta.validate()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FirebaseCloudMessage

        if (payload != other.payload) return false
        if (meta != other.meta) return false

        return true
    }

    override fun hashCode(): Int {
        var result = payload.hashCode()
        result = 31 * result + meta.hashCode()
        return result
    }

    override fun toString(): String {
        return "[payload=$payload, meta=$meta]"
    }

}

open class FcmPayload(
    val data: Map<String, String>,
    val notification: FcmNotification?,
) : DataObject() {

    override fun validate() {
        super.validate()
        notification?.validate()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FcmPayload

        if (data != other.data) return false
        if (notification != other.notification) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + (notification?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "[data=$data, notification=$notification]"
    }

}

open class FcmNotification(
    val title: String?,
    val body: String?,
    val icon: String?,
    val clickAction: String?,
    val notificationPriority: Int?,
    val defaultLightSettings: Boolean,
    val defaultSound: Boolean,
    val defaultVibrateTimings: Boolean,
    val sticky: Boolean,
    val tag: String?,
) : DataObject() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FcmNotification

        if (title != other.title) return false
        if (body != other.body) return false
        if (icon != other.icon) return false
        if (clickAction != other.clickAction) return false
        if (notificationPriority != other.notificationPriority) return false
        if (defaultLightSettings != other.defaultLightSettings) return false
        if (defaultSound != other.defaultSound) return false
        if (defaultVibrateTimings != other.defaultVibrateTimings) return false
        if (sticky != other.sticky) return false
        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title?.hashCode() ?: 0
        result = 31 * result + (body?.hashCode() ?: 0)
        result = 31 * result + (icon?.hashCode() ?: 0)
        result = 31 * result + (clickAction?.hashCode() ?: 0)
        result = 31 * result + (notificationPriority?.hashCode() ?: 0)
        result = 31 * result + defaultLightSettings.hashCode()
        result = 31 * result + defaultSound.hashCode()
        result = 31 * result + defaultVibrateTimings.hashCode()
        result = 31 * result + sticky.hashCode()
        result = 31 * result + (tag?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "[title=$title, body=$body, icon=$icon, clickAction=$clickAction, " +
                "notificationPriority=$notificationPriority, " +
                "defaultLightSettings=$defaultLightSettings, " +
                "defaultSound=$defaultSound, defaultVibrateTimings=$defaultVibrateTimings, " +
                "sticky=$sticky, tag=$tag]"
    }

}

open class FcmMeta(
    val to: String? = null,
    val from: String? = null,
    val messageId: String? = null,
    val messageType: String? = null,
    val senderId: String? = null,
    val sentTime: Long,
    val ttl: Int,
    val originalPriority: Int,
    val priority: Int,
    val collapseKey: String? = null,
) : DataObject() {

    override fun validate() {
        if (sentTime < 0) {
            throw ValidateException(this, "sentTime must be greater than 0")
        }
        if (ttl < 0) {
            throw ValidateException(this, "ttl must be greater than 0")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FcmMeta

        if (to != other.to) return false
        if (from != other.from) return false
        if (messageId != other.messageId) return false
        if (messageType != other.messageType) return false
        if (senderId != other.senderId) return false
        if (sentTime != other.sentTime) return false
        if (ttl != other.ttl) return false
        if (originalPriority != other.originalPriority) return false
        if (priority != other.priority) return false
        if (collapseKey != other.collapseKey) return false

        return true
    }

    override fun hashCode(): Int {
        var result = to?.hashCode() ?: 0
        result = 31 * result + (from?.hashCode() ?: 0)
        result = 31 * result + (messageId?.hashCode() ?: 0)
        result = 31 * result + (messageType?.hashCode() ?: 0)
        result = 31 * result + (senderId?.hashCode() ?: 0)
        result = 31 * result + sentTime.hashCode()
        result = 31 * result + ttl.hashCode()
        result = 31 * result + originalPriority.hashCode()
        result = 31 * result + priority.hashCode()
        result = 31 * result + (collapseKey?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "[to=$to, from=$from, messageId=$messageId, messageType=$messageType, " +
                "senderId=$senderId, sentTime=$sentTime, ttl=$ttl, " +
                "originalPriority=$originalPriority, priority=$priority, " +
                "collapseKey=$collapseKey]"
    }

}