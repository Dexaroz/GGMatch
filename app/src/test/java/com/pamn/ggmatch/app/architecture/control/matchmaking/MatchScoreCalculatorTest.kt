package com.pamn.ggmatch.app.architecture.control.matchmaking

import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider
import com.pamn.ggmatch.app.test.architecture.io.profile.MockProfileRepository
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MatchScoreCalculatorTest {
    // 🚨 CORRECCIÓN: Renombrar a camel case minúscula para pasar Ktlint.
    private val userIdBuscador = UserId("U_01_BUSCADOR")
    private val userIdMaxMatch = UserId("U_02_MAX_MATCH")
    private val userIdNoMatch = UserId("U_06_NO_MATCH")
    private val userIdIncompleto = UserId("U_07_INCOMPLETO")
    private val userIdNoRiot = UserId("U_08_NO_RIOT")

    private lateinit var mockRepository: MockProfileRepository

    private val timeProvider =
        object : TimeProvider {
            override fun now(): Instant = Clock.System.now()
        }

    @Before
    fun setup() {
        mockRepository = MockProfileRepository(timeProvider)
    }

    @Test
    fun `scoreCalculation_isHigh_forMaxMatchCandidate`() =
        runBlocking {
            val userBuscador = mockRepository.get(userIdBuscador).getOrNull()!!
            val userMaxMatch = mockRepository.get(userIdMaxMatch).getOrNull()!!

            val score =
                MatchScoreCalculator.calculateMatchScore(
                    currentUserProfile = userBuscador,
                    candidateProfile = userMaxMatch,
                )

            assertTrue("El score debe ser alto (mayor a 60)", score > 60.0)
        }

    @Test
    fun `scoreCalculation_isZero_forSelfMatching`() =
        runBlocking {
            val userBuscador = mockRepository.get(userIdBuscador).getOrNull()!!

            val score =
                MatchScoreCalculator.calculateMatchScore(
                    currentUserProfile = userBuscador,
                    candidateProfile = userBuscador,
                )

            assertTrue("El score debe ser exactamente 0.0", score < 0.001)
        }

    @Test
    fun `scoreCalculation_isZero_forNoRiotAccount`() =
        runBlocking {
            val userBuscador = mockRepository.get(userIdBuscador).getOrNull()!!
            val userNoRiot = mockRepository.get(userIdNoRiot).getOrNull()!!

            val score =
                MatchScoreCalculator.calculateMatchScore(
                    currentUserProfile = userBuscador,
                    candidateProfile = userNoRiot,
                )

            assertTrue("El score debe ser 0.0 para usuarios sin cuenta Riot", score < 0.001)
        }

    @Test
    fun `scoreCalculation_isLow_forMinMatchCandidate`() =
        runBlocking {
            val userBuscador = mockRepository.get(userIdBuscador).getOrNull()!!
            val userNoMatch = mockRepository.get(userIdNoMatch).getOrNull()!!

            val score =
                MatchScoreCalculator.calculateMatchScore(
                    currentUserProfile = userBuscador,
                    candidateProfile = userNoMatch,
                )

            assertTrue("El score debe ser bajo (entre 0% y 15%)", score > 0.0 && score < 15.0)
        }

    @Test
    fun `scoreCalculation_isIntermediate_handlesEmptyRoles`() =
        runBlocking {
            val userBuscador = mockRepository.get(userIdBuscador).getOrNull()!!
            val userIncompleto = mockRepository.get(userIdIncompleto).getOrNull()!!

            val score =
                MatchScoreCalculator.calculateMatchScore(
                    currentUserProfile = userBuscador,
                    candidateProfile = userIncompleto,
                )

            assertTrue(
                "El score debe ser intermedio (entre 30% y 60%)",
                score > 30.0 && score < 60.0,
            )
        }
}
