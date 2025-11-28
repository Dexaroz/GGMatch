package com.pamn.ggmatch.app.architecture.model.user

import com.pamn.ggmatch.app.architecture.sharedKernel.domain.AggregateRoot
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider
import kotlinx.datetime.Instant

class User private constructor(
    id: UserId,
    val email: Email,
    val username: Username,
    var status: UserStatus,
    val createdAt: Instant,
    var updatedAt: Instant,
) : AggregateRoot<UserId>(id) {
    companion object {
        fun register(
            id: UserId,
            email: Email,
            username: Username,
            timeProvider: TimeProvider,
        ): User {
            val now = timeProvider.now()

            val user =
                User(
                    id = id,
                    email = email,
                    username = username,
                    status = UserStatus.EMAIL_PENDING,
                    createdAt = now,
                    updatedAt = now,
                )

            user.registerUserRegisteredEvent()
            return user
        }

        fun fromPersistence(
            id: UserId,
            email: Email,
            username: Username,
            status: UserStatus,
            createdAt: Instant,
            updatedAt: Instant,
        ): User =
            User(
                id = id,
                email = email,
                username = username,
                status = status,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
    }

    fun confirmEmail(timeProvider: TimeProvider) {
        if (status == UserStatus.ACTIVE) return

        status = UserStatus.ACTIVE
        updatedAt = timeProvider.now()

        registerUserEmailConfirmedEvent()
    }

    private fun registerUserRegisteredEvent() {
        registerEvent(
            UserRegisteredEvent(
                userId = id,
                email = email,
                username = username,
            ),
        )
    }

    private fun registerUserEmailConfirmedEvent() {
        registerEvent(
            UserEmailConfirmedEvent(
                userId = id,
                email = email,
            ),
        )
    }
}
