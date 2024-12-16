package io.verse.messaging.core.crud

import io.tagd.arch.data.dao.DataAccessObject
import io.tagd.arch.data.repo.Repository
import io.verse.messaging.core.Message
import io.verse.messaging.core.MessageServiceGateway
import io.verse.messaging.core.MessageServiceProvider

interface CrudMessageServiceProvider : MessageServiceProvider

interface CrudServiceMessageDao : DataAccessObject

interface TrayNotificationDao : DataAccessObject

interface CrudServiceMessageRepository : Repository, MessageServiceGateway {

    fun messageRead(message: Message) {
        //onReceive(message)
    }
}