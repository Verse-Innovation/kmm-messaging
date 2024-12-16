package io.verse.messaging.core

import io.tagd.arch.scopable.library.AbstractLibrary
import io.tagd.arch.scopable.library.Library
import io.tagd.di.Scope
import io.tagd.di.bind
import io.verse.messaging.core.inapp.InAppMessaging
import io.verse.messaging.core.pull.PullMessaging
import io.verse.messaging.core.push.PushMessaging

open class Messaging protected constructor(
    name: String,
    outerScore: Scope,
) : AbstractLibrary(name, outerScore) {

    var pushMessaging: PushMessaging? = null
        private set

    var pullMessaging: PullMessaging? = null
        private set

    var inAppMessaging: InAppMessaging? = null
        private set

    override fun release() {
        pushMessaging = null
        pullMessaging = null
        inAppMessaging = null
        super.release()
    }

    class Builder : Library.Builder<Messaging>() {

        private var pushMessaging: PushMessaging? = null
        private var pullMessaging: PullMessaging? = null
        private var inAppMessaging: InAppMessaging? = null

        override fun name(name: String?): Builder {
            super.name(name)
            return this
        }

        fun pushMessaging(library: PushMessaging?): Builder {
            this.pushMessaging = library
            return this
        }

        fun pullMessaging(library: PullMessaging?): Builder {
            this.pullMessaging = library
            return this
        }

        fun inAppMessaging(library: InAppMessaging?): Builder {
            this.inAppMessaging = library
            return this
        }

        override fun buildLibrary(): Messaging {
            return Messaging(name ?: "${outerScope.name}/$NAME", outerScope)
                .also { messaging ->
                    messaging.pushMessaging = pushMessaging
                    messaging.pullMessaging = pullMessaging
                    messaging.inAppMessaging = inAppMessaging

                    outerScope.bind<Library, Messaging>(instance = messaging)
                }
        }

        companion object {
            const val NAME = "messaging"
        }
    }
}