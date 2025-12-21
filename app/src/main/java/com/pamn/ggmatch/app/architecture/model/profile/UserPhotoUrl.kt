package com.pamn.ggmatch.app.architecture.model.profile

import com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject

@JvmInline
value class UserPhotoUrl private constructor(val value: String) : ValueObject {

    companion object {

        fun from(raw: String?): UserPhotoUrl? {
            val v = raw?.trim().orEmpty()
            if (v.isBlank()) return null

            val lower = v.lowercase()
            val allowed =
                lower.startsWith("http://") ||
                        lower.startsWith("https://") ||
                        lower.startsWith("gs://") ||
                        lower.startsWith("content://")

            require(allowed) { "Invalid photo url scheme." }

            return UserPhotoUrl(v)
        }
    }
}
