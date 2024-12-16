package io.verse.messaging.android.vendor.huawei

import com.google.gson.annotations.SerializedName
import io.tagd.arch.datatype.DataObject
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler
import io.verse.architectures.soa.io.ServiceDataObject

data class HuaweiRemoteMessageDto(
    @SerializedName("data")
    val data: Map<String, String> = mapOf(),
    @SerializedName("notification")
    val notification: Notification?,
    @SerializedName("to")
    val to: String?,
    @SerializedName("from")
    val from: String?,
    @SerializedName("messageId")
    val messageId: String?,
    @SerializedName("messageType")
    val messageType: String?,
    @SerializedName("sentTime")
    val sentTime: Long?,
    @SerializedName("ttl")
    val ttl: Int?,
    @SerializedName("originalUrgency")
    val originalUrgency: Int?,
    @SerializedName("urgency")
    val urgency: Int?,
    @SerializedName("collapseKey")
    val collapseKey: String?,
) : DataObject() {

    data class Notification(
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("body")
        val body: String? = null,
        @SerializedName("icon")
        val icon: String? = null,
        @SerializedName("clickAction")
        val clickAction: String? = null,
        @SerializedName("importance")
        val importance: Int? = null,
        @SerializedName("isDefaultLight")
        val isDefaultLight: Boolean = false,
        @SerializedName("isDefaultSound")
        val isDefaultSound: Boolean = false,
        @SerializedName("isDefaultVibrate")
        val isDefaultVibrate: Boolean = false,
        @SerializedName("tag")
        val tag: String? = null,
        @SerializedName("channelId")
        val channelId: String? = null,
    ) : DataObject() {
        fun toHuaweiPushNotification(): HuaweiPushNotification {
            return HuaweiPushNotification(
                title = title,
                body = body,
                icon = icon,
                clickAction = clickAction,
                notificationPriority = importance,
                defaultLightSettings = isDefaultLight,
                defaultSound = isDefaultSound,
                defaultVibrateTimings = isDefaultVibrate,
                tag = tag,
                channelId = channelId,
            )
        }
    }

    fun toHuaweiCloudMessage(): HuaweiCloudMessage {
        return HuaweiCloudMessage(
            handle = data[ServiceDataObject.HANDLE_KEY]
                ?: ServiceDataObjectHandler.DEFAULT_HANDLE,
            payload = toHuaweiPushPayload(),
            meta = toHuaweiPushMeta()
        )
    }

    private fun toHuaweiPushPayload() = HuaweiPushPayload(
        data = data,
        notification = notification?.toHuaweiPushNotification(),
    )

    private fun toHuaweiPushMeta() = HuaweiPushMeta(
        to = to,
        from = from,
        messageId = messageId,
        messageType = messageType,
        sentTime = sentTime ?: 0,
        ttl = ttl ?: 0,
        originalPriority = originalUrgency ?: 0,
        priority = urgency ?: 0,
        collapseKey = collapseKey,
    )
}
