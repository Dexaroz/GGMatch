package com.pamn.ggmatch.app.architecture.io.user

import com.pamn.ggmatch.app.architecture.model.user.Email
import com.pamn.ggmatch.app.architecture.model.user.User
import com.pamn.ggmatch.app.architecture.model.user.Username
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

interface AuthRepository {
    suspend fun register(
        email: Email,
        password: String,
        username: Username,
    ): Result<User, AppError>

    suspend fun login(
        email: Email,
        password: String,
    ): Result<User, AppError>

    suspend fun logout(): Result<Unit, AppError>

    suspend fun getCurrentUser(): Result<User?, AppError>
}
