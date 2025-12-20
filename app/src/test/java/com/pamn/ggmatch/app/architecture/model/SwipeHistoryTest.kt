package com.pamn.ggmatch.app.architecture.model

import com.pamn.ggmatch.app.architecture.model.swipe.Swipe
import com.pamn.ggmatch.app.architecture.model.swipe.SwipeHistory
import com.pamn.ggmatch.app.architecture.model.swipe.SwipeType
import com.pamn.ggmatch.app.architecture.model.user.UserId
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.datetime.Instant
import org.junit.Test

class SwipeHistoryTest {
    private val ownerId = UserId("user-owner")
    private val targetId = UserId("user-target")
    private val initialTime = Instant.parse("2025-01-01T10:00:00Z")

    @Test
    fun `create should initialize history for user with no swipes`() {
        val history = SwipeHistory.create(ownerId, initialTime)

        assertEquals(ownerId, history.userId)
        assertTrue(history.items.isEmpty())
        assertEquals(initialTime, history.createdAt)
        assertEquals(initialTime, history.updatedAt)
    }

    @Test
    fun `add should record a new swipe and update history timestamp`() {
        val history = SwipeHistory.create(ownerId, initialTime)
        val swipeTime = Instant.parse("2025-01-01T11:00:00Z")

        val newSwipe =
            Swipe(
                fromUserId = ownerId,
                toUserId = targetId,
                type = SwipeType.LIKE,
                createdAt = swipeTime,
                updatedAt = swipeTime,
            )

        val updatedHistory = history.add(newSwipe)

        assertEquals(1, updatedHistory.items.size)
        assertEquals(newSwipe, updatedHistory.items[targetId])
        assertEquals(swipeTime, updatedHistory.updatedAt)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `add should fail if swipe sender does not match history owner`() {
        val history = SwipeHistory.create(ownerId, initialTime)
        val strangerId = UserId("stranger-danger")

        val invalidSwipe =
            Swipe(
                fromUserId = strangerId,
                toUserId = targetId,
                type = SwipeType.DISLIKE,
                createdAt = initialTime,
                updatedAt = initialTime,
            )

        history.add(invalidSwipe)
    }

    @Test
    fun `add should replace previous swipe when swiping on the same user again`() {
        val history = SwipeHistory.create(ownerId, initialTime)

        val firstSwipeTime = Instant.parse("2025-01-01T11:00:00Z")
        val firstSwipe = Swipe(ownerId, targetId, SwipeType.DISLIKE, firstSwipeTime, firstSwipeTime)

        val secondSwipeTime = Instant.parse("2025-01-01T12:00:00Z")
        val secondSwipe = Swipe(ownerId, targetId, SwipeType.LIKE, secondSwipeTime, secondSwipeTime)

        val historyAfterFirst = history.add(firstSwipe)
        val historyAfterSecond = historyAfterFirst.add(secondSwipe)

        assertEquals(1, historyAfterSecond.items.size)
        assertEquals(SwipeType.LIKE, historyAfterSecond.items[targetId]?.type)
        assertEquals(secondSwipeTime, historyAfterSecond.updatedAt)
    }

    @Test
    fun `fromPersistence should correctly reconstruct history state`() {
        val swipe = Swipe(ownerId, targetId, SwipeType.LIKE, initialTime, initialTime)
        val items = mapOf(targetId to swipe)
        val lastUpdate = Instant.parse("2025-01-02T10:00:00Z")

        val history =
            SwipeHistory.fromPersistence(
                userId = ownerId,
                items = items,
                createdAt = initialTime,
                updatedAt = lastUpdate,
            )

        assertEquals(ownerId, history.userId)
        assertEquals(items, history.items)
        assertEquals(initialTime, history.createdAt)
        assertEquals(lastUpdate, history.updatedAt)
    }
}
