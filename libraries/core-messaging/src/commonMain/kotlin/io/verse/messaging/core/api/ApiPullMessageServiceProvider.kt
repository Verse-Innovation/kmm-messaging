package io.verse.messaging.core.api

import io.verse.messaging.core.crud.CrudServiceMessageRepository
import io.verse.messaging.core.pull.PullMessageServiceProvider

/**
 * If any business entity want to be informed as a [io.verse.messaging.core.Message], then
 * that particular Api end point behaves as a [ApiPullMessageServiceProvider]
 */
interface ApiPullMessageServiceProvider : PullMessageServiceProvider

interface ApiServiceMessageRepository : CrudServiceMessageRepository {

    val apis: List<ApiPullMessageServiceProvider>
}