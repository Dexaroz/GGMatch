package com.pamn.ggmatch.app.architecture.view.chats.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.pamn.ggmatch.app.AppContainer
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.view.matches.view.chatsView
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.coroutines.launch

@Composable
fun chatsScreen(
    onBack: () -> Unit,
    onOpenChat: (UserId) -> Unit,
) {
    val me = AppContainer.currentUserId
    val scope = rememberCoroutineScope()

    val profilesState = remember { mutableStateOf<List<UserProfile>>(emptyList()) }
    val lastMessagesState = remember { mutableStateOf<Map<UserId, String>>(emptyMap()) }

    LaunchedEffect(me) {
        AppContainer.chatRepository.observeConversations(me).collect { res ->
            when (res) {
                is Result.Error -> {
                    profilesState.value = emptyList()
                    lastMessagesState.value = emptyMap()
                }

                is Result.Ok -> {
                    val summaries = res.value

                    lastMessagesState.value =
                        summaries.associate { s ->
                            s.otherUserId to (s.lastMessageText ?: "")
                        }

                    val loaded = mutableListOf<UserProfile>()
                    summaries.forEach { s ->
                        scope.launch {
                            val p = AppContainer.profileRepository.get(s.otherUserId)
                            if (p is Result.Ok && p.value != null) {
                                loaded.add(p.value)
                                profilesState.value = loaded.toList()
                            }
                        }
                    }
                }
            }
        }
    }

    chatsView(
        profiles = profilesState.value,
        lastMessagesByUserId = lastMessagesState.value,
        onBack = onBack,
        onOpenChat = { profile -> onOpenChat(profile.id) },
    )
}