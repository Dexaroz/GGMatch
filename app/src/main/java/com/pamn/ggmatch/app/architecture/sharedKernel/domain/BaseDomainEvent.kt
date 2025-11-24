package com.pamn.ggmatch.app.architecture.sharedKernel.domain

import java.time.Instant
import java.util.UUID

abstract class BaseDomainEvent(
    override val eventId: String = UUID.randomUUID().toString(),
    override val ocurredAt: Instant = Instant.now()
) : com.pamn.ggmatch.app.architecture.sharedKernel.domain.DomainEvent {
}