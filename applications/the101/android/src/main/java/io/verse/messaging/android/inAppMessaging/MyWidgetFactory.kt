package io.verse.messaging.android.inAppMessaging

import io.tagd.arch.control.application
import io.verse.messaging.core.inapp.InAppMessage
import io.verse.messaging.core.inapp.InAppMessageWidget
import io.verse.messaging.core.inapp.InAppMessageWidgetBuilder
import io.verse.messaging.core.inapp.InAppMessageWidgetFactory
import kotlin.reflect.KClass

class MyWidgetFactory : InAppMessageWidgetFactory() {

    private var map: HashMap<KClass<out InAppMessage<*>>, InAppMessageWidgetBuilder<*, *>>? =
        hashMapOf()

    /**
     * The client application will register which [InAppMessageWidget] to be shown to the user for
     * a given [InAppMessage]
     */
    override fun <W : InAppMessageWidget, M : InAppMessage<W>> put(
        modelClass: KClass<M>,
        builder: InAppMessageWidgetBuilder<W, M>,
    ) {

        map?.put(modelClass, builder)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <W : InAppMessageWidget, M : InAppMessage<W>> newWidget(model: M): W? {
        val builder = map?.get(model::class) as? InAppMessageWidgetBuilder<W, M>
        return builder?.invoke(application()?.currentView(), model)
    }

}