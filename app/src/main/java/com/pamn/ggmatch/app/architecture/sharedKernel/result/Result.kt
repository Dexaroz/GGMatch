package com.pamn.ggmatch.app.architecture.sharedKernel.result

sealed class Result<out T, out E> {
    data class Ok<out T>(val value: T) : Result<T, Nothing>()

    data class Error<out E>(val error: E) : Result<Nothing, E>()

    val isOk: Boolean get() = this is Ok<T>
    val isError: Boolean get() = this is Error<E>

    inline fun getOrNull(): T? =
        when (this) {
            is Ok -> value
            is Error -> null
        }

    inline fun errorOrNull(): E? =
        when (this) {
            is Ok -> null
            is Error -> error
        }

    inline fun <R> map(transform: (T) -> R): Result<R, E> =
        when (this) {
            is Ok -> Ok(transform(value))
            is Error -> this
        }

    inline fun <F> mapError(transform: (E) -> F): Result<T, F> =
        when (this) {
            is Ok -> this
            is Error -> Error(transform(error))
        }

    inline fun onSuccess(block: (T) -> Unit): Result<T, E> {
        if (this is Ok) block(value)
        return this
    }

    inline fun onError(block: (E) -> Unit): Result<T, E> {
        if (this is Error) block(error)
        return this
    }
}
