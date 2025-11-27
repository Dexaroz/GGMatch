package com.pamn.ggmatch.app.architecture.io.user

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.ggmatch.app.architecture.io.FirebaseRepository
import com.pamn.ggmatch.app.architecture.model.user.Email
import com.pamn.ggmatch.app.architecture.model.user.User
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.model.user.UserStatus
import com.pamn.ggmatch.app.architecture.model.user.Username
import kotlinx.datetime.Instant

class FirebaseUserRepository(
    firestore: FirebaseFirestore,
) : FirebaseRepository<UserId, User>(
    firestore = firestore,
    collectionName = "users",
), UserRepository {

    override fun getId(entity: User): UserId =
        entity.id

    override fun idToString(id: UserId): String =
        id.value

    override fun stringToId(id: String): UserId =
        UserId(id)

    override fun toDocument(entity: User): Map<String, Any?> =
        mapOf(
            "email" to entity.email.value,
            "username" to entity.username.value,
            "status" to entity.status.name,
            "createdAtEpochMs" to entity.createdAt.toEpochMilliseconds(),
            "updatedAtEpochMs" to entity.updatedAt.toEpochMilliseconds(),
        )

    override fun fromDocument(id: UserId, doc: DocumentSnapshot): User {
        val email =
            Email(
                doc.getString("email")
                    ?: error("Missing 'email' field for user ${id.value}"),
            )

        val username =
            Username(
                doc.getString("username")
                    ?: error("Missing 'username' field for user ${id.value}"),
            )

        val statusName = doc.getString("status") ?: UserStatus.EMAIL_PENDING.name
        val status =
            runCatching { UserStatus.valueOf(statusName) }
                .getOrElse { UserStatus.EMAIL_PENDING }

        val createdAtMs =
            doc.getLong("createdAtEpochMs")
                ?: error("Missing 'createdAtEpochMs' for user ${id.value}")

        val updatedAtMs =
            doc.getLong("updatedAtEpochMs")
                ?: createdAtMs

        return User.fromPersistence(
            id = id,
            email = email,
            username = username,
            status = status,
            createdAt = Instant.fromEpochMilliseconds(createdAtMs),
            updatedAt = Instant.fromEpochMilliseconds(updatedAtMs),
        )
    }

    override suspend fun findByEmail(email: Email) =
        findFirstBy(field = "email", value = email.value)

    override suspend fun findByUsername(username: Username) =
        findFirstBy(field = "username", value = username.value)
}