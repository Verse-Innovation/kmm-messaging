package io.verse.messaging.core

import io.tagd.di.Scope
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.architectures.soa.provider.ServiceProviderLibrary
import kotlin.reflect.KClass

open class MessagingLibrary<T : MessageServiceProvider> protected constructor(
    name: String,
    outerScope: Scope
) : ServiceProviderLibrary<T>(name = name, outerScope = outerScope) {

    override fun <S : T> put(serviceProvider: S): MessagingLibrary<T> {
        super.put(serviceProvider)
        return this
    }

    override fun <S : T> remove(identifier: KClass<S>): MessagingLibrary<T> {
        super.remove(identifier)
        return this
    }

    abstract class Builder<GENRE_SUPER_TYPE : MessageServiceProvider,
            T : MessagingLibrary<GENRE_SUPER_TYPE>> :
        ServiceProviderLibrary.Builder<GENRE_SUPER_TYPE, T>() {

        override fun name(name: String?): Builder<GENRE_SUPER_TYPE, T> {
            super.name(name)
            return this
        }

        override fun scope(outer: Scope?): Builder<GENRE_SUPER_TYPE, T> {
            super.scope(outer)
            return this
        }

        override fun consumer(
            consumer: ApplicationServiceConsumer,
            genre: String
        ): Builder<GENRE_SUPER_TYPE, T> {

            super.consumer(consumer, genre)
            return this
        }

        abstract override fun buildLibrary(): T
    }
}