package com.pamn.ggmatch.app.architecture.model.chats

import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject

@JvmInline
value class ConversationId(val value: String) : ValueObject {
    init {
        require(value.isNotBlank())
    }

    companion object {
        fun fromUsers(
            a: UserId,
            b: UserId,
        ): ConversationId {
            val (x, y) = listOf(a.value, b.value).sorted()
            return ConversationId("${x}_$y")
        }
    }
}
