package io.verse.messaging.android

import io.tagd.android.app.TagdApplication
import io.tagd.android.app.loadingstate.AppLoadingStateHandler
import io.tagd.arch.control.ApplicationInjector
import io.verse.messaging.android.access.MyAppInjector

class MyApplication : TagdApplication() {

    override fun newInjector(): ApplicationInjector<out TagdApplication> {
        return MyAppInjector(this)
    }
}