package io.verse.messaging.android.access

import android.app.Activity
import io.tagd.android.app.TagdApplication
import io.tagd.arch.control.application
import io.tagd.arch.present.mvp.PresentableView
import io.verse.messaging.android.HelloWorldMessage
import io.verse.messaging.android.HelloWorldMessageWidget
import io.verse.messaging.core.inapp.InAppMessageWidgetFactory
import io.verse.messaging.core.inapp.InAppMessagingInitializer

class MyInAppMessagingInitializer : InAppMessagingInitializer() {

    override fun registerWidgets(widgetFactory: InAppMessageWidgetFactory) {
        widgetFactory.put(
            modelClass = HelloWorldMessage::class,
            builder = { currentView, model ->
                getCurrentActivity(currentView)?.let {
                    HelloWorldMessageWidget(it, model)
                }
            }
        )
    }

    private fun getCurrentActivity(currentView: PresentableView?): Activity? {
        return currentView as? Activity ?: kotlin.run {
            (application() as? TagdApplication)?.currentActivity
        }
    }
}