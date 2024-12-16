package io.verse.messaging.android.pull

import com.google.gson.annotations.SerializedName
import io.verse.architectures.soa.io.PullServiceResponse
import io.verse.messaging.android.vendor.huawei.HuaweiRemoteMessageDto
import io.verse.messaging.android.vendor.xiaomi.XiaomiRemoteMessageDto
import io.verse.messaging.cloud.fcm.FirebaseRemoteMessageDto
import io.verse.messaging.core.Message

class PullMessageResponse(
    @SerializedName("fcm")
    val firebase: List<FirebaseRemoteMessageDto>?,
    @SerializedName("huawei")
    val huawei: List<HuaweiRemoteMessageDto>?,
    @SerializedName("xiaomi")
    val xiaomi: List<XiaomiRemoteMessageDto>?,
): PullServiceResponse<Message>()