package com.pamn.ggmatch.app.architecture.model.user

import com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject

@JvmInline
value class Email(val value: String) : ValueObject {
    init {
        val trimmed = value.trim()
        require(trimmed.isNotEmpty()) { "Email cannot be blank" }
        require(isValid(trimmed)) { "Invalid email format: $trimmed" }
    }

    override fun toString(): String = value

    companion object {
        private fun isValid(raw: String): Boolean {
            val atIndex = raw.indexOf('@')

            if (atIndex <= 0 || atIndex == raw.length - 1) return false

            val localPart = raw.substring(0, atIndex)
            val domainPart = raw.substring(atIndex + 1)

            if (localPart.isBlank() || domainPart.isBlank()) return false

            val dotIndex = domainPart.indexOf('.')
            if (dotIndex <= 0 || dotIndex == domainPart.length - 1) return false

            return true
        }
    }
}
