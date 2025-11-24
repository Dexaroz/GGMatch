package com.pamn.ggmatch.app.architecture.sharedKernel.domain

import java.time.Instant

interface DomainEvent : com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject {
    val eventId: String
    val ocurredAt: Instant
}