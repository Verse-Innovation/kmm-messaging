package io.verse.messaging.core.pull

import io.tagd.arch.access.library
import io.tagd.arch.scopable.library.Library
import io.tagd.di.Scope
import io.tagd.di.bind
import io.tagd.langx.assert
import io.verse.architectures.soa.Soa
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.messaging.core.MessagingLibrary
import io.verse.messaging.core.push.PushMessaging
import kotlin.reflect.KClass

class PullMessaging private constructor(
    name: String,
    outerScope: Scope
) : MessagingLibrary<PullMessageServiceProvider>(name = name, outerScope = outerScope) {

    override fun <S : PullMessageServiceProvider> put(serviceProvider: S): PullMessaging {
        super.put(serviceProvider)
        return this
    }

    override fun <S : PullMessageServiceProvider> remove(identifier: KClass<S>): PullMessaging {
        super.remove(identifier)
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun <P, S : PullMessageServiceSpec<P>> service(
        spClass: KClass<out PullMessageServiceProvider>,
        serviceClass: KClass<out PullMessageServiceSpec<*>>
    ): S? {

        return get(spClass)?.service(serviceClass) as? S
    }

    fun <P, G : PullMessageServiceGateway<P>> gateway(
        spClass: KClass<out PullMessageServiceProvider>,
        serviceClass: KClass<out PullMessageServiceSpec<*>>
    ): G? {

        return get(spClass)?.service(serviceClass)?.gateway()
    }

    class Builder : MessagingLibrary.Builder<PullMessageServiceProvider, PullMessaging>() {

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

        override fun inject(bindings: Scope.(PullMessaging) -> Unit): Builder {
            super.inject(bindings)
            return this
        }

        override fun buildLibrary(): PullMessaging {
            assert(library<Soa>() != null)

            return PullMessaging(name ?: "${outerScope.name}/$NAME", outerScope).also { pm ->
                pm.factory = factory

                outerScope.bind<Library, PullMessaging>(instance = pm) // as 3rd party lib offering
                outerScope.library<Soa>()?.put(pm) // as soa lib offering
            }
        }

        companion object {
            const val NAME = "pull-messaging"
        }
    }
}