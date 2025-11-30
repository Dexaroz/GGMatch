package com.pamn.ggmatch.app.architecture.io.user

import com.pamn.ggmatch.app.architecture.model.user.Email
import com.pamn.ggmatch.app.architecture.model.user.User
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

interface UserRepository {
    suspend fun add(user: User): Result<Unit, AppError>

    suspend fun update(user: User): Result<Unit, AppError>

    suspend fun remove(id: UserId): Result<Unit, AppError>

    suspend fun get(id: UserId): Result<User?, AppError>

    suspend fun getAll(): Result<List<User>, AppError>

    suspend fun findByEmail(email: Email): Result<User?, AppError>
}
