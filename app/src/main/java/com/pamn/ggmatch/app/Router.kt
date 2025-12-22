package com.pamn.ggmatch.app

object Router {
    const val AUTH_LOGIN = "auth/login"
    const val AUTH_REGISTER = "auth/register"
    const val HOME = "home"

    const val PREFERENCES = "preferences"

    const val CHATS = "chats"

    const val CHAT = "chat/{otherUserId}"

    fun chat(otherUserId: String) = "chat/$otherUserId"

    const val PROFILE = "profile"
}
