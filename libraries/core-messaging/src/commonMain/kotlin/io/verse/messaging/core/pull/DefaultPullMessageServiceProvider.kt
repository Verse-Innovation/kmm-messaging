package io.verse.messaging.core.pull

import io.verse.architectures.soa.provider.DefaultServiceProvider
import io.verse.architectures.soa.provider.UserProfileEnricher

open class DefaultPullMessageServiceProvider(
    userProfileEnricher: UserProfileEnricher? = null,
) : DefaultServiceProvider(
    genre = PullMessageServiceProvider.GENRE,
    userProfileEnricher = userProfileEnricher
), PullMessageServiceProvider