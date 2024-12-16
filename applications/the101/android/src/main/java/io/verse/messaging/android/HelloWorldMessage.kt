package io.verse.messaging.android

import android.app.AlertDialog
import io.tagd.langx.Context
import io.verse.messaging.core.inapp.InAppMessage
import io.verse.messaging.core.inapp.InAppMessageWidget

data class HelloWorldMessage(
    val title: String = "Hello World",
    val message: String = "Welcome to InApp Messaging",
) : InAppMessage<HelloWorldMessageWidget>()

class HelloWorldMessageWidget(
    context: Context,
    helloWorldMessage: HelloWorldMessage,
) : InAppMessageWidget {

    private var alertDialog: AlertDialog? = AlertDialog.Builder(context)
        .setTitle(helloWorldMessage.title)
        .setMessage(helloWorldMessage.message)
        .create()

    override fun show() {
        alertDialog?.show()
    }

    override fun dismiss() {
        alertDialog?.dismiss()
    }

    override fun release() {
        dismiss()
        alertDialog = null
    }
}
