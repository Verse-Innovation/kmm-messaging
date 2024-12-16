package io.verse.messaging.android.vendor.huawei

import android.os.Bundle
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import io.tagd.arch.access.library
import io.tagd.arch.domain.crosscutting.async.compute
import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher

class HuaweiCloudMessagingServiceGateway : HmsMessageService(), HuaweiPushMessageServiceGateway {

    private var library: HuaweiCloudMessaging? = library()

    override var service: HuaweiCloudMessageServiceSpec? = library?.serviceSpec

    override var dispatcher: ServiceDataObjectDispatcher<HuaweiCloudMessage, Unit>? =
        library?.serviceSpec?.dispatcher

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        compute {
            super.onMessageReceived(remoteMessage)
            onReceive(dataObject = remoteMessage.toHuaweiPushMessage())
        }
    }

    override fun onReceive(dataObject: HuaweiCloudMessage) {
        dispatcher?.dispatch(dataObject, result = {
            println("processed hms message")
        }, error = {
            it.printStackTrace()
        })
    }

    override fun onNewToken(token: String, bundle: Bundle) {
        super.onNewToken(token)
        service?.tokenHandler?.onNewToken(token)
    }

    override fun release() {
        library = null
        service = null
        dispatcher = null
    }

}