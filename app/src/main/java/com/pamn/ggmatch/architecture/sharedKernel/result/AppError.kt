package com.pamn.ggmatch.architecture.sharedKernel.result

sealed class AppError(open val message: String) {

    data class Validation(override val message: String) : AppError(message)

    data class NotFound(override val message: String) : AppError(message)

    data class Conflict(override val message: String) : AppError(message)

    data class Unauthorized(override val message: String) : AppError(message)

    data class Unexpected(
        override val message: String,
        val cause: Throwable? = null
    ) : AppError(message)
}