package com.pamn.ggmatch.app.architecture.control.matching

import com.pamn.ggmatch.app.architecture.model.profile.UserProfile

interface ProfileNavigator {
    fun current(): UserProfile?

    fun next(): UserProfile?
}
