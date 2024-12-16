package io.verse.messaging.core.inapp

import io.tagd.arch.access.library

class DefaultInAppMessageServiceGateway : InAppMessageServiceGateway {

    private var library = library<InAppMessaging>()
    private var factory: InAppMessageWidgetFactory? = library?.widgetFactory
    override var service: InAppMessageServiceSpec? = defaultInAppMessageService()

    override fun <W : InAppMessageWidget> publish(message: InAppMessage<W>) {
        val widget = factory?.newWidget(message)
        widget?.show()
    }

    override fun release() {
        library = null
        factory = null
        service = null
    }
}