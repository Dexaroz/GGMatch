package com.pamn.ggmatch.app.architecture.sharedKernel.time

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class SystemTimeProvider : com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider {
    override fun now(): Instant = Clock.System.now()
}