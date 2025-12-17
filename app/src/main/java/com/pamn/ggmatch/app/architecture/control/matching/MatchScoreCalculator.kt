import com.pamn.ggmatch.app.architecture.model.profile.UserProfile

object MatchScoreCalculator {
    private const val ROLE_WEIGHT = 0.15
    private const val LANGUAGE_WEIGHT = 0.4
    private const val SCHEDULE_WEIGHT = 0.30
    private const val PLAYSTYLE_WEIGHT = 0.15

    fun calculateMatchScore(
        currentUserProfile: UserProfile,
        candidateProfile: UserProfile,
    ): Double {
        if (candidateProfile.riotAccount == null || currentUserProfile.id == candidateProfile.id) {
            return 0.0
        }

        val userPrefs = currentUserProfile.preferences
        val candidatePrefs = candidateProfile.preferences

        fun <T> calculateJaccard(
            setA: Set<T>,
            setB: Set<T>,
        ): Double {
            if (setA.isEmpty() && setB.isEmpty()) return 1.0 // Si ambos están vacíos, es 100% compatible
            val intersectionSize = setA.intersect(setB).size.toDouble()
            val unionSize = setA.union(setB).size.toDouble()
            return if (unionSize == 0.0) 0.0 else intersectionSize / unionSize
        }

        val roleScore = calculateJaccard(userPrefs.favoriteRoles, candidatePrefs.favoriteRoles)
        val langScore = calculateJaccard(userPrefs.languages, candidatePrefs.languages)
        val scheduleScore = calculateJaccard(userPrefs.playSchedule, candidatePrefs.playSchedule)
        val playstyleScore = calculateJaccard(userPrefs.playstyle, candidatePrefs.playstyle)

        val finalScore = (
            (roleScore * ROLE_WEIGHT) +
                (langScore * LANGUAGE_WEIGHT) +
                (scheduleScore * SCHEDULE_WEIGHT) +
                (playstyleScore * PLAYSTYLE_WEIGHT)
        )

        return finalScore * 100.0
    }
}
