package io.verse.messaging.android.fcm

import com.google.firebase.messaging.RemoteMessage
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler.Companion.DEFAULT_HANDLE
import io.verse.architectures.soa.io.ServiceDataObject.Companion.HANDLE_KEY
import io.verse.messaging.cloud.fcm.FcmMeta
import io.verse.messaging.cloud.fcm.FcmNotification
import io.verse.messaging.cloud.fcm.FcmPayload
import io.verse.messaging.cloud.fcm.FirebaseCloudMessage

fun RemoteMessage.toFcmServiceMessage(): FirebaseCloudMessage {
    return FirebaseCloudMessage(
        handle = data[HANDLE_KEY] ?: DEFAULT_HANDLE,
        payload = toFcmPayload(),
        meta = toFcmMeta()
    )
}

fun RemoteMessage.toFcmPayload() = FcmPayload(
    data = data,
    notification = notification?.toFcmNotification(),
)

fun RemoteMessage.Notification.toFcmNotification(): FcmNotification {
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

fun RemoteMessage.toFcmMeta() = FcmMeta(
    to = to,
    from = from,
    messageId = messageId,
    messageType = messageType,
    senderId = senderId,
    sentTime = sentTime,
    ttl = ttl,
    originalPriority = originalPriority,
    priority = priority,
    collapseKey = collapseKey,
)