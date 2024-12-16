package io.verse.messaging.android.pull

import io.verse.architectures.soa.io.PullServiceRequest

data class PullMessageRequest(
    override val url: String,
    val filters: List<String> = listOf(FILTER_FCM)
) : PullServiceRequest(url = url) {

    companion object {
        const val FILTER_FCM = "fcm"
        const val FILTER_HUAWEI = "huawei"
        const val FILTER_XIAOMI = "xiaomi"
    }
}