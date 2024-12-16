package io.verse.messaging.android.vendor.xiaomi

import android.content.Context
import android.util.Log
import com.xiaomi.mipush.sdk.MiPushClient
import com.xiaomi.mipush.sdk.MiPushCommandMessage
import com.xiaomi.mipush.sdk.MiPushMessage
import com.xiaomi.mipush.sdk.PushMessageReceiver
import io.tagd.arch.domain.crosscutting.async.compute
import io.tagd.di.injectX
import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher


/**
 * This class is responsible for receiving the [MiPushMessage] from Xiaomi Push
 * and passing it to the [ServiceDataObjectDispatcher].
 */
class XiaomiCloudMessagingServiceGateway : XiaomiPushMessageServiceGateway, PushMessageReceiver() {

    private var library by injectX<XiaomiCloudMessaging>()

    override var service: XiaomiCloudMessageServiceSpec? = library?.serviceSpec

    override var dispatcher: ServiceDataObjectDispatcher<XiaomiCloudMessage, Unit>? =
        library?.serviceSpec?.dispatcher

    /**
     * onReceivePassThroughMessage is used to receive pass-through messages sent by the server.
     * Process transparent messages
     */
    override fun onReceivePassThroughMessage(
        context: Context?,
        miPushMessage: MiPushMessage?,
    ) {

        compute {
            Log.d(TAG, "onReceivePassThroughMessage: $miPushMessage")
            super.onReceivePassThroughMessage(context, miPushMessage)
            miPushMessage?.let { onReceive(it.toXiaomiPushMessage()) }
        }
    }

    /**
     * onNotificationMessageClicked is used to receive notification shade messages
     * sent by the server (Triggered when the user taps the notification shade).
     * Process the clicks of customized notification messages
     */
    override fun onNotificationMessageClicked(
        context: Context?,
        miPushMessage: MiPushMessage?,
    ) {
        //no-op
    }

    /**
     * onNotificationMessageArrived is used to receive notification shade messages
     * sent by the server (Triggered when the message arrives at the c client,
     * and can receive notification messages that the application does not display the notification
     * when the application is in the foreground).
     * Notification messages reach the device
     */
    override fun onNotificationMessageArrived(
        context: Context?,
        miPushMessage: MiPushMessage?,
    ) {

        compute {
            Log.d(TAG, "onNotificationMessageArrived: $miPushMessage")
            super.onNotificationMessageArrived(context, miPushMessage)
            miPushMessage?.let { onReceive(it.toXiaomiPushMessage()) }
        }
    }

    /**
     * onReceiveRegisterResult is used to receive responses returned after the client
     * sends a register command message to the server.
     * Receive registration result
     */
    override fun onReceiveRegisterResult(
        context: Context?,
        miPushCommandMessage: MiPushCommandMessage?,
    ) {

        compute {
            handleCommandResult(miPushCommandMessage)
        }
    }

    /**
     * onCommandResult is used to receive responses returned after the client
     * sends a command message to the server
     * Command result processing
     */
    override fun onCommandResult(
        context: Context?,
        miPushCommandMessage: MiPushCommandMessage?,
    ) {

        compute {
            handleCommandResult(miPushCommandMessage)
        }
    }

    /**
     * This interface will be called back when the required permissions are not obtained
     */
    override fun onRequirePermissions(context: Context?, p1: Array<out String>?) = compute {
        super.onRequirePermissions(context, p1)
    }

    override fun onReceive(dataObject: XiaomiCloudMessage) = compute {
        dispatcher?.dispatch(dataObject, result = {
            Log.d(TAG, "onReceive: processed xiaomi message")
        }, error = {
            it.printStackTrace()
        })
    }

    private fun handleCommandResult(miPushCommandMessage: MiPushCommandMessage?) {
        val message = miPushCommandMessage ?: return
        Log.d(TAG, "processCommandResult: $message")

        when (message.command) {
            MiPushClient.COMMAND_REGISTER -> handleRegisterCommandResult(message)
            MiPushClient.COMMAND_UNREGISTER -> handleUnRegisterCommandResult(message)
            MiPushClient.COMMAND_SET_ALIAS -> handleSetAliasCommandResult(message)
            MiPushClient.COMMAND_UNSET_ALIAS -> handleUnSetAliasCommandResult(message)
            MiPushClient.COMMAND_SUBSCRIBE_TOPIC -> handleSubscribeTopicCommandResult(message)
            MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC -> handleUnSubscribeTopicCommandResult(message)
            MiPushClient.COMMAND_SET_ACCOUNT -> handleSetAccountCommandResult(message)
            MiPushClient.COMMAND_UNSET_ACCOUNT -> handleUnSetAccountCommandResult(message)
            MiPushClient.COMMAND_SET_ACCEPT_TIME -> handleSetAcceptTimeCommandResult(message)
        }
    }

    private fun handleRegisterCommandResult(commandMessage: MiPushCommandMessage) {
        if (commandMessage.hasSuccessResult()) {
            val registrationId = commandMessage.commandArguments.firstOrNull()
            registrationId?.let {
                service?.tokenHandler?.onNewToken(it)
            }

            Log.d(TAG, "handleRegisterCommandResult: $registrationId")
        } else {
            Log.d(TAG, "handleRegisterCommandResult: ${commandMessage.reason}")
        }
    }

    private fun handleUnRegisterCommandResult(commandMessage: MiPushCommandMessage) {
        if (commandMessage.hasSuccessResult()) {
            val registrationId = commandMessage.commandArguments.firstOrNull()
            Log.d(TAG, "handleUnRegisterCommandResult: $registrationId")
        } else {
            Log.d(TAG, "handleUnRegisterCommandResult: ${commandMessage.reason}")
        }
    }

    private fun handleSubscribeTopicCommandResult(commandMessage: MiPushCommandMessage) {
        if (commandMessage.hasSuccessResult()) {
            val subscriptionTopic = commandMessage.commandArguments.firstOrNull()
            Log.d(TAG, "handleSubscribeTopicCommandResult: $subscriptionTopic")
        } else {
            Log.d(TAG, "handleSubscribeTopicCommandResult: ${commandMessage.reason}")
        }
    }

    private fun handleUnSubscribeTopicCommandResult(commandMessage: MiPushCommandMessage) {
        if (commandMessage.hasSuccessResult()) {
            val subscriptionTopic = commandMessage.commandArguments.firstOrNull()
            Log.d(TAG, "handleUnSubscribeTopicCommandResult: $subscriptionTopic")
        } else {
            Log.d(TAG, "handleUnSubscribeTopicCommandResult: ${commandMessage.reason}")
        }
    }

    private fun handleSetAccountCommandResult(commandMessage: MiPushCommandMessage) {
        if (commandMessage.hasSuccessResult()) {
            val userAccount = commandMessage.commandArguments.firstOrNull()
            Log.d(TAG, "handleSetAccountCommandResult: $userAccount")
        } else {
            Log.d(TAG, "handleSetAccountCommandResult: ${commandMessage.reason}")
        }
    }

    private fun handleUnSetAccountCommandResult(commandMessage: MiPushCommandMessage) {
        if (commandMessage.hasSuccessResult()) {
            val userAccount = commandMessage.commandArguments.firstOrNull()
            Log.d(TAG, "handleUnSetAccountCommandResult: $userAccount")
        } else {
            Log.d(TAG, "handleUnSetAccountCommandResult: ${commandMessage.reason}")
        }
    }

    private fun handleSetAliasCommandResult(commandMessage: MiPushCommandMessage) {
        if (commandMessage.hasSuccessResult()) {
            val alias = commandMessage.commandArguments.firstOrNull()
            Log.d(TAG, "handleSetAliasCommandResult: $alias")
        } else {
            Log.d(TAG, "handleSetAliasCommandResult: ${commandMessage.reason}")
        }
    }

    private fun handleUnSetAliasCommandResult(commandMessage: MiPushCommandMessage) {
        if (commandMessage.hasSuccessResult()) {
            val alias = commandMessage.commandArguments.firstOrNull()
            Log.d(TAG, "handleUnSetAliasCommandResult: $alias")
        } else {
            Log.d(TAG, "handleUnSetAliasCommandResult: ${commandMessage.reason}")
        }
    }

    private fun handleSetAcceptTimeCommandResult(commandMessage: MiPushCommandMessage) {
        if (commandMessage.hasSuccessResult()) {
            val startTime = commandMessage.commandArguments.firstOrNull()
            val endTime = commandMessage.commandArguments.getOrNull(1)
            Log.d(TAG, "handleSetAcceptTimeCommandResult: $startTime - $endTime")
        } else {
            Log.d(TAG, "handleSetAcceptTimeCommandResult: ${commandMessage.reason}")
        }
    }


    override fun release() {
        library = null
        service = null
        dispatcher = null
    }

    companion object {

        private const val TAG = "XiaomiService"
    }

}