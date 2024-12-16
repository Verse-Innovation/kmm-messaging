package io.verse.messaging.android.pull

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.tagd.arch.access.bind
import io.tagd.arch.access.library
import io.tagd.arch.scopable.library.Library
import io.tagd.core.Dependencies
import io.tagd.di.Global
import io.tagd.di.Scope
import io.tagd.langx.IllegalValueException
import io.verse.architectures.soa.Soa
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.messaging.android.vendor.huawei.HuaweiCloudMessage
import io.verse.messaging.android.vendor.huawei.HuaweiCloudMessageServiceSpec
import io.verse.messaging.android.vendor.huawei.HuaweiCloudMessaging
import io.verse.messaging.android.vendor.xiaomi.XiaomiCloudMessage
import io.verse.messaging.android.vendor.xiaomi.XiaomiCloudMessageServiceSpec
import io.verse.messaging.android.vendor.xiaomi.XiaomiCloudMessaging
import io.verse.messaging.cloud.fcm.FirebaseCloudMessage
import io.verse.messaging.cloud.fcm.FirebaseCloudMessaging
import io.verse.messaging.cloud.fcm.FirebaseCloudMessagingServiceSpec
import io.verse.messaging.core.pull.DefaultPullMessageServiceProvider
import org.junit.Test

class AbstractPullAbstractAndroidMessagingInitializerTest {

    private val dependencies = mock<Dependencies>()
    private val initializer = AndroidPullMessagingInitializer()

    @Test(expected = IllegalValueException::class)
    fun `if consumer is not present then new should throw exception`() {
        initializer.new(dependencies)
    }

    @Test
    fun `new should register PullMessagingServiceProvider`() {
        initDependencies()
        val data = initializer.new(dependencies)

        assert(data.get(DefaultPullMessageServiceProvider::class) != null)
    }

    @Test
    fun `PullMessagingServiceProvider should be registered with PullMessagingServiceSpec`() {
        initDependencies()
        val data = initializer.new(dependencies)
        val serviceProvider = data.get(DefaultPullMessageServiceProvider::class)!!

        assert(serviceProvider.service(DefaultPullMessageServiceSpec::class) != null)
    }

    @Test
    fun `PullMessagingServiceGateway should be registered with pushServiceSpecs`() {
        initDependencies()
        bindPushLibraries()
        val data = initializer.new(dependencies)
        val serviceProvider = data.get(DefaultPullMessageServiceProvider::class)!!
        val service = serviceProvider.service(DefaultPullMessageServiceSpec::class)!!
        val gateway = service.gateway

        assert(library<FirebaseCloudMessaging>() != null)
        assert(library<HuaweiCloudMessaging>() != null)
        assert(library<XiaomiCloudMessaging>() != null)
        assert(
            gateway?.get<FirebaseCloudMessage, Unit, FirebaseCloudMessagingServiceSpec>(
                FirebaseCloudMessagingServiceSpec.PULL_KEY
            ) != null
        )
        assert(
            gateway?.get<HuaweiCloudMessage, Unit, HuaweiCloudMessageServiceSpec>(
                HuaweiCloudMessageServiceSpec.PULL_KEY
            ) != null
        )
        assert(
            gateway?.get<XiaomiCloudMessage, Unit, XiaomiCloudMessageServiceSpec>(
                XiaomiCloudMessageServiceSpec.PULL_KEY
            ) != null
        )
    }

    private fun initDependencies() {
        bind<Library, Soa>(instance = mock())
        whenever(
            dependencies.get<ApplicationServiceConsumer>(AndroidPullMessagingInitializer.ARG_CONSUMER)
        ).then {
            mock<ApplicationServiceConsumer>()
        }
        whenever(
            dependencies.get<Scope>(AndroidPullMessagingInitializer.ARG_OUTER_SCOPE)
        ).then {
            Global
        }
    }

    private fun bindPushLibraries() {
        bind<Library, FirebaseCloudMessaging>(instance = firebaseLibrary())
        bind<Library, HuaweiCloudMessaging>(instance = huaweiLibrary())
        bind<Library, XiaomiCloudMessaging>(instance = xiaomiLibrary())
    }

    private fun firebaseLibrary(): FirebaseCloudMessaging {
        return mock<FirebaseCloudMessaging>().also {
            val serviceSpec = mock<FirebaseCloudMessagingServiceSpec>()
            whenever(serviceSpec.pullKey).thenReturn(FirebaseCloudMessagingServiceSpec.PULL_KEY)
            whenever(it.serviceSpec).thenReturn(serviceSpec)
        }
    }

    private fun huaweiLibrary(): HuaweiCloudMessaging {
        return mock<HuaweiCloudMessaging>().also {
            val serviceSpec = mock<HuaweiCloudMessageServiceSpec>()
            whenever(serviceSpec.pullKey).thenReturn(HuaweiCloudMessageServiceSpec.PULL_KEY)
            whenever(it.serviceSpec).thenReturn(serviceSpec)
        }
    }

    private fun xiaomiLibrary(): XiaomiCloudMessaging {
        return mock<XiaomiCloudMessaging>().also {
            val serviceSpec = mock<XiaomiCloudMessageServiceSpec>()
            whenever(serviceSpec.pullKey).thenReturn(XiaomiCloudMessageServiceSpec.PULL_KEY)
            whenever(it.serviceSpec).thenReturn(serviceSpec)
        }
    }

}