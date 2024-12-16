package io.verse.messaging.android

import io.tagd.arch.scopable.Scopable
import io.tagd.core.Dependencies
import io.verse.messaging.android.pull.AndroidPullMessagingInitializer
import io.verse.messaging.android.push.AndroidPushMessagingInitializer
import io.verse.messaging.core.MessagingInitializerSpec
import io.verse.messaging.core.pull.PullMessaging
import io.verse.messaging.core.push.PushMessaging

open class AndroidMessagingInitializer<S : Scopable>(within: S) :
    MessagingInitializerSpec<S>(within) {

    override fun newPushMessaging(dependencies: Dependencies): PushMessaging {
        return AndroidPushMessagingInitializer().new(dependencies)
    }

    override fun newPullMessaging(dependencies: Dependencies): PullMessaging {
        return AndroidPullMessagingInitializer().new(dependencies)
    }
}