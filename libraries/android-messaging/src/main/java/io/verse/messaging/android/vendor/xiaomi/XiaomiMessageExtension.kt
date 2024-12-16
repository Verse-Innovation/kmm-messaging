package io.verse.messaging.android.vendor.xiaomi

import com.xiaomi.mipush.sdk.ErrorCode
import com.xiaomi.mipush.sdk.MiPushCommandMessage
import com.xiaomi.mipush.sdk.MiPushMessage
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler.Companion.DEFAULT_HANDLE
import io.verse.architectures.soa.io.ServiceDataObject.Companion.HANDLE_KEY

fun MiPushCommandMessage.hasSuccessResult(): Boolean {
    return this.resultCode == ErrorCode.SUCCESS.toLong()
}

fun MiPushMessage.toXiaomiPushMessage(): XiaomiCloudMessage {
    return XiaomiCloudMessage(
        handle = extra[HANDLE_KEY] ?: DEFAULT_HANDLE,
        payload = toXiaomiPayload(),
        meta = toXiaomiMeta()
    )
}

fun MiPushMessage.toXiaomiPayload() = XiaomiPayload(
    data = extra,
    notification = toXiaomiNotification(),
)

fun MiPushMessage.toXiaomiNotification(): XiaomiNotification {
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

fun MiPushMessage.toXiaomiMeta() = XiaomiMeta(
    messageId = messageId,
    messageType = getXiaomiMessageType(),
    content = content,
    topic = topic,
    alias = alias,
    userAccount = userAccount,
    passThrough = passThrough,
)

fun MiPushMessage.getXiaomiMessageType(): XiaomiMessageType {
    return when (messageType) {
        MiPushMessage.MESSAGE_TYPE_REG -> XiaomiMessageType.REG
        MiPushMessage.MESSAGE_TYPE_TOPIC -> XiaomiMessageType.TOPIC
        MiPushMessage.MESSAGE_TYPE_ALIAS -> XiaomiMessageType.ALIAS
        MiPushMessage.MESSAGE_TYPE_ACCOUNT -> XiaomiMessageType.ACCOUNT
        else -> XiaomiMessageType.INVALID
    }
}