package com.pamn.ggmatch.app.architecture.model

import com.pamn.ggmatch.app.architecture.model.user.Email
import com.pamn.ggmatch.app.architecture.model.user.User
import com.pamn.ggmatch.app.architecture.model.user.UserEmailConfirmedEvent
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.model.user.UserRegisteredEvent
import com.pamn.ggmatch.app.architecture.model.user.UserStatus
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.datetime.Instant
import org.junit.Test

private class FixedTimeProvider(
    private val fixedInstant: Instant,
) : TimeProvider {
    override fun now(): Instant = fixedInstant
}

class UserTest {
    @Test
    fun `register should create user with expected initial state`() {
        val id = UserId("user-123")
        val email = Email("test@example.com")
        val now = Instant.parse("2025-01-01T10:00:00Z")
        val timeProvider = FixedTimeProvider(now)

        val user =
            User.register(
                id = id,
                email = email,
                timeProvider = timeProvider,
            )

        assertEquals(id, user.id)
        assertEquals(email, user.email)
        assertEquals(UserStatus.EMAIL_PENDING, user.status)
        assertEquals(now, user.createdAt)
        assertEquals(now, user.updatedAt)
    }

    @Test
    fun `register should raise exactly one UserRegisteredEvent`() {
        val id = UserId("user-123")
        val email = Email("test@example.com")
        val now = Instant.parse("2025-01-01T10:00:00Z")
        val timeProvider = FixedTimeProvider(now)

        val user =
            User.register(
                id = id,
                email = email,
                timeProvider = timeProvider,
            )

        val events = user.domainEvents.filterIsInstance<UserRegisteredEvent>()

        assertEquals(1, events.size)
        val event = events.first()
        assertEquals(id, event.userId)
        assertEquals(email, event.email)
    }

    @Test
    fun `confirmEmail should activate user update timestamp and raise event when pending`() {
        val id = UserId("user-123")
        val email = Email("test@example.com")
        val createdAt = Instant.parse("2025-01-01T10:00:00Z")
        val registerProvider = FixedTimeProvider(createdAt)

        val user =
            User.register(
                id = id,
                email = email,
                timeProvider = registerProvider,
            )

        val confirmTime = Instant.parse("2025-01-02T12:00:00Z")
        val confirmProvider = FixedTimeProvider(confirmTime)

        user.confirmEmail(confirmProvider)

        assertEquals(UserStatus.ACTIVE, user.status)
        assertEquals(createdAt, user.createdAt)
        assertEquals(confirmTime, user.updatedAt)

        val events = user.domainEvents.filterIsInstance<UserEmailConfirmedEvent>()
        assertEquals(1, events.size)
        val event = events.first()
        assertEquals(id, event.userId)
        assertEquals(email, event.email)
    }

    @Test
    fun `confirmEmail should be idempotent when already active`() {
        val id = UserId("user-123")
        val email = Email("test@example.com")
        val createdAt = Instant.parse("2025-01-01T10:00:00Z")
        val firstUpdateTime = Instant.parse("2025-01-02T12:00:00Z")
        val secondUpdateTime = Instant.parse("2025-01-03T15:00:00Z")

        val registerProvider = FixedTimeProvider(createdAt)
        val user =
            User.register(
                id = id,
                email = email,
                timeProvider = registerProvider,
            )

        val firstConfirmProvider = FixedTimeProvider(firstUpdateTime)
        user.confirmEmail(firstConfirmProvider)

        assertEquals(UserStatus.ACTIVE, user.status)
        assertEquals(firstUpdateTime, user.updatedAt)

        val eventsAfterFirst = user.domainEvents.filterIsInstance<UserEmailConfirmedEvent>()
        assertEquals(1, eventsAfterFirst.size)

        val secondConfirmProvider = FixedTimeProvider(secondUpdateTime)
        user.confirmEmail(secondConfirmProvider)

        assertEquals(firstUpdateTime, user.updatedAt)

        val eventsAfterSecond = user.domainEvents.filterIsInstance<UserEmailConfirmedEvent>()
        assertEquals(1, eventsAfterSecond.size)
    }

    @Test
    fun `fromPersistence should restore user without raising domain events`() {
        val id = UserId("user-123")
        val email = Email("test@example.com")
        val createdAt = Instant.parse("2025-01-01T10:00:00Z")
        val updatedAt = Instant.parse("2025-01-05T18:30:00Z")
        val status = UserStatus.ACTIVE

        val user =
            User.fromPersistence(
                id = id,
                email = email,
                status = status,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )

        assertEquals(id, user.id)
        assertEquals(email, user.email)
        assertEquals(status, user.status)
        assertEquals(createdAt, user.createdAt)
        assertEquals(updatedAt, user.updatedAt)
        assertTrue(user.domainEvents.isEmpty())
    }

    @Test
    fun `register should keep email value`() {
        val id = UserId("user-123")
        val email = Email("test@example.com")
        val now = Instant.parse("2025-01-01T10:00:00Z")
        val timeProvider = FixedTimeProvider(now)

        val user =
            User.register(
                id = id,
                email = email,
                timeProvider = timeProvider,
            )

        assertEquals(email, user.email)
    }
}

class EmailTest {
    @Test
    fun `valid email should be accepted`() {
        val value = "john.doe@example.com"
        val email = Email(value)
        assertEquals(value, email.value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `blank email should be rejected`() {
        Email("   ")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `email without at sign should be rejected`() {
        Email("johndoe.example.com")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `email without local part should be rejected`() {
        Email("@example.com")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `email without domain should be rejected`() {
        Email("john.doe@")
    }

    @Test
    fun `email with uppercase should be handled consistently`() {
        val value = "John.Doe@Example.COM"
        val email = Email(value)
        assertEquals(value, email.value)
    }
}

class UserIdTest {
    @Test
    fun `non blank id should be accepted`() {
        val value = "user-123"
        val userId = UserId(value)
        assertEquals(value, userId.value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `blank id should be rejected`() {
        UserId("   ")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `empty id should be rejected`() {
        UserId("")
    }
}
