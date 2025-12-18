package com.pamn.ggmatch.app.architecture.model.profile

interface ProfileNavigator {
    fun current(): UserProfile?

    fun next(): UserProfile?
}
