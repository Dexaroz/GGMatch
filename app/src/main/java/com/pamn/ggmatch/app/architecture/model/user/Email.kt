package com.pamn.ggmatch.app.architecture.model.user

import com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject

@JvmInline
value class Email(val value: String) :
    com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject {
    init {
        require(value.isNotBlank()) { "Email cannot be blank" }
        require(
            _root_ide_package_.com.pamn.ggmatch.app.architecture.model.user.Email.Companion.isValid(
                value
            )
        ) { "Invalid email format: $value" }
    }

    override fun toString(): String = value

    companion object {
        private fun isValid(raw: String): Boolean {
            val trimmed = raw.trim()
            return '@' in trimmed && '.' in trimmed.substringAfter('@')
        }
    }
}