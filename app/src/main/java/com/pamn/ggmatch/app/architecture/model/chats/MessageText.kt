package com.pamn.ggmatch.app.architecture.model.chats

import com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject

@JvmInline
value class MessageText(val value: String) : ValueObject {
    init {
        val t = value.trim()
        require(t.isNotEmpty()) { "Message text cannot be blank" }
        require(t.length <= 500) { "Message text too long" }
    }
}
