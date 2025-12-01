package com.pamn.ggmatch.app.architecture.model.profile

class ProfileNavigator(private val profiles: List<Profile>) {

    private var index = 0

    fun current(): Profile = profiles[index]

    fun next(): Profile {
        index = (index + 1) % profiles.size
        return current()
    }

    fun previous(): Profile {
        index = if (index > 0) index - 1 else profiles.size - 1
        return current()
    }
}
