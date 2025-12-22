package com.pamn.ggmatch.app.architecture.view.chats.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.AppContainer
import com.pamn.ggmatch.app.architecture.control.chats.commands.EnsureConversationForMatchCommand
import com.pamn.ggmatch.app.architecture.control.chats.commands.SendMessageCommand
import com.pamn.ggmatch.app.architecture.model.chats.ChatMessage
import com.pamn.ggmatch.app.architecture.model.chats.ConversationId
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.coroutines.launch

@Composable
fun chatScreen(
    otherUserId: UserId,
    onBack: () -> Unit,
) {
    val me = AppContainer.currentUserId
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var conversationId by remember { mutableStateOf<ConversationId?>(null) }
    var messages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }
    var input by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val listState = rememberLazyListState()

    val createErrorText = remember { context.getString(R.string.chat_create_error) }
    val loadErrorText = remember { context.getString(R.string.chat_load_error) }
    val sendErrorText = remember { context.getString(R.string.chat_send_error) }
    val loadingText = remember { context.getString(R.string.chat_loading) }

    LaunchedEffect(me, otherUserId) {
        val cmd = EnsureConversationForMatchCommand(me = me, other = otherUserId)
        when (val res = AppContainer.ensureConversationForMatchHandler.invoke(cmd)) {
            is Result.Ok -> {
                conversationId = res.value
                error = null
            }
            is Result.Error -> {
                conversationId = null
                error = createErrorText
            }
        }
    }

    val conv = conversationId
    if (conv == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(error ?: loadingText, color = Color.White)
        }
        return
    }

    LaunchedEffect(conv.value) {
        AppContainer.chatRepository.observeMessages(conv).collect { res ->
            when (res) {
                is Result.Ok -> {
                    messages = res.value
                    error = null
                }
                is Result.Error -> error = loadErrorText
            }
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isEmpty()) return@LaunchedEffect
        val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        val lastIndex = messages.lastIndex
        val nearBottom = (lastIndex - lastVisible) <= 2
        if (nearBottom) listState.animateScrollToItem(lastIndex)
    }

    LaunchedEffect(Unit) {
        snapshotFlow { input }.collect { txt ->
            if (txt.isNotBlank() && messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.lastIndex)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF1A1A1A), Color(0xFF322E3D), Color(0xFF1A1A1A)),
                        ),
                    ),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding()
                    .imePadding(),
        ) {
            Spacer(Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.undo),
                    contentDescription = stringResource(R.string.chats_back_description),
                    tint = Color.White,
                    modifier = Modifier.size(28.dp).clickable { onBack() },
                )

                Spacer(Modifier.width(16.dp))

                Text(
                    text = stringResource(R.string.chat_title),
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            if (error != null) {
                Text(
                    text = stringResource(R.string.chat_error_prefix, error.orEmpty()),
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 8.dp),
            ) {
                items(messages) { m ->
                    val isMine = (m.senderId == me)
                    chatBubble(text = m.text.value, isMine = isMine)
                }
            }

            Surface(
                color = Color.Transparent,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = input,
                        onValueChange = { input = it },
                        placeholder = { Text(stringResource(R.string.chat_message_placeholder)) },
                        singleLine = true,
                        colors =
                            TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White,
                                focusedPlaceholderColor = Color(0xFFB0B0B0),
                                unfocusedPlaceholderColor = Color(0xFFB0B0B0),
                                focusedContainerColor = Color(0x22000000),
                                unfocusedContainerColor = Color(0x22000000),
                                focusedIndicatorColor = Color.White,
                                unfocusedIndicatorColor = Color(0x66FFFFFF),
                            ),
                    )

                    Spacer(Modifier.width(10.dp))

                    Button(
                        enabled = input.trim().isNotBlank(),
                        onClick = {
                            val text = input
                            input = ""

                            scope.launch {
                                val cmd =
                                    SendMessageCommand(
                                        conversationIdRaw = conv.value,
                                        from = me,
                                        to = otherUserId,
                                        text = text,
                                    )

                                when (AppContainer.sendMessageHandler.invoke(cmd)) {
                                    is Result.Ok -> error = null
                                    is Result.Error -> error = sendErrorText
                                }
                            }
                        },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4E7FFF),
                                contentColor = Color.White,
                                disabledContainerColor = Color(0x664E7FFF),
                                disabledContentColor = Color.White,
                            ),
                    ) {
                        Text(stringResource(R.string.chat_send))
                    }
                }
            }
        }
    }
}

@Composable
private fun chatBubble(
    text: String,
    isMine: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = if (isMine) Color(0xFF4E7FFF) else Color(0x33FFFFFF),
            tonalElevation = 2.dp,
        ) {
            Text(
                text = text,
                modifier =
                    Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .widthIn(max = 280.dp),
                color = Color.White,
                textAlign = TextAlign.Start,
            )
        }
    }
}
