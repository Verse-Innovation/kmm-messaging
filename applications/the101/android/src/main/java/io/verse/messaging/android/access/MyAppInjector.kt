package io.verse.messaging.android.access

import io.tagd.android.app.TagdApplicationInjector
import io.tagd.arch.scopable.WithinScopableInitializer
import io.tagd.core.dependencies
import io.tagd.langx.Callback
import io.verse.messaging.android.MyApplication

class MyAppInjector(application: MyApplication) :
    TagdApplicationInjector<MyApplication>(application) {

    private var prerequisitesInjector: PrerequisitesInjector? = null

    init {
        prerequisitesInjector = PrerequisitesInjector(application)
    }

    override fun setup() {
        super.setup()
        prerequisitesInjector?.setup()
    }

    override fun load(initializers: ArrayList<WithinScopableInitializer<MyApplication, *>>) {
        super.load(initializers)
        initializers.add(MyLatchInitializer(within))
        initializers.add(MyMessagingInitializer(within))
    }

    override fun inject(callback: Callback<Unit>) {
        super.inject {
            setupSampleModule()
            callback.invoke(Unit)
        }
    }

    private fun setupSampleModule() {
        within.let { app ->
            SampleModuleInitializer().new(
                dependencies = dependencies(
                    SampleModuleInitializer.ARG_CONTEXT to app
                )
            )
        }
    }
}