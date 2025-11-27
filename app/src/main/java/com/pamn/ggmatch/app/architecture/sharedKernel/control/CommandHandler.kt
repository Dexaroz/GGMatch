package com.pamn.ggmatch.app.architecture.sharedKernel.control

import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

interface CommandHandler<C : Command, R> {
    suspend operator fun invoke(command: C): Result<R, AppError>
}
