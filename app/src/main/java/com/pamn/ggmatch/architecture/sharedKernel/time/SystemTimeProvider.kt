package com.pamn.ggmatch.architecture.sharedKernel.time

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class SystemTimeProvider : TimeProvider {
    override fun now(): Instant = Clock.System.now()
}