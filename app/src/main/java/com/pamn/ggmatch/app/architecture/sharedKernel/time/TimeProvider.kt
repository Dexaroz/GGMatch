package com.pamn.ggmatch.app.architecture.sharedKernel.time

import kotlinx.datetime.Instant

interface TimeProvider {
    fun now(): Instant
}