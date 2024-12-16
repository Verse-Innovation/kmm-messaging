package io.verse.messaging.core.inapp

import io.tagd.arch.access.library
import io.tagd.arch.scopable.library.Library
import io.tagd.di.Scope
import io.tagd.di.bind
import io.tagd.langx.assert
import io.verse.architectures.soa.Soa
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.messaging.core.MessagingLibrary
import kotlin.reflect.KClass

open class InAppMessaging private constructor(
    name: String,
    outerScope: Scope
) : MessagingLibrary<InAppMessageServiceProvider>(name = name, outerScope = outerScope) {

    var widgetFactory: InAppMessageWidgetFactory? = null
        private set

    override fun <S : InAppMessageServiceProvider> put(serviceProvider: S): InAppMessaging {
        super.put(serviceProvider)
        return this
    }

    override fun <S : InAppMessageServiceProvider> remove(identifier: KClass<S>): InAppMessaging {
        super.remove(identifier)
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun <S : InAppMessageServiceSpec> service(
        spClass: KClass<out InAppMessageServiceProvider>,
        serviceClass: KClass<out InAppMessageServiceSpec>,
    ): S? {

        return get(spClass)?.service(serviceClass) as? S
    }

    fun <G : InAppMessageServiceGateway> gateway(
        spClass: KClass<out InAppMessageServiceProvider>,
        serviceClass: KClass<out InAppMessageServiceSpec>,
    ): G? {

        return get(spClass)?.service(serviceClass)?.gateway()
    }

    override fun release() {
        widgetFactory?.release()
        widgetFactory = null
        super.release()
    }

    class Builder : MessagingLibrary.Builder<InAppMessageServiceProvider, InAppMessaging>() {

        private var widgetFactory: InAppMessageWidgetFactory? = null

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

        fun widgetFactory(inAppMessageWidgetFactory: InAppMessageWidgetFactory): Builder {
            this.widgetFactory = inAppMessageWidgetFactory
            return this
        }

        override fun inject(bindings: Scope.(InAppMessaging) -> Unit): Builder {
            super.inject(bindings)
            return this
        }

        override fun buildLibrary(): InAppMessaging {
            assert(library<Soa>() != null)

            return InAppMessaging(
                name = name ?: "${outerScope.name}/$NAME",
                outerScope = outerScope
            ).also { library ->

                library.factory = factory
                library.widgetFactory = widgetFactory ?: InAppMessageWidgetFactory()

                outerScope.bind<Library, InAppMessaging>(instance = library) // as 3rd party lib offering
                outerScope.library<Soa>()?.put(library) // as soa lib offering
            }
        }

        companion object {
            const val NAME = "in-app-messaging"
        }
    }
}

fun defaultInAppMessageService(): DefaultInAppMessageServiceSpec? {
    val library = library<InAppMessaging>()
    return library?.service(
        DefaultInAppMessageServiceProvider::class,
        DefaultInAppMessageServiceSpec::class
    )
}