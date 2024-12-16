package io.verse.messaging.cloud.fcm

import io.tagd.arch.datatype.DataObject
import io.tagd.arch.domain.crosscutting.codec.SerializedName
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler
import io.verse.architectures.soa.io.ServiceDataObject

data class FirebaseRemoteMessageDto(
    @SerializedName("data", [])
    val data: Map<String, String> = mapOf(),
    @SerializedName("notification", [])
    val notification: Notification?,
    @SerializedName("to", [])
    val to: String? = null,
    @SerializedName("from", [])
    val from: String? = null,
    @SerializedName("messageId", [])
    val messageId: String? = null,
    @SerializedName("messageType", [])
    val messageType: String? = null,
    @SerializedName("senderId", [])
    val senderId: String? = null,
    @SerializedName("sentTime", [])
    val sentTime: Long? = null,
    @SerializedName("ttl", [])
    val ttl: Int? = null,
    @SerializedName("originalPriority", [])
    val originalPriority: Int? = null,
    @SerializedName("priority", [])
    val priority: Int? = null,
    @SerializedName("collapseKey", [])
    val collapseKey: String? = null,
) : DataObject() {

    data class Notification(
        @SerializedName("title", [])
        val title: String? = null,
        @SerializedName("body", [])
        val body: String? = null,
        @SerializedName("icon", [])
        val icon: String? = null,
        @SerializedName("clickAction", [])
        val clickAction: String? = null,
        @SerializedName("notificationPriority", [])
        val notificationPriority: Int? = null,
        @SerializedName("defaultLightSettings", [])
        val defaultLightSettings: Boolean = false,
        @SerializedName("defaultSound", [])
        val defaultSound: Boolean = false,
        @SerializedName("defaultVibrateTimings", [])
        val defaultVibrateSettings: Boolean = false,
        @SerializedName("sticky", [])
        val sticky: Boolean = false,
        @SerializedName("tag", [])
        val tag: String? = null,
    ) : DataObject() {

        fun toFcmNotification(): FcmNotification {
            return FcmNotification(
                title = title,
                body = body,
                icon = icon,
                clickAction = clickAction,
                notificationPriority = notificationPriority,
                defaultLightSettings = defaultLightSettings,
                defaultSound = defaultSound,
                defaultVibrateTimings = defaultVibrateSettings,
                sticky = sticky,
                tag = tag,
            )
        }
    }

    fun toFcmServiceMessage(): FirebaseCloudMessage {
        return FirebaseCloudMessage(
            handle = data[ServiceDataObject.HANDLE_KEY] ?: ServiceDataObjectHandler.DEFAULT_HANDLE,
            payload = toFcmPayload(),
            meta = toFcmMeta()
        )
    }

    private fun toFcmPayload() = FcmPayload(
        data = data,
        notification = notification?.toFcmNotification(),
    )

    private fun toFcmMeta() = FcmMeta(
        to = to,
        from = from,
        messageId = messageId,
        messageType = messageType,
        senderId = senderId,
        sentTime = sentTime ?: 0L,
        ttl = ttl ?: 0,
        originalPriority = originalPriority ?: 0,
        priority = priority ?: 0,
        collapseKey = collapseKey,
    )
}