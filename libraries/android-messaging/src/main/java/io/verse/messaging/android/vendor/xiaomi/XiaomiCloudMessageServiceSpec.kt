package io.verse.messaging.android.vendor.xiaomi

import com.xiaomi.mipush.sdk.MiPushClient
import io.tagd.langx.Context
import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher
import io.verse.architectures.soa.provider.SubscriptionProfile
import io.verse.messaging.core.push.PushMessageServiceSpec
import io.verse.messaging.core.push.TokenHandlerSpec

@Suppress("unused")
open class XiaomiCloudMessageServiceSpec(
    override val provider: XiaomiPushMessageServiceProvider,
    override val subscriptionProfile: SubscriptionProfile?,
    override val dispatcher: ServiceDataObjectDispatcher<XiaomiCloudMessage, Unit>?,
    override val gateway: XiaomiPushMessageServiceGateway? = null,
    override val tokenHandler: TokenHandlerSpec,
) : PushMessageServiceSpec<XiaomiCloudMessage> {

    override val name: String
        get() = "messaging/push/vendor/xiaomi/cloud-messaging"

    override val pullKey: String = PULL_KEY

    /**
     * Function: Register for Mi Push Service.
     * <br>
     * Scenario: When developers decide whether or not to register for push.
     */
    fun registerPush(context: Context, appId: String, appKey: String, region: XiaomiPushRegion) {
        MiPushClient.setRegion(region.toMiRegion())
        MiPushClient.registerPush(context, appId, appKey)
    }

    /**
     * Function: Unregister from Mi Push Service.
     * <br>
     * Scenario: When developers decide whether or not to turn off push.
     */
    fun unregisterPush(context: Context) {
        MiPushClient.unregisterPush(context)
    }

    /**
     * Function: Enable Mi Push service.
     * After calling the interfaces of disablePush and enablePush,
     * no new RegID will be generated, and the RegID will be the same as the original one.
     * <br>
     * Scenario: When developers decide whether or not to turn on push.
     */
    fun enablePush(context: Context) {
        MiPushClient.enablePush(context)
    }

    /**
     * Function: Disable Mi Push service.
     * After calling the interfaces of disablePush and enablePush,
     * no new RegID will be generated, and the RegID will be the same as the original one.
     * <br>
     * Scenario: When developers decide whether or not to disable push.
     */
    fun disablePush(context: Context) {
        MiPushClient.disablePush(context)
    }

    /**
     * Function: Pause receiving messages pushed by Mi Push service.
     * <br>
     * Scenario: Before an app resumes the Mi Push service, it won't receive any push messages.
     */
    fun pausePush(context: Context, category: String?) {
        MiPushClient.pausePush(context, category)
    }

    /**
     * Function: Resume receiving messages pushed by the Mi Push service.
     * At this point, the server will resend the push messages
     * that were suspended during the paused period.
     * <br>
     * Scenario: Resume receiving Mi Push service push messages.
     */
    fun resumePush(context: Context, category: String?) {

        MiPushClient.resumePush(context, category)
    }

    /**
     * Function: Set a subscription topic for a user.
     * <br>
     * Scenario: When developers send group messages to groups divided by subscriptions,
     * according to the user's subscriptions.
     */
    fun subscribe(context: Context, topic: String, category: String?) {
        MiPushClient.subscribe(context, topic, category)
    }

    /**
     * Function: Unsubscribe a user from a topic.
     * <br>
     * Scenario: When a user unsubscribes from a topic.
     */
    fun unsubscribe(context: Context, topic: String, category: String?) {
        MiPushClient.unsubscribe(context, topic, category)
    }

    /**
     * Function: Set an alias for a specified user.
     * <br>
     * Scenario: When developers decide to push through aliases.
     */
    fun setAlias(context: Context, alias: String, category: String?) {
        MiPushClient.setAlias(context, alias, category)
    }

    /**
     * Function: Cancel the alias of a specified user.
     * <br>
     * Scenario: When developers need to cancel a user alias.
     */
    fun unSetAlias(context: Context, alias: String, category: String?) {
        MiPushClient.unsetAlias(context, alias, category)
    }

    /**
     * Function: Set a userAccount for a specified user.
     * <br>
     * Scenario: When developers decide to push through userAccount.
     */
    fun setUserAccount(context: Context, userAccount: String, category: String?) {
        MiPushClient.setUserAccount(context, userAccount, category)
    }

    /**
     * Function: Cancel the userAccount of a specified user.
     * <br>
     * Scenario: When developers need to cancel a user's userAccount.
     */
    fun unSetUserAccount(context: Context, userAccount: String, category: String?) {
        MiPushClient.unsetUserAccount(context, userAccount, category)
    }

    /**
     * Function: Set a time period to accept Mi Push messages,
     * messages pushed beyond the period will be cached at server side,
     * when the appropriate time comes the cached messages will be pushed to the app.
     * <br>
     * Scenario: Users can control at what time they will receive push messages.
     */
    fun setAcceptTime(
        context: Context,
        startHour: Int,
        startMin: Int,
        endHour: Int,
        endMin: Int,
        category: String?,
    ) {

        MiPushClient.setAcceptTime(context, startHour, startMin, endHour, endMin, category)
    }


    /**
     * Function: By using the method, a list of aliases set by the client will be returned
     * (if the client hasn't set any aliases, a blank list will be returned).
     * <br>
     * Scenario: When developers need to check the aliases set.
     */
    fun getAllAlias(context: Context): List<String> {
        return MiPushClient.getAllAlias(context)
    }

    /**
     * Function: By using the method, a list of topics the client has subscribed to will be returned
     * (if the client hasn't subscribed to any topics, a blank list will be returned).
     * <br>
     * Scenario: When developers need to check all topics subscribed to.
     */
    fun getAllTopic(context: Context): List<String> {
        return MiPushClient.getAllTopic(context)
    }

    /**
     * Function: Get all accounts set by the client.
     * <br>
     * Scenario: When developers need to check all accounts subscribed to.
     */
    fun getAllUserAccount(context: Context): List<String> {
        return MiPushClient.getAllUserAccount(context)
    }

    /**
     * Function: Clear a pop-up notification with specified notifyId of Mi Push.
     * <br>
     * Scenario: When clearing a pop-up notification with specified notifyId of Mi Push.
     */
    fun clearNotification(context: Context, notificationId: Int) {
        MiPushClient.clearNotification(context, notificationId)
    }

    /**
     * Function: Clear all Mi Push pop-up notifications.
     * <br>
     * Scenario: When clearing all Mi Push pop-up notifications.
     */
    fun clearAllNotifications(context: Context) {
        MiPushClient.clearNotification(context)
    }

    /**
     * Function: Clear the notification message reminder type set by the client.
     * <br>
     * Scenario: Clear the notification message reminder type set by the client.
     */
    fun clearLocalNotificationType(context: Context) {
        MiPushClient.clearLocalNotificationType(context)
    }

    /**
     * Function: Get the client RegID.
     * <br>
     * Scenario: Get the client RegID.
     */
    fun getRegId(context: Context): String? {
        return MiPushClient.getRegId(context)
    }

    override fun release() {
        //no-op
    }

    companion object {
        const val PULL_KEY = "xiaomi"
    }

}