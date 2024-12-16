package io.verse.messaging.core.inapp

import io.tagd.arch.control.application
import io.tagd.arch.present.mvp.PresentableView
import io.tagd.core.Releasable
import kotlin.reflect.KClass

fun interface InAppMessageWidgetBuilder<W : InAppMessageWidget, M : InAppMessage<W>> {

    fun invoke(currentView: PresentableView?, model: M): W?
}

open class InAppMessageWidgetFactory : Releasable {

    private var map: HashMap<KClass<out InAppMessage<*>>, InAppMessageWidgetBuilder<*, *>>? =
        hashMapOf()

    /**
     * The client application will register which [InAppMessageWidget] to be shown to the user for
     * a given [InAppMessage]
     */
    open fun <W : InAppMessageWidget, M : InAppMessage<W>> put(
        modelClass: KClass<M>,
        builder: InAppMessageWidgetBuilder<W, M>,
    ) {

        map?.put(modelClass, builder)
    }

    @Suppress("UNCHECKED_CAST")
    open fun <W : InAppMessageWidget, M : InAppMessage<W>> newWidget(model: M): W? {
        val builder = map?.get(model::class) as? InAppMessageWidgetBuilder<W, M>
        return builder?.invoke(application()?.currentView(), model)
    }

    override fun release() {
        map?.clear()
        map = null
    }
}