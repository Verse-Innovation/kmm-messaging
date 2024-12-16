package io.verse.messaging.android.vendor.xiaomi

import com.xiaomi.channel.commonutils.android.Region
import io.tagd.arch.datatype.DataObject
import io.tagd.arch.scopable.module.Module
import io.tagd.langx.Context
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer

data class XiaomiCloudMessagingConfig(
    val context: Context,
    val xiaomiPushAppId: String,
    val xiaomiPushAppKey: String,
    val xiaomiPushRegion: XiaomiPushRegion,
    val defaultModule: Module,
    val consumer: ApplicationServiceConsumer
) : DataObject()

enum class XiaomiPushRegion {
    India,
    Europe,
    Russia,
    Global,
}

fun XiaomiPushRegion.toMiRegion(): Region {
    return when (this) {
        XiaomiPushRegion.India -> Region.India
        XiaomiPushRegion.Europe -> Region.Europe
        XiaomiPushRegion.Russia -> Region.Russia
        XiaomiPushRegion.Global -> Region.Global
    }
}