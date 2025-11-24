package com.pamn.ggmatch.app.architecture.sharedKernel.domain

import java.time.Instant

interface DomainEvent : ValueObject {
    val eventId: String
    val ocurredAt: Instant
}
