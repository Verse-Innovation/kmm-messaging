package io.verse.messaging.cloud.fcm

import io.tagd.arch.datatype.DataObject
import io.tagd.arch.scopable.module.Module
import io.tagd.langx.Context
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer

data class FirebaseCloudMessagingConfig(
    val context: Context,
    val consumer: ApplicationServiceConsumer,
    val defaultModule: Module,
): DataObject()