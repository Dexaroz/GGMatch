package com.pamn.ggmatch.app.architecture.io.chats

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.pamn.ggmatch.app.architecture.model.chats.ChatMessage
import com.pamn.ggmatch.app.architecture.model.chats.ConversationId
import com.pamn.ggmatch.app.architecture.model.chats.ConversationSummary
import com.pamn.ggmatch.app.architecture.model.chats.MessageId
import com.pamn.ggmatch.app.architecture.model.chats.MessageText
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.Instant

class FirebaseChatRepository(
    private val firestore: FirebaseFirestore,
) : ChatRepository {
    private fun conversationIdFor(
        a: UserId,
        b: UserId,
    ): ConversationId {
        val (x, y) = listOf(a.value, b.value).sorted()
        return ConversationId("${x}_$y")
    }

    private fun convDoc(id: ConversationId) = firestore.collection("conversations").document(id.value)

    private fun msgCol(id: ConversationId) = convDoc(id).collection("messages")

    private fun userInboxCol(userId: UserId) = firestore.collection("users").document(userId.value).collection("conversations")

    override suspend fun ensureConversationForMatch(
        me: UserId,
        other: UserId,
    ): Result<ConversationId, AppError> {
        return try {
            val id = conversationIdFor(me, other)
            val ref = convDoc(id)
            val snap = ref.get().await()
            if (snap.exists()) return Result.Ok(id)

            val now = System.currentTimeMillis()

            val convData =
                mapOf(
                    "participants" to listOf(me.value, other.value),
                    "createdAtEpochMs" to now,
                    "updatedAtEpochMs" to now,
                    "lastMessageText" to null,
                    "lastMessageSenderId" to null,
                    "lastMessageAtEpochMs" to null,
                )

            val inboxDataMe =
                mapOf(
                    "conversationId" to id.value,
                    "otherUserId" to other.value,
                    "updatedAtEpochMs" to now,
                    "lastMessageText" to null,
                    "lastMessageSenderId" to null,
                    "lastMessageAtEpochMs" to null,
                )

            val inboxDataOther =
                mapOf(
                    "conversationId" to id.value,
                    "otherUserId" to me.value,
                    "updatedAtEpochMs" to now,
                    "lastMessageText" to null,
                    "lastMessageSenderId" to null,
                    "lastMessageAtEpochMs" to null,
                )

            val batch = firestore.batch()
            batch.set(ref, convData)
            batch.set(userInboxCol(me).document(id.value), inboxDataMe)
            batch.set(userInboxCol(other).document(id.value), inboxDataOther)
            batch.commit().await()

            Result.Ok(id)
        } catch (e: Exception) {
            Result.Error(AppError.Unexpected("Failed to ensure conversation", e))
        }
    }

    override fun observeConversations(me: UserId): Flow<Result<List<ConversationSummary>, AppError>> =
        callbackFlow {
            val q =
                userInboxCol(me)
                    .orderBy("updatedAtEpochMs", Query.Direction.DESCENDING)
                    .limit(200)

            val reg =
                q.addSnapshotListener { snap, err ->
                    if (err != null) {
                        trySend(Result.Error(AppError.Unexpected("Failed to observe conversations", err)))
                        return@addSnapshotListener
                    }
                    if (snap == null) {
                        trySend(Result.Ok(emptyList()))
                        return@addSnapshotListener
                    }

                    val out =
                        snap.documents.mapNotNull { d ->
                            val convIdRaw = d.getString("conversationId") ?: d.id
                            val otherIdRaw = d.getString("otherUserId") ?: return@mapNotNull null
                            val updatedAtMs = d.getLong("updatedAtEpochMs") ?: return@mapNotNull null

                            val lastAtMs = d.getLong("lastMessageAtEpochMs")
                            val lastText = d.getString("lastMessageText")

                            val lastSenderRaw = d.getString("lastMessageSenderId")
                            val lastSenderId = lastSenderRaw?.let(::UserId)

                            val updatedAt = Instant.fromEpochMilliseconds(updatedAtMs)
                            val lastAt = lastAtMs?.let { Instant.fromEpochMilliseconds(it) }

                            ConversationSummary(
                                id = ConversationId(convIdRaw),
                                otherUserId = UserId(otherIdRaw),
                                lastMessageText = lastText,
                                lastMessageSenderId = lastSenderId,
                                lastMessageAt = lastAt,
                                updatedAt = updatedAt,
                            )
                        }

                    trySend(Result.Ok(out))
                }

            awaitClose { reg.remove() }
        }

    override fun observeMessages(conversationId: ConversationId): Flow<Result<List<ChatMessage>, AppError>> =
        callbackFlow {
            val q =
                msgCol(conversationId)
                    .orderBy("sentAtEpochMs", Query.Direction.ASCENDING)
                    .limit(300)

            val reg =
                q.addSnapshotListener { snap, err ->
                    if (err != null) {
                        trySend(Result.Error(AppError.Unexpected("Failed to observe messages", err)))
                        return@addSnapshotListener
                    }
                    if (snap == null) {
                        trySend(Result.Ok(emptyList()))
                        return@addSnapshotListener
                    }

                    val out =
                        snap.documents.mapNotNull { d ->
                            val senderIdRaw = d.getString("senderId") ?: return@mapNotNull null
                            val textRaw = d.getString("text") ?: return@mapNotNull null
                            val atMs = d.getLong("sentAtEpochMs") ?: return@mapNotNull null

                            // ✅ Errores: esperaba MessageId y MessageText; y faltaba sentAt
                            ChatMessage(
                                id = MessageId(d.id),
                                conversationId = conversationId,
                                senderId = UserId(senderIdRaw),
                                text = MessageText(textRaw),
                                sentAt = Instant.fromEpochMilliseconds(atMs),
                            )
                        }

                    trySend(Result.Ok(out))
                }

            awaitClose { reg.remove() }
        }

    override suspend fun sendMessage(
        conversationId: ConversationId,
        from: UserId,
        to: UserId,
        text: String,
        sentAtEpochMs: Long,
    ): Result<Unit, AppError> {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return Result.Ok(Unit)

        return try {
            val messageRef = msgCol(conversationId).document()

            val msgData =
                mapOf(
                    "senderId" to from.value,
                    "text" to trimmed,
                    "sentAtEpochMs" to sentAtEpochMs,
                )

            val lastData =
                mapOf(
                    "updatedAtEpochMs" to sentAtEpochMs,
                    "lastMessageText" to trimmed,
                    "lastMessageSenderId" to from.value,
                    "lastMessageAtEpochMs" to sentAtEpochMs,
                )

            val batch = firestore.batch()
            batch.set(messageRef, msgData)
            batch.set(convDoc(conversationId), lastData, SetOptions.merge())
            batch.set(userInboxCol(from).document(conversationId.value), lastData, SetOptions.merge())
            batch.set(userInboxCol(to).document(conversationId.value), lastData, SetOptions.merge())
            batch.commit().await()

            Result.Ok(Unit)
        } catch (e: Exception) {
            Result.Error(AppError.Unexpected("Failed to send message", e))
        }
    }
}
