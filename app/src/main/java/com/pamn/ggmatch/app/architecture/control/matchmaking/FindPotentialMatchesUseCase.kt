package com.pamn.ggmatch.app.architecture.control.matchmaking

import MatchScoreCalculator
import com.pamn.ggmatch.app.architecture.io.profile.ProfileRepository
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.user.UserId

class FindPotentialMatchesUseCase(
    private val repository: ProfileRepository,
    private val calculator: MatchScoreCalculator = MatchScoreCalculator,
) {
    data class MatchCandidate(val profile: UserProfile, val score: Double)

    suspend operator fun invoke(
        currentUserId: UserId,
        minScore: Double = 10.0,
    ): List<MatchCandidate> {
        val currentUserProfile = repository.get(currentUserId).getOrNull() ?: return emptyList()
        val allCandidates = repository.getAll().getOrNull() ?: return emptyList()

        return allCandidates
            .filter { it.id != currentUserId }
            .map { candidate ->
                val score = calculator.calculateMatchScore(currentUserProfile, candidate)
                MatchCandidate(candidate, score)
            }
            .filter { it.score >= minScore }
            .sortedByDescending { it.score }
    }
}
