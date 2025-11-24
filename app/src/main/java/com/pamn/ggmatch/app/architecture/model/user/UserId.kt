package com.pamn.ggmatch.app.architecture.model.user
import com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject

@JvmInline
value class UserId(val value: String) :
    ValueObject {
    init {
        require(value.isNotBlank()) { "UserId cannot be blank" }
    }

    override fun toString(): String = value
}
