package io.verse.messaging.core.inapp

import io.verse.architectures.soa.provider.DefaultServiceProvider
import io.verse.architectures.soa.provider.UserProfileEnricher

open class DefaultInAppMessageServiceProvider(
    userProfileEnricher: UserProfileEnricher? = null,
) : DefaultServiceProvider(
    genre = InAppMessageServiceProvider.GENRE,
    userProfileEnricher = userProfileEnricher
), InAppMessageServiceProvider