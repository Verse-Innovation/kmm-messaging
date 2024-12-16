package io.verse.messaging.core.push

import io.tagd.arch.access.library
import io.tagd.arch.scopable.library.Library
import io.tagd.di.Scope
import io.tagd.di.bind
import io.tagd.langx.assert
import io.verse.architectures.soa.Soa
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.messaging.core.MessagingLibrary
import kotlin.reflect.KClass

class PushMessaging private constructor(
    name: String,
    outerScope: Scope,
) : MessagingLibrary<PushMessageServiceProvider>(name = name, outerScope = outerScope) {

    lateinit var pushConfig: PushMessagingConfig
        private set

    override fun <S : PushMessageServiceProvider> put(serviceProvider: S): PushMessaging {
        super.put(serviceProvider)
        return this
    }

    override fun <S : PushMessageServiceProvider> remove(identifier: KClass<S>): PushMessaging {
        super.remove(identifier)
        return this
    }

    @Suppress("unused")
    fun update(pushConfig: PushMessagingConfig) {
        this.pushConfig = pushConfig
    }

    class Builder : MessagingLibrary.Builder<PushMessageServiceProvider, PushMessaging>() {

        private lateinit var pushConfig: PushMessagingConfig

        override fun name(name: String?): Builder {
            super.name(name)
            return this
        }

        override fun scope(outer: Scope?): Builder {
            super.scope(outer)
            return this
        }

        override fun consumer(
            consumer: ApplicationServiceConsumer,
            genre: String,
        ): Builder {

            super.consumer(consumer, genre)
            return this
        }

        fun pushConfig(pushConfig: PushMessagingConfig): Builder {
            this.pushConfig = pushConfig
            return this
        }

        override fun inject(bindings: Scope.(PushMessaging) -> Unit): Builder {
            super.inject(bindings)
            return this
        }

        override fun buildLibrary(): PushMessaging {
            assert(library<Soa>() != null)

            return PushMessaging(name ?: "${outerScope.name}/$NAME", outerScope).also { pm ->
                pm.factory = factory
                pm.pushConfig = pushConfig

                outerScope.bind<Library, PushMessaging>(instance = pm) // as 3rd party lib offering
                outerScope.library<Soa>()?.put(pm) // as soa lib offering
            }
        }

        companion object {
            const val NAME = "push_messaging"
        }
    }
}