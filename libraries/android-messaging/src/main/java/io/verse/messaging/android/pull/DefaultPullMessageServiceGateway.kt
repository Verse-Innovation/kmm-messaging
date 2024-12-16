package io.verse.messaging.android.pull

import io.tagd.arch.access.library
import io.tagd.arch.domain.crosscutting.async.networkIO
import io.tagd.langx.Callback
import io.tagd.langx.IllegalAccessException
import io.verse.architectures.soa.io.ServiceDataObject
import io.verse.architectures.soa.service.SubscribablePushService
import io.verse.latch.core.ExecutionException
import io.verse.latch.core.InterceptorGateway
import io.verse.latch.core.Latch
import io.verse.latch.core.Request
import io.verse.latch.core.ResultContext
import io.verse.latch.core.newHttpsGetRequestBuilder
import io.verse.messaging.android.vendor.huawei.HuaweiCloudMessage
import io.verse.messaging.android.vendor.huawei.HuaweiCloudMessageServiceSpec
import io.verse.messaging.android.vendor.huawei.HuaweiRemoteMessageDto
import io.verse.messaging.android.vendor.xiaomi.XiaomiCloudMessage
import io.verse.messaging.android.vendor.xiaomi.XiaomiCloudMessageServiceSpec
import io.verse.messaging.android.vendor.xiaomi.XiaomiRemoteMessageDto
import io.verse.messaging.cloud.fcm.FirebaseCloudMessage
import io.verse.messaging.cloud.fcm.FirebaseCloudMessagingServiceSpec
import io.verse.messaging.cloud.fcm.FirebaseRemoteMessageDto
import io.verse.messaging.core.Message
import io.verse.messaging.core.pull.PullMessageServiceGateway
import io.verse.messaging.core.pull.PullMessaging

open class DefaultPullMessageServiceGateway :
    InterceptorGateway<String, PullMessageResponse, List<Message>>(),
    PullMessageServiceGateway<PullMessageRequest> {

    private val library
        get() = library<PullMessaging>()

    override val service: DefaultPullMessageServiceSpec?
        get() = defaultPullService()

    private val pushServices =
        hashMapOf<String, SubscribablePushService<*, *>>()

    override fun <D : ServiceDataObject, RESULT> put(
        pushService: SubscribablePushService<D, RESULT>
    ): DefaultPullMessageServiceGateway {

        pushServices[pushService.pullKey!!] = pushService
        return this
    }

    @Suppress("UNCHECKED_CAST")
    override fun <D : ServiceDataObject, RESULT, S : SubscribablePushService<D, RESULT>> get(
        pullKey: String,
    ): S? {

        return pushServices[pullKey] as S
    }

    override fun fetch(
        request: PullMessageRequest,
        success: Callback<List<Message>>?,
        failure: Callback<Throwable>?
    ) {

        networkIO {
            val jsonRequest = newHttpsGetRequest(request)
            jsonRequest?.let {
                fire(jsonRequest, success, failure)
            } ?: throw IllegalAccessException("failed to create request")
        }
    }

    private fun newHttpsGetRequest(
        config: PullMessageRequest
    ): Request<String, PullMessageResponse>? {

        val latchLibrary = library<Latch>()
        return latchLibrary
            ?.newHttpsGetRequestBuilder(config.url, this)
            ?.build()
    }

    override fun success(
        context: ResultContext<String, PullMessageResponse>,
        result: PullMessageResponse
    ) {

        result.firebase?.forEach {
            dispatchFirebaseCloudMessage(it)
        }
        result.huawei?.forEach {
            dispatchHuaweiCloudMessage(it)
        }
        result.xiaomi?.forEach {
            dispatchXiaomiCloudMessage(it)
        }
    }

    override fun failure(exception: ExecutionException) {
        val requestContext = requestContext(exception.identifier)
        requestContext?.failure?.invoke(exception)
    }

    private fun dispatchFirebaseCloudMessage(message: FirebaseRemoteMessageDto) {
        dispatchCloudMessage<FirebaseCloudMessage, FirebaseCloudMessagingServiceSpec>(
            pullKey = FirebaseCloudMessagingServiceSpec.PULL_KEY,
            message = message.toFcmServiceMessage()
        )
    }

    private fun dispatchHuaweiCloudMessage(message: HuaweiRemoteMessageDto) {
        dispatchCloudMessage<HuaweiCloudMessage, HuaweiCloudMessageServiceSpec>(
            pullKey = HuaweiCloudMessageServiceSpec.PULL_KEY,
            message = message.toHuaweiCloudMessage()
        )
    }

    private fun dispatchXiaomiCloudMessage(message: XiaomiRemoteMessageDto) {
        dispatchCloudMessage<XiaomiCloudMessage, XiaomiCloudMessageServiceSpec>(
            pullKey = XiaomiCloudMessageServiceSpec.PULL_KEY,
            message = message.toXiaomiPushMessage()
        )
    }

    private fun <M : Message, S : SubscribablePushService<M, Unit>> dispatchCloudMessage(
        pullKey: String,
        message: M,
    ) {

        get<M, Unit, S>(pullKey)?.dispatcher?.dispatch(message)
    }

    override fun release() {
        pushServices.clear()
    }
}