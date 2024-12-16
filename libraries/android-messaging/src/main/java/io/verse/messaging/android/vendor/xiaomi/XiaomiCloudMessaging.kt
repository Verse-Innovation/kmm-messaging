package io.verse.messaging.android.vendor.xiaomi

import io.tagd.arch.access.bind
import io.tagd.arch.scopable.library.AbstractLibrary
import io.tagd.arch.scopable.library.Library
import io.tagd.di.Scope
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler

class XiaomiCloudMessaging private constructor(
    name: String,
    outerScope: Scope
) : AbstractLibrary(name, outerScope) {

    lateinit var serviceSpec: XiaomiCloudMessageServiceSpec
        private set

    fun register(
        handle: String,
        handler: ServiceDataObjectHandler<XiaomiCloudMessage, Unit>
    ): XiaomiCloudMessaging {

        serviceSpec.dispatcher?.factory?.put(handle, handler)
        return this
    }

    fun get(handle: String): ServiceDataObjectHandler<XiaomiCloudMessage, Unit>? {
        return serviceSpec.dispatcher?.factory?.get(handle)
    }

    fun unregister(handle: String): XiaomiCloudMessaging {
        serviceSpec.dispatcher?.factory?.remove(handle)
        return this
    }

    override fun release() {
        if (::serviceSpec.isInitialized) {
            serviceSpec.release()
        }
        super.release()
    }

    class Builder : Library.Builder<XiaomiCloudMessaging>() {

        private lateinit var serviceSpec: XiaomiCloudMessageServiceSpec

        override fun name(name: String?): Builder {
            this.name = name
            return this
        }

        override fun scope(outer: Scope?): Builder {
            super.scope(outer)
            return this
        }

        fun service(serviceSpec: XiaomiCloudMessageServiceSpec): Builder {
            this.serviceSpec = serviceSpec
            return this
        }

        override fun buildLibrary(): XiaomiCloudMessaging {
            return XiaomiCloudMessaging(
                name ?: "${outerScope.name}/$NAME",
                outerScope
            ).also { xiaomi ->

                xiaomi.serviceSpec = serviceSpec
                bind<Library, XiaomiCloudMessaging>(instance = xiaomi)
            }
        }

        companion object {
            const val NAME = "xiaomi-push"
        }
    }

}