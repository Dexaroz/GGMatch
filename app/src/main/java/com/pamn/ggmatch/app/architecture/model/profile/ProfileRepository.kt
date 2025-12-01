package com.pamn.ggmatch.app.architecture.model.profile

interface ProfileRepository {
    fun allProfiles(): List<Profile>
}
