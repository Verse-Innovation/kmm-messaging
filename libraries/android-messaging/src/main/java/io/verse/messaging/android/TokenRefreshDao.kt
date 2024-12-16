package io.verse.messaging.android

import io.tagd.langx.Callback
import io.tagd.core.BidirectionalDependentOn
import io.verse.messaging.core.push.PushMessaging
import io.verse.storage.core.DataObjectFileAccessor
import io.verse.storage.core.FilableDao
import io.verse.storage.core.FilableDataAccessObject

interface TokenRefreshDaoSpec : FilableDataAccessObject<TokenSet>,
    BidirectionalDependentOn<PushMessaging> {

    fun readEntityAsync(
        success: Callback<TokenSet>? = null,
        failure: Callback<Throwable>? = null,
    )

    fun writeEntityAsync(
        data: TokenSet,
        success: Callback<Unit>? = null,
        failure: Callback<Throwable>? = null,
    )
}

class TokenRefreshDao(
    name: String,
    path: String,
    accessor: DataObjectFileAccessor,
) : FilableDao<TokenSet>(name = name, path = path, accessor = accessor),
    TokenRefreshDaoSpec {

    override fun injectBidirectionalDependent(other: PushMessaging) {
        // no-op
    }

    override fun readEntityAsync(
        success: Callback<TokenSet>?,
        failure: Callback<Throwable>?,
    ) {
        readAsync(success, failure)
    }

    override fun writeEntityAsync(
        data: TokenSet,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?,
    ) {
        writeAsync(data, success, failure)
    }
}