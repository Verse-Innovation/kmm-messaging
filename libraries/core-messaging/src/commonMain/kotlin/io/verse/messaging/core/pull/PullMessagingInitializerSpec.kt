package io.verse.messaging.core.pull

import io.tagd.core.Dependencies
import io.tagd.core.Initializer
import io.tagd.di.Global
import io.tagd.di.Scope
import io.tagd.langx.IllegalValueException
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer

abstract class PullMessagingInitializerSpec : Initializer<PullMessaging> {

    override fun new(dependencies: Dependencies): PullMessaging {
        val consumer = dependencies.get<ApplicationServiceConsumer>(ARG_CONSUMER)
            ?: kotlin.run {
                throw IllegalValueException("consumer must not be null")
            }
        val outerScope = dependencies.get<Scope>(ARG_OUTER_SCOPE)!!

        return PullMessaging.Builder()
            .scope(outerScope)
            .consumer(consumer, PullMessageServiceProvider.GENRE)
            .build().also {
                it.put(newPullMessagingServiceProvider())
            }
    }

    protected abstract fun newPullMessagingServiceProvider(): DefaultPullMessageServiceProvider

    override fun release() {
        //no-op
    }

    companion object {
        const val ARG_CONSUMER = "consumer"
        const val ARG_OUTER_SCOPE = "outer_scope"
    }
}