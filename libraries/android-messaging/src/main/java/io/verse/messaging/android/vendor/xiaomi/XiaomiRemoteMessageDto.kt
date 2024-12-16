package io.verse.messaging.android.vendor.xiaomi

import com.google.gson.annotations.SerializedName
import com.xiaomi.mipush.sdk.MiPushMessage
import io.tagd.arch.datatype.DataObject
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler
import io.verse.architectures.soa.io.ServiceDataObject

data class XiaomiRemoteMessageDto(
    @SerializedName("extra")
    val extra: Map<String, String> = mapOf(),
    @SerializedName("messageId")
    val messageId: String? = null,
    @SerializedName("notifyId")
    val notifyId: Int? = null,
    @SerializedName("notifyType")
    val notifyType: Int? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("isNotified")
    val isNotified: Boolean = false,
    @SerializedName("messageType")
    val messageType: Int? = null,
    @SerializedName("topic")
    val topic: String? = null,
    @SerializedName("alias")
    val alias: String? = null,
    @SerializedName("userAccount")
    val userAccount: String? = null,
    @SerializedName("passThrough")
    val passThrough: Int? = null,
) : DataObject() {

    fun toXiaomiPushMessage(): XiaomiCloudMessage {
        return XiaomiCloudMessage(
            handle = extra[ServiceDataObject.HANDLE_KEY] ?: ServiceDataObjectHandler.DEFAULT_HANDLE,
            payload = toXiaomiPayload(),
            meta = toXiaomiMeta()
        )
    }


    private fun toXiaomiPayload() = XiaomiPayload(
        data = extra,
        notification = toXiaomiNotification(),
    )

    private fun toXiaomiNotification(): XiaomiNotification {
        return XiaomiNotification(
            notifyId = notifyId,
            notifyType = notifyType,
            title = title,
            description = description,
            content = content,
            category = category,
            isNotified = isNotified,
        )
    }

    private fun toXiaomiMeta() = XiaomiMeta(
        messageId = messageId,
        messageType = getXiaomiMessageType(),
        content = content,
        topic = topic,
        alias = alias,
        userAccount = userAccount,
        passThrough = passThrough ?: XiaomiMeta.PASS_THROUGH_TRANSPARENT,
    )

    private fun getXiaomiMessageType(): XiaomiMessageType {
        return when (messageType) {
            MiPushMessage.MESSAGE_TYPE_REG -> XiaomiMessageType.REG
            MiPushMessage.MESSAGE_TYPE_TOPIC -> XiaomiMessageType.TOPIC
            MiPushMessage.MESSAGE_TYPE_ALIAS -> XiaomiMessageType.ALIAS
            MiPushMessage.MESSAGE_TYPE_ACCOUNT -> XiaomiMessageType.ACCOUNT
            else -> XiaomiMessageType.INVALID
        }
    }
}