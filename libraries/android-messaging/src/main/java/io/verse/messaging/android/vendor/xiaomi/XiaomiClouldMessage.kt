package io.verse.messaging.android.vendor.xiaomi

import io.tagd.arch.datatype.DataObject
import io.tagd.core.ValidateException
import io.verse.messaging.core.Message

open class XiaomiCloudMessage(
    handle: String,
    val payload: XiaomiPayload,
    val meta: XiaomiMeta,
) : Message(handle = handle) {

    override fun validate() {
        super.validate()
        payload.validate()
        meta.validate()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as XiaomiCloudMessage

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

open class XiaomiPayload(
    val data: Map<String, String>,
    val notification: XiaomiNotification,
) : DataObject() {

    override fun validate() {
        super.validate()
        notification.validate()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as XiaomiPayload

        if (data != other.data) return false
        if (notification != other.notification) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + notification.hashCode()
        return result
    }

    override fun toString(): String {
        return "[data=$data, notification=$notification]"
    }

}

open class XiaomiNotification(
    val notifyId: Int? = null,
    val notifyType: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val content: String? = null,
    val category: String? = null,
    val isNotified: Boolean = false,
) : DataObject() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is XiaomiNotification) return false

        if (notifyId != other.notifyId) return false
        if (notifyType != other.notifyType) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (content != other.content) return false
        if (category != other.category) return false
        if (isNotified != other.isNotified) return false

        return true
    }

    override fun hashCode(): Int {
        var result = notifyId ?: 0
        result = 31 * result + (notifyType ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + isNotified.hashCode()
        return result
    }

    override fun toString(): String {
        return "XiaomiNotification(notifyId=$notifyId, notifyType=$notifyType, title=$title, " +
                "description=$description, content=$content, category=$category, " +
                "isNotified=$isNotified)"
    }
}

enum class XiaomiMessageType {
    INVALID, REG, ALIAS, TOPIC, ACCOUNT
}

open class XiaomiMeta(
    val messageId: String? = null,
    val messageType: XiaomiMessageType,
    val content: String? = null,
    val category: String? = null,
    val userAccount: String? = null,
    val topic: String? = null,
    val alias: String? = null,
    val passThrough: Int,
) : DataObject() {

    val isNotificationMessage get() = passThrough == PASS_THROUGH_NOTIFICATION

    val isTransparentMessage get() = passThrough == PASS_THROUGH_TRANSPARENT

    override fun validate() {
        if (messageId.isNullOrBlank()) {
            throw ValidateException(this, "Message ID must not be null or blank")
        }
        if (messageType == XiaomiMessageType.INVALID) {
            throw ValidateException(this, "Invalid message type found")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is XiaomiMeta) return false

        if (messageId != other.messageId) return false
        if (messageType != other.messageType) return false
        if (content != other.content) return false
        if (category != other.category) return false
        if (userAccount != other.userAccount) return false
        if (topic != other.topic) return false
        if (alias != other.alias) return false
        if (passThrough != other.passThrough) return false

        return true
    }

    override fun hashCode(): Int {
        var result = messageId?.hashCode() ?: 0
        result = 31 * result + messageType.hashCode()
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + (userAccount?.hashCode() ?: 0)
        result = 31 * result + (topic?.hashCode() ?: 0)
        result = 31 * result + (alias?.hashCode() ?: 0)
        result = 31 * result + passThrough
        return result
    }

    companion object {
        const val PASS_THROUGH_NOTIFICATION = 0
        const val PASS_THROUGH_TRANSPARENT = 1
    }

}