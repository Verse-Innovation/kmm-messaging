package io.verse.messaging.android.vendor.huawei

import io.tagd.arch.datatype.DataObject
import io.tagd.arch.scopable.module.Module
import io.tagd.langx.Context
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer

data class HuaweiCloudMessagingConfig(
    val context: Context,
    val consumer: ApplicationServiceConsumer,
    val defaultModule: Module,
    val cpId: String? = null,
    val appId: String? = null,
) : DataObject()