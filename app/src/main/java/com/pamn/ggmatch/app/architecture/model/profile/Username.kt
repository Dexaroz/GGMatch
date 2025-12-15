package com.pamn.ggmatch.app.architecture.model.profile

import com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject

@JvmInline
value class Username(val value: String) :
    ValueObject {
    init {
        val trimmed = value.trim()
        require(trimmed.isNotEmpty()) { "Username cannot be blank" }
        require(trimmed.length in 3..20) { "Username must be between 3 and 20 characters" }
        require(trimmed.all { it.isLetterOrDigit() || it == '_' }) {
            "Username can only contain letters, digits and underscore"
        }
    }

    override fun toString(): String = value
}
