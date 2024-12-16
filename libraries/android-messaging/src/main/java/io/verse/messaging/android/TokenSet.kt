package io.verse.messaging.android

import io.verse.storage.core.FilableDataObject

data class TokenSet(
    val serviceProviderToTokenMap: Map<String, String>
): FilableDataObject()