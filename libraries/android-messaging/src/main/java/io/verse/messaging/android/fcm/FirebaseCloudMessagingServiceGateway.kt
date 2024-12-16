package io.verse.messaging.android.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.tagd.arch.access.library
import io.tagd.arch.domain.crosscutting.async.compute
import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher
import io.verse.messaging.cloud.fcm.FirebaseCloudMessage
import io.verse.messaging.cloud.fcm.FirebaseCloudMessaging
import io.verse.messaging.cloud.fcm.FirebaseCloudMessagingServiceSpec
import io.verse.messaging.cloud.fcm.FirebasePushMessageServiceGateway

/**
 * This class is responsible for receiving the [RemoteMessage] from Firebase
 * and passing it to the [ServiceDataObjectDispatcher]
 */
class FirebaseCloudMessagingServiceGateway : FirebasePushMessageServiceGateway,
    FirebaseMessagingService() {

    private var library: FirebaseCloudMessaging? = library()

    override var service: FirebaseCloudMessagingServiceSpec? = library?.serviceSpec

    override var dispatcher: ServiceDataObjectDispatcher<FirebaseCloudMessage, Unit>? =
        library?.serviceSpec?.dispatcher

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        compute {
            super.onMessageReceived(remoteMessage)
            onReceive(dataObject = remoteMessage.toFcmServiceMessage())
        }
    }

    override fun onReceive(dataObject: FirebaseCloudMessage) {
        dispatcher?.dispatch(dataObject, result = {
            println("processed fcm message")
        }, error = {
            it.printStackTrace()
        })
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        service?.tokenHandler?.onNewToken(token)
    }

    override fun release() {
        library = null
        service = null
        dispatcher = null
    }

}