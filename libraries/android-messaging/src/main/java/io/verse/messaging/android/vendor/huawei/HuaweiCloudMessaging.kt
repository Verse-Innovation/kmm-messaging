package io.verse.messaging.android.vendor.huawei

import io.tagd.arch.access.bind
import io.tagd.arch.scopable.library.AbstractLibrary
import io.tagd.arch.scopable.library.Library
import io.tagd.di.Scope
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler

class HuaweiCloudMessaging private constructor(
    name: String,
    outerScope: Scope
) : AbstractLibrary(name, outerScope) {

    lateinit var serviceSpec: HuaweiCloudMessageServiceSpec
        private set

    fun register(
        handle: String,
        handler: ServiceDataObjectHandler<HuaweiCloudMessage, Unit>
    ): HuaweiCloudMessaging {

        serviceSpec.dispatcher?.factory?.put(handle, handler)
        return this
    }

    fun get(handle: String): ServiceDataObjectHandler<HuaweiCloudMessage, Unit>? {
        return serviceSpec.dispatcher?.factory?.get(handle)
    }

    fun unregister(handle: String): HuaweiCloudMessaging {
        serviceSpec.dispatcher?.factory?.remove(handle)
        return this
    }

    class Builder : Library.Builder<HuaweiCloudMessaging>() {

        private lateinit var service: HuaweiCloudMessageServiceSpec

        override fun name(name: String?): Builder {
            this.name = name
            return this
        }

        override fun scope(outer: Scope?): Builder {
            super.scope(outer)
            return this
        }

        fun service(service: HuaweiCloudMessageServiceSpec): Builder {
            this.service = service
            return this
        }

        override fun buildLibrary(): HuaweiCloudMessaging {
            return HuaweiCloudMessaging(
                name ?: "${outerScope.name}/$NAME",
                outerScope
            ).also { library ->
                library.serviceSpec = service
                bind<Library, HuaweiCloudMessaging>(instance = library)
            }
        }

        companion object {
            const val NAME = "huawei-push"
        }
    }

}