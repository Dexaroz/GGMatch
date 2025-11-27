package com.pamn.ggmatch.app

import android.content.Context
import com.pamn.ggmatch.app.architecture.sharedKernel.time.SystemTimeProvider
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider

object AppContainer {
    private var initialized = false

    lateinit var timeProvider: TimeProvider
        private set

    fun init(context: Context) {
        if (initialized) return

        timeProvider = SystemTimeProvider()

        initialized = true
    }
}
