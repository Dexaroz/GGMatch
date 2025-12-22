package com.pamn.ggmatch.app.architecture.model.chats

import com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject

@JvmInline
value class MessageId(val value: String) : ValueObject {
    init {
        require(value.isNotBlank())
    }
}
