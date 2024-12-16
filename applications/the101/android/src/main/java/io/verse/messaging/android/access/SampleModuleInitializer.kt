package io.verse.messaging.android.access

import io.tagd.android.app.TagdApplication
import io.tagd.arch.access.library
import io.tagd.core.Dependencies
import io.tagd.core.Initializer
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.messaging.android.SampleModule
import io.verse.messaging.android.vendor.huawei.HuaweiCloudMessageHandler
import io.verse.messaging.android.vendor.huawei.HuaweiCloudMessageHandler.Companion.HUAWEI_PUSH_HANDLER
import io.verse.messaging.android.vendor.huawei.HuaweiCloudMessaging
import io.verse.messaging.android.vendor.xiaomi.XiaomiCloudMessageHandler
import io.verse.messaging.android.vendor.xiaomi.XiaomiCloudMessageHandler.Companion.XIAOMI_HANDLER
import io.verse.messaging.android.vendor.xiaomi.XiaomiCloudMessaging
import io.verse.messaging.cloud.fcm.FirebaseCloudMessageHandler
import io.verse.messaging.cloud.fcm.FirebaseCloudMessageHandler.Companion.FCM_HANDLER
import io.verse.messaging.cloud.fcm.FirebaseCloudMessaging
import io.verse.messaging.core.notification.NotificationChannelManager
import io.verse.messaging.core.notification.NotificationIdGenerator
import io.verse.messaging.core.notification.NotificationPublisher

class SampleModuleInitializer: Initializer<SampleModule> {

    private var fcmLibrary = library<FirebaseCloudMessaging>()
    private var xiaomiLibrary = library<XiaomiCloudMessaging>()
    private var hmsLibrary = library<HuaweiCloudMessaging>()

    override fun new(dependencies: Dependencies): SampleModule {
        val app = dependencies.get<Context>(ARG_CONTEXT)!!

        return SampleModule(
            "sample-module",
            (app.applicationContext as TagdApplication).thisScope
        ).also { module ->

            registerFcmHandler(app, module)
            registerXiaomiHandler(app, module)
            registerHmsHandler(app, module)
        }
    }

    private fun registerFcmHandler(context: Context, module: SampleModule) {
        fcmLibrary?.let { fcmLibrary ->
            val handler = FirebaseCloudMessageHandler(
                handle = module.name,
                weakContext = WeakReference(context),
                notificationPublisher = notificationPublisher(context),
            )

            fcmLibrary.register(
                handle = FCM_HANDLER,
                handler = handler
            )
        }
    }

    private fun registerXiaomiHandler(context: Context, module: SampleModule) {
        xiaomiLibrary?.let { xiaomiLibrary ->
            val handler = XiaomiCloudMessageHandler(
                handle = module.name,
                weakContext = WeakReference(context),
                notificationPublisher = notificationPublisher(context),
            )

            xiaomiLibrary.register(
                handle = XIAOMI_HANDLER,
                handler = handler
            )
        }
    }

    private fun registerHmsHandler(context: Context, module: SampleModule) {
        hmsLibrary?.let { hmsLibrary ->
            val handler = HuaweiCloudMessageHandler(
                handle = module.name,
                weakContext = WeakReference(context),
                notificationPublisher = notificationPublisher(context),
            )

            hmsLibrary.register(
                handle = HUAWEI_PUSH_HANDLER,
                handler = handler
            )
        }
    }

    private fun notificationPublisher(context: Context): NotificationPublisher {
        return NotificationPublisher(
            weakContext = WeakReference(context),
            notificationChannelManager = NotificationChannelManager(),
            notificationIdGenerator = NotificationIdGenerator()
        )
    }

    override fun release() {
        fcmLibrary?.unregister(FCM_HANDLER)
        xiaomiLibrary?.unregister(XIAOMI_HANDLER)
        hmsLibrary?.unregister(HUAWEI_PUSH_HANDLER)
    }

    companion object {
        const val ARG_CONTEXT = "context"
    }

}