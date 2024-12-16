package io.verse.messaging.android.push

import io.tagd.android.app.TagdApplication
import io.tagd.arch.access.library
import io.tagd.arch.control.IApplication
import io.tagd.arch.data.dao.DataAccessObject
import io.tagd.arch.data.gateway.Gateway
import io.tagd.arch.data.repo.Repository
import io.tagd.arch.domain.usecase.Command
import io.tagd.arch.scopable.library.usecase
import io.tagd.core.dependencies
import io.tagd.di.Scope
import io.tagd.di.key
import io.tagd.di.layer
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.messaging.android.R
import io.verse.messaging.android.TokenRefreshDao
import io.verse.messaging.android.TokenRefreshDaoSpec
import io.verse.messaging.android.TokenRefreshGateway
import io.verse.messaging.android.TokenRefreshGatewaySpec
import io.verse.messaging.android.TokenRefreshRepository
import io.verse.messaging.android.TokenRefreshRepositorySpec
import io.verse.messaging.android.TokenRefreshUsecase
import io.verse.messaging.android.vendor.huawei.HuaweiCloudMessagingConfig
import io.verse.messaging.android.vendor.huawei.HuaweiCloudMessagingInitializer
import io.verse.messaging.android.vendor.huawei.HuaweiServiceChecker
import io.verse.messaging.android.vendor.xiaomi.XiaomiCloudMessagingConfig
import io.verse.messaging.android.vendor.xiaomi.XiaomiCloudMessagingInitializer
import io.verse.messaging.android.vendor.xiaomi.XiaomiPushRegion
import io.verse.messaging.android.vendor.xiaomi.XiaomiServiceChecker
import io.verse.messaging.cloud.fcm.FirebaseCloudMessagingConfig
import io.verse.messaging.cloud.fcm.FirebaseCloudMessagingInitializer
import io.verse.messaging.core.push.PushMessaging
import io.verse.messaging.core.push.PushMessagingInitializerSpec
import io.verse.storage.core.Storage
import java.lang.ref.WeakReference

open class AndroidPushMessagingInitializer : PushMessagingInitializerSpec() {

    override fun setupVendorPushMessagingServices(
        application: IApplication,
        outerScope: Scope,
        consumer: ApplicationServiceConsumer
    ) {

        application as TagdApplication
        setupFirebaseCloudMessaging(application, outerScope, consumer)
        setupXiaomiCloudMessagingIfApplicable(application, outerScope, consumer)
        setupHmsIfApplicable(application, outerScope, consumer)
    }

    override fun Scope.injectPlatformDependencies(
        application: IApplication,
        library: PushMessaging
    ) {

        application as TagdApplication

        layer<DataAccessObject> {
            bind(key(), newTokenRefreshDao(application).also {
                it.injectBidirectionalDependent(library)
            })
        }
        layer<Gateway> {
            bind(key<TokenRefreshGatewaySpec>(), TokenRefreshGateway().also {
                it.injectBidirectionalDependent(library)
            })
        }
        layer<Repository> {
            bind(key<TokenRefreshRepositorySpec>(), TokenRefreshRepository().also {
                it.injectBidirectionalDependent(library)
            })
        }
        layer<Command<*, *>> {
            bind(key(), TokenRefreshUsecase().also {
                it.injectBidirectionalDependent(library)
            })
        }
    }

    private fun newTokenRefreshDao(app: TagdApplication): TokenRefreshDaoSpec {
        return TokenRefreshDao(
            name = TOKEN_REGISTRATION_DTO_FILE_NAME,
            path = app.filesDir.path,
            accessor = library<Storage>()!!.dataObjectFileAccessor
        )
    }

    protected open fun setupFirebaseCloudMessaging(
        application: TagdApplication,
        outerScope: Scope,
        consumer: ApplicationServiceConsumer,
    ) {

        val config = FirebaseCloudMessagingConfig(
            context = application,
            consumer = consumer,
            defaultModule = application,
        )

        initializeFirebaseCloudMessaging(config, outerScope)
    }

    protected open fun initializeFirebaseCloudMessaging(
        config: FirebaseCloudMessagingConfig,
        outerScope: Scope,
    ) {

        FirebaseCloudMessagingInitializer().new(
            dependencies = dependencies(
                FirebaseCloudMessagingInitializer.ARG_CONFIG to config,
                ARG_OUTER_SCOPE to outerScope,
            )
        )
    }

    protected open fun setupHmsIfApplicable(
        application: TagdApplication,
        outerScope: Scope,
        consumer: ApplicationServiceConsumer,
    ) {

        val serviceChecker = HuaweiServiceChecker(
            weakContext = WeakReference(application)
        )
        if (serviceChecker.canInitializeVendor()) {
            val config = HuaweiCloudMessagingConfig(
                context = application,
                consumer = consumer,
                defaultModule = application,
                cpId = "2640091000024604118", // todo - pull it later
                appId = "108250785", // todo - pull it later
            )

            initializeHuaweiCloudMessaging(config, outerScope)
        }
    }

    protected open fun initializeHuaweiCloudMessaging(
        config: HuaweiCloudMessagingConfig,
        outerScope: Scope,
    ) {
        HuaweiCloudMessagingInitializer().new(
            dependencies = dependencies(
                HuaweiCloudMessagingInitializer.ARG_CONFIG to config,
                ARG_OUTER_SCOPE to outerScope,
            )
        )
    }

    protected open fun setupXiaomiCloudMessagingIfApplicable(
        application: TagdApplication,
        outerScope: Scope,
        consumer: ApplicationServiceConsumer,
    ) {

        if (XiaomiServiceChecker().canInitializeVendor()) {
            val config = XiaomiCloudMessagingConfig(
                context = application,
                defaultModule = application,
                xiaomiPushAppId = application.getString(R.string.mi_push_app_id),
                xiaomiPushAppKey = application.getString(R.string.mi_push_app_key),
                xiaomiPushRegion = XiaomiPushRegion.India,
                consumer = consumer
            )
            initializeXiaomiCloudMessaging(config, outerScope)
        }
    }

    protected open fun initializeXiaomiCloudMessaging(
        config: XiaomiCloudMessagingConfig,
        outerScope: Scope,
    ) {

        XiaomiCloudMessagingInitializer().new(
            dependencies = dependencies(
                XiaomiCloudMessagingInitializer.ARG_CONFIG to config,
                ARG_OUTER_SCOPE to outerScope,
            )
        )
    }

    companion object {
        //todo - break application into context and module then we can move this class to library
        //however we've to make it abstract to leave clients to inject service specific config
        private const val TOKEN_REGISTRATION_DTO_FILE_NAME = "TokenRefreshDto.json"
    }
}

@Suppress("unused")
fun tokenRefreshUseCase(): TokenRefreshUsecase? {
    return library<PushMessaging>()?.usecase()
}