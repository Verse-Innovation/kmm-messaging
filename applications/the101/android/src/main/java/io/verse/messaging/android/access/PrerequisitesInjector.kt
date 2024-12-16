package io.verse.messaging.android.access

import android.content.Context
import android.os.Build
import io.tagd.android.app.TagdApplication
import io.tagd.arch.access.Injector
import io.tagd.arch.access.library
import io.tagd.arch.scopable.Scopable
import io.tagd.core.dependencies
import io.tagd.langx.Callback
import io.tagd.langx.Locale
import io.tagd.langx.time.UnixEpochInMillis
import io.verse.app.AppBundle
import io.verse.app.AppBundleLibrary
import io.verse.app.AppBundleLibraryInitializer
import io.verse.app.InstallIdentifier
import io.verse.architectures.soa.Soa
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.architectures.soa.provider.ApplicationProfile
import io.verse.architectures.soa.provider.DeviceProfile
import io.verse.architectures.soa.provider.ServiceProviderFactoriesFactory
import io.verse.architectures.soa.provider.UserProfile
import io.verse.messaging.android.MyApplication
import io.verse.system.Device
import java.lang.ref.WeakReference

class PrerequisitesInjector(application: TagdApplication) : Injector {

    private var appReference: WeakReference<TagdApplication>? = WeakReference(application)

    private val app: TagdApplication?
        get() = appReference?.get()

    lateinit var device: Device
        private set

    lateinit var consumer: ApplicationServiceConsumer
        private set

    fun setup() {
        setupSystemFeatures()
        setupAppBundleFeatures()
        setupApplicationConsumer()
        setupSoa()
    }

    override fun inject(callback: Callback<Unit>) {
        callback.invoke(Unit)
    }

    private fun setupSystemFeatures() {
        device = Device(app!!)
    }

    private fun setupAppBundleFeatures() {
        app?.let { app ->
            val appBundleLibrary = AppBundleLibraryInitializer<MyApplication>(app, app as MyApplication).new(
                dependencies = dependencies(
                    AppBundleLibraryInitializer.ARG_CONTEXT to app,
                    AppBundleLibraryInitializer.ARG_OUTER_SCOPE to app.thisScope,
                )
            )
            if (appBundleLibrary.appBundle == null) {
                appBundleLibrary.update(newAppBundle(app))
            }
        }
    }

    private fun newAppBundle(context: Context): AppBundle {
        return AppBundle(
            namespace = context.packageName,
            versionName = Build.VERSION.RELEASE,
            currentVersionCode = 1,
            previousVersionCode = 1,
            flavour = null,
            flavorDimension = null,
            buildType = Build.TYPE,
            profilable = false,
            appLocale = null,
            localityLocale = null,
            systemLocale = Locale.default(),
            installTime = UnixEpochInMillis(),
            themeLabel = null,
            publishingIdentifier = context.packageName,
            installIdentifier = InstallIdentifier(),
        )
    }

    private fun setupSoa() {
        val scope = (app as? Scopable)!!.thisScope
        Soa.Builder()
            .scope(scope)
            .consumer(consumer)
            .build()
    }

    private fun setupApplicationConsumer() {
        consumer = ApplicationServiceConsumer(
            factories = ServiceProviderFactoriesFactory(),
            deviceProfile = deviceProfile(),
            applicationProfile = applicationProfile(),
            userProfile = userProfile(),
        )
    }

    private fun deviceProfile(): DeviceProfile {
        return DeviceProfile().also {
            it.set(name = DEVICE_INFO, value = device)
        }
    }

    private fun applicationProfile(): ApplicationProfile {
        return ApplicationProfile().also {
            library<AppBundleLibrary>()?.appBundle?.let { appBundle ->
                it.set(name = APP_BUNDLE, value = appBundle)
            }
        }
    }

    private fun userProfile(): UserProfile {
        return UserProfile()
    }

    override fun release() {
        appReference?.clear()
        appReference = null
    }

    companion object {
        //todo - these things must be moved to soa's profiles
        const val DEVICE_INFO = "device_info"
        const val APP_BUNDLE = "app_bundle"
    }
}