package com.pamn.ggmatch.architecture.sharedKernel.time

import kotlinx.datetime.Instant

interface TimeProvider {
    fun now(): Instant
}