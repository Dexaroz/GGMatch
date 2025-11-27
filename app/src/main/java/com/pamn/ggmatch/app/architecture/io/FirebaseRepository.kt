package com.pamn.ggmatch.app.architecture.io

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

abstract class FirebaseRepository<ID, T>(
    firestore: FirebaseFirestore,
    collectionName: String,
) {
    protected val collection = firestore.collection(collectionName)

    protected abstract fun getId(entity: T): ID

    protected abstract fun idToString(id: ID): String

    protected abstract fun toDocument(entity: T): Map<String, Any?>

    protected abstract fun fromDocument(
        id: ID,
        doc: DocumentSnapshot,
    ): T

    protected abstract fun stringToId(id: String): ID

    suspend fun add(entity: T): Result<Unit, AppError> {
        val id = getId(entity)
        return setDocument(id, entity, mustExist = false)
    }

    suspend fun update(entity: T): Result<Unit, AppError> {
        val id = getId(entity)
        return setDocument(id, entity, mustExist = true)
    }

    suspend fun remove(id: ID): Result<Unit, AppError> =
        suspendCancellableCoroutine<Result<Unit, AppError>> { cont ->
            collection.document(idToString(id))
                .delete()
                .addOnSuccessListener {
                    cont.resume(Result.Ok(Unit))
                }
                .addOnFailureListener { e ->
                    cont.resume(
                        Result.Error(
                            AppError.Unexpected("Failed to delete document", e),
                        ),
                    )
                }
        }

    suspend fun get(id: ID): Result<T?, AppError> =
        suspendCancellableCoroutine<Result<T?, AppError>> { cont ->
            collection.document(idToString(id))
                .get()
                .addOnSuccessListener { snapshot ->
                    if (!snapshot.exists()) {
                        cont.resume(Result.Ok(null))
                        return@addOnSuccessListener
                    }
                    try {
                        val entity = fromDocument(id, snapshot)
                        cont.resume(Result.Ok(entity))
                    } catch (e: Exception) {
                        cont.resume(
                            Result.Error(
                                AppError.Unexpected("Failed to parse document", e),
                            ),
                        )
                    }
                }
                .addOnFailureListener { e ->
                    cont.resume(
                        Result.Error(
                            AppError.Unexpected("Failed to load document", e),
                        ),
                    )
                }
        }

    suspend fun getAll(): Result<List<T>, AppError> =
        suspendCancellableCoroutine<Result<List<T>, AppError>> { cont ->
            collection.get()
                .addOnSuccessListener { query ->
                    try {
                        val entities =
                            query.documents.map { doc ->
                                val id = stringToId(doc.id)
                                fromDocument(id, doc)
                            }
                        cont.resume(Result.Ok(entities))
                    } catch (e: Exception) {
                        cont.resume(
                            Result.Error(
                                AppError.Unexpected("Failed to parse documents", e),
                            ),
                        )
                    }
                }
                .addOnFailureListener { e ->
                    cont.resume(
                        Result.Error(
                            AppError.Unexpected("Failed to load collection", e),
                        ),
                    )
                }
        }

    suspend fun findFirstBy(
        field: String,
        value: Any,
    ): Result<T?, AppError> =
        suspendCancellableCoroutine<Result<T?, AppError>> { cont ->
            collection
                .whereEqualTo(field, value)
                .limit(1)
                .get()
                .addOnSuccessListener { query ->
                    if (query.isEmpty) {
                        cont.resume(Result.Ok(null))
                        return@addOnSuccessListener
                    }
                    try {
                        val doc = query.documents.first()
                        val id = stringToId(doc.id)
                        val entity = fromDocument(id, doc)
                        cont.resume(Result.Ok(entity))
                    } catch (e: Exception) {
                        cont.resume(
                            Result.Error(
                                AppError.Unexpected("Failed to parse document", e),
                            ),
                        )
                    }
                }
                .addOnFailureListener { e ->
                    cont.resume(
                        Result.Error(
                            AppError.Unexpected("Failed to query documents", e),
                        ),
                    )
                }
        }

    private suspend fun setDocument(
        id: ID,
        entity: T,
        mustExist: Boolean,
    ): Result<Unit, AppError> =
        suspendCancellableCoroutine<Result<Unit, AppError>> { cont ->
            val docRef = collection.document(idToString(id))
            val data = toDocument(entity)

            val task = if (mustExist) docRef.update(data) else docRef.set(data)

            task
                .addOnSuccessListener {
                    cont.resume(Result.Ok(Unit))
                }
                .addOnFailureListener { e ->
                    cont.resume(
                        Result.Error(
                            AppError.Unexpected("Failed to save document", e),
                        ),
                    )
                }
        }
}
