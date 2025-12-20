package com.pamn.ggmatch.app.architecture.io.swipe

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.ggmatch.app.architecture.io.FirebaseRepository
import com.pamn.ggmatch.app.architecture.model.swipe.Swipe
import com.pamn.ggmatch.app.architecture.model.swipe.SwipeHistory
import com.pamn.ggmatch.app.architecture.model.swipe.SwipeType
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.datetime.Instant

class FirebaseSwipeHistoryRepository(
    firestore: FirebaseFirestore,
) : FirebaseRepository<UserId, SwipeHistory>(
        firestore = firestore,
        collectionName = "swipe-interactions",
    ),
    SwipeHistoryRepository {
    override fun getId(entity: SwipeHistory): UserId = entity.userId

    override fun idToString(id: UserId): String = id.value

    override fun stringToId(id: String): UserId = UserId(id)

    override fun toDocument(entity: SwipeHistory): Map<String, Any?> {
        return mapOf(
            "items" to
                entity.items.mapKeys { (targetId, _) ->
                    idToString(targetId)
                }.mapValues { (_, swipe) ->
                    mapOf(
                        "type" to swipe.type.name,
                        "createdAtEpochMs" to swipe.createdAt.toEpochMilliseconds(),
                        "updatedAtEpochMs" to swipe.updatedAt.toEpochMilliseconds(),
                    )
                },
            "createdAtEpochMs" to entity.createdAt.toEpochMilliseconds(),
            "updatedAtEpochMs" to entity.updatedAt.toEpochMilliseconds(),
        )
    }

    override fun fromDocument(
        id: UserId,
        doc: DocumentSnapshot,
    ): SwipeHistory {
        val rawItems =
            doc.get("items") as? Map<*, *> ?: emptyMap<String, Any?>()

        val items =
            rawItems.mapNotNull { (key, value) ->
                val targetId = (key as? String)?.let(::UserId) ?: return@mapNotNull null
                val data = value as? Map<*, *> ?: return@mapNotNull null

                val type =
                    (data["type"] as? String)
                        ?.let { runCatching { SwipeType.valueOf(it) }.getOrNull() }
                        ?: return@mapNotNull null

                val createdAt =
                    (data["createdAtEpochMs"] as? Long)?.let(Instant::fromEpochMilliseconds)
                        ?: return@mapNotNull null

                val updatedAt =
                    (data["updatedAtEpochMs"] as? Long)?.let(Instant::fromEpochMilliseconds)
                        ?: createdAt

                targetId to
                    Swipe(
                        fromUserId = id,
                        toUserId = targetId,
                        type = type,
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                    )
            }.toMap()

        val createdAt =
            doc.getLong("createdAtEpochMs")
                ?.let(Instant::fromEpochMilliseconds)
                ?: error("Missing createdAtEpochMs for user ${id.value}")

        val updatedAt =
            doc.getLong("updatedAtEpochMs")
                ?.let(Instant::fromEpochMilliseconds)
                ?: createdAt

        return SwipeHistory.fromPersistence(
            userId = id,
            items = items,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }

    override suspend fun addOrUpdate(history: SwipeHistory): Result<Unit, AppError> =
        when (val existing = get(history.userId)) {
            is Result.Error -> existing
            is Result.Ok ->
                if (existing.value == null) {
                    add(history)
                } else {
                    update(history)
                }
        }
}
