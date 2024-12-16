package io.verse.messaging.cloud.fcm

import io.tagd.arch.scopable.library.AbstractLibrary
import io.tagd.arch.scopable.library.Library
import io.tagd.di.Scope
import io.tagd.di.bind
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler

class FirebaseCloudMessaging private constructor(
    name: String,
    outerScope: Scope
) : AbstractLibrary(name, outerScope) {

    lateinit var serviceSpec: FirebaseCloudMessagingServiceSpec
        private set

    fun register(
        handle: String,
        handler: ServiceDataObjectHandler<FirebaseCloudMessage, Unit>
    ): FirebaseCloudMessaging {

        serviceSpec.dispatcher?.factory?.put(handle, handler)
        return this
    }

    fun get(handle: String): ServiceDataObjectHandler<FirebaseCloudMessage, Unit>? {
        return serviceSpec.dispatcher?.factory?.get(handle)
    }

    fun unregister(handle: String): FirebaseCloudMessaging {
        serviceSpec.dispatcher?.factory?.remove(handle)
        return this
    }

    class Builder : Library.Builder<FirebaseCloudMessaging>() {

        private lateinit var serviceSpec: FirebaseCloudMessagingServiceSpec

        override fun name(name: String?): Builder {
            this.name = name
            return this
        }

        override fun scope(outer: Scope?): Builder {
            super.scope(outer)
            return this
        }

        fun service(serviceSpec: FirebaseCloudMessagingServiceSpec): Builder {
            this.serviceSpec = serviceSpec
            return this
        }

        override fun buildLibrary(): FirebaseCloudMessaging {
            return FirebaseCloudMessaging(name ?: "${outerScope.name}/$NAME", outerScope)
                .also { fcm ->
                    fcm.serviceSpec = serviceSpec
                    outerScope.bind<Library, FirebaseCloudMessaging>(instance = fcm)
                }
        }

        companion object {
            const val NAME = "fcm"
        }
    }
}