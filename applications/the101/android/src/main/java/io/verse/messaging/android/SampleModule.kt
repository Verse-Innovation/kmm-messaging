package io.verse.messaging.android

import io.tagd.arch.scopable.module.AbstractModule
import io.tagd.arch.scopable.module.Module
import io.tagd.di.Global
import io.tagd.di.Scope

class SampleModule(name: String, scope: Scope) : AbstractModule(name, scope) {

    override fun release() {
        // no-op
    }

}