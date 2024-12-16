package io.verse.messaging.core.inapp

import io.tagd.core.Releasable
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler
import io.verse.messaging.core.Message

/**
 * 1. [InAppMessage]s are system wide, hence the module/handle must be default module
 */
open class InAppMessage<W : InAppMessageWidget> protected constructor(
    handle: String = ServiceDataObjectHandler.DEFAULT_HANDLE,
) : Message(handle = handle)

interface InAppMessageWidget : Releasable {

    fun show()

    fun dismiss()
}