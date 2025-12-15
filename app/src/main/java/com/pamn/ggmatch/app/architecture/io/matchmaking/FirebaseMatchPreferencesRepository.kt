package com.pamn.ggmatch.app.architecture.io.matchmaking

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.ggmatch.app.architecture.io.FirebaseRepository
import com.pamn.ggmatch.app.architecture.model.matchmaking.MatchPreferencesProfile
import com.pamn.ggmatch.app.architecture.model.matchmaking.preferences.MatchPreferences
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.datetime.Instant

class FirebaseMatchPreferencesRepository(
    firestore: FirebaseFirestore,
) : FirebaseRepository<UserId, MatchPreferencesProfile>(
        firestore = firestore,
        collectionName = "match-preferences",
    ),
    MatchPreferencesRepository {
    override fun getId(entity: MatchPreferencesProfile): UserId = entity.id

    override fun idToString(id: UserId): String = id.value

    override fun stringToId(id: String): UserId = UserId(id)

    override fun toDocument(entity: MatchPreferencesProfile): Map<String, Any?> {
        val prefs = entity.preferences

        return mapOf(
            "roles" to prefs.roles.map { it.name },
            "languages" to prefs.languages.map { it.name },
            "schedules" to prefs.schedules.map { it.name },
            "playstyles" to prefs.playstyles.map { it.name },
            "createdAtEpochMs" to entity.createdAt.toEpochMilliseconds(),
            "updatedAtEpochMs" to entity.updatedAt.toEpochMilliseconds(),
        )
    }

    override fun fromDocument(
        id: UserId,
        doc: DocumentSnapshot,
    ): MatchPreferencesProfile {
        fun rolesFromDoc(field: String): Set<LolRole> {
            val raw = doc.get(field) as? List<*> ?: emptyList<Any?>()
            return raw.mapNotNull { value ->
                (value as? String)?.let { name ->
                    runCatching { LolRole.valueOf(name) }.getOrNull()
                }
            }.toSet()
        }

        fun languagesFromDoc(field: String): Set<Language> {
            val raw = doc.get(field) as? List<*> ?: emptyList<Any?>()
            return raw.mapNotNull { value ->
                (value as? String)?.let { name ->
                    runCatching { Language.valueOf(name) }.getOrNull()
                }
            }.toSet()
        }

        fun scheduleFromDoc(field: String): Set<PlaySchedule> {
            val raw = doc.get(field) as? List<*> ?: emptyList<Any?>()
            return raw.mapNotNull { value ->
                (value as? String)?.let { name ->
                    runCatching { PlaySchedule.valueOf(name) }.getOrNull()
                }
            }.toSet()
        }

        fun playstyleFromDoc(field: String): Set<Playstyle> {
            val raw = doc.get(field) as? List<*> ?: emptyList<Any?>()
            return raw.mapNotNull { value ->
                (value as? String)?.let { name ->
                    runCatching { Playstyle.valueOf(name) }.getOrNull()
                }
            }.toSet()
        }

        val preferences =
            MatchPreferences(
                roles = rolesFromDoc("roles"),
                languages = languagesFromDoc("languages"),
                schedules = scheduleFromDoc("schedules"),
                playstyles = playstyleFromDoc("playstyles"),
            )

        val createdAtMs =
            doc.getLong("createdAtEpochMs")
                ?: error("Missing 'createdAtEpochMs' for profile ${id.value}")
        val updatedAtMs =
            doc.getLong("updatedAtEpochMs")
                ?: createdAtMs

        return MatchPreferencesProfile.fromPersistence(
            userId = id,
            preferences = preferences,
            createdAt = Instant.fromEpochMilliseconds(createdAtMs),
            updatedAt = Instant.fromEpochMilliseconds(updatedAtMs),
        )
    }

    override suspend fun addOrUpdate(profile: MatchPreferencesProfile): Result<Unit, AppError> =
        when (val existing = get(profile.id)) {
            is Result.Error -> existing
            is Result.Ok ->
                if (existing.value == null) {
                    add(profile)
                } else {
                    update(profile)
                }
        }
}
