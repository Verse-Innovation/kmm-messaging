package io.verse.messaging.android.vendor.huawei

import com.huawei.hms.push.RemoteMessage
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler.Companion.DEFAULT_HANDLE
import io.verse.architectures.soa.io.ServiceDataObject.Companion.HANDLE_KEY

fun RemoteMessage.toHuaweiPushMessage(): HuaweiCloudMessage {
    return HuaweiCloudMessage(
        handle = dataOfMap[HANDLE_KEY] ?: DEFAULT_HANDLE,
        payload = toHuaweiPushPayload(),
        meta = toHuaweiPushMeta()
    )
}

fun RemoteMessage.toHuaweiPushPayload() = HuaweiPushPayload(
    data = dataOfMap,
    notification = notification?.toHuaweiPushNotification(),
)

fun RemoteMessage.Notification.toHuaweiPushNotification(): HuaweiPushNotification {
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

fun RemoteMessage.toHuaweiPushMeta() = HuaweiPushMeta(
    to = to,
    from = from,
    messageId = messageId,
    messageType = messageType,
    sentTime = sentTime,
    ttl = ttl,
    originalPriority = originalUrgency,
    priority = urgency,
    collapseKey = collapseKey,
)