package com.pamn.ggmatch.app.architecture.view.profile.view

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.AppContainer
import com.pamn.ggmatch.app.architecture.control.profile.commands.VerifyRiotAccountCommand
import com.pamn.ggmatch.app.architecture.model.profile.UserPhotoUrl
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.profile.Username
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Preferences
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccountStatus
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.view.matchPreferences.components.matchPreferenceChip
import com.pamn.ggmatch.app.architecture.view.profiles.ProfileTextVariables
import com.pamn.ggmatch.app.architecture.view.profiles.components.gradientVerifyButton
import com.pamn.ggmatch.app.architecture.view.profiles.components.preferenceChipsFlowRow
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun profileEditView(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    texts: ProfileTextVariables = ProfileTextVariables(),
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val firebaseUid = AppContainer.firebaseAuth.currentUser?.uid
    if (firebaseUid == null) {
        Column(modifier = modifier.padding(16.dp)) {
            Text(texts.notAuthenticatedText)
            Spacer(Modifier.height(12.dp))
            Button(onClick = onBack) { Text(texts.backText) }
        }
        return
    }
    val userId = remember(firebaseUid) { UserId(firebaseUid) }

    var isLoading by rememberSaveable { mutableStateOf(true) }
    var isBusy by rememberSaveable { mutableStateOf(false) }

    var errorText by rememberSaveable { mutableStateOf<String?>(null) }
    var infoText by rememberSaveable { mutableStateOf<String?>(null) }

    var usernameText by rememberSaveable { mutableStateOf("") }
    var showEditUsername by rememberSaveable { mutableStateOf(false) }

    var pickedImageUri by rememberSaveable { mutableStateOf<String?>(null) }

    var cameraOutputUri by rememberSaveable { mutableStateOf<String?>(null) }

    var showImagePickerSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var riotGameName by rememberSaveable { mutableStateOf("") }
    var riotTagLine by rememberSaveable { mutableStateOf("") }
    var riotSummary by rememberSaveable { mutableStateOf<String?>(null) }
    var riotStatus by rememberSaveable { mutableStateOf(RiotAccountStatus.UNVERIFIED) }

    var selectedRoles by remember { mutableStateOf(setOf<LolRole>()) }
    var selectedLanguages by remember { mutableStateOf(setOf<Language>()) }
    var selectedSchedules by remember { mutableStateOf(setOf<PlaySchedule>()) }
    var selectedPlaystyles by remember { mutableStateOf(setOf<Playstyle>()) }

    fun uploadAndPersist(uri: Uri) {
        scope.launch {
            isBusy = true
            errorText = null
            infoText = null

            try {
                val photoUrlVo =
                    AppContainer.profileImageStrategy.save(
                        context = context,
                        userId = userId,
                        source = uri,
                    )

                val profile =
                    when (val get = AppContainer.profileRepository.get(userId)) {
                        is Result.Error -> {
                            errorText = texts.loadErrorText
                            isBusy = false
                            return@launch
                        }
                        is Result.Ok ->
                            get.value
                                ?: UserProfile.createNew(id = userId, timeProvider = AppContainer.timeProvider)
                    }

                profile.changePhotoUrl(photoUrlVo, AppContainer.timeProvider)

                when (val saved = AppContainer.profileRepository.addOrUpdate(profile)) {
                    is Result.Error -> errorText = texts.saveErrorText
                    is Result.Ok -> {
                        pickedImageUri = photoUrlVo.value
                        infoText = texts.profileSavedText
                    }
                }
            } catch (e: Exception) {
                errorText = e.message ?: "Upload failed"
            } finally {
                isBusy = false
            }
        }
    }

    val imagePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                pickedImageUri = uri.toString()
                showImagePickerSheet = false
                uploadAndPersist(uri)
            }
        }

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
            showImagePickerSheet = false
            if (!ok) return@rememberLauncherForActivityResult

            val uri = cameraOutputUri?.let(Uri::parse) ?: return@rememberLauncherForActivityResult
            pickedImageUri = uri.toString()
            uploadAndPersist(uri)
        }

    fun launchCamera() {
        val uri = createTempImageUri(context)
        cameraOutputUri = uri.toString()
        takePictureLauncher.launch(uri)
    }

    LaunchedEffect(userId) {
        isLoading = true
        errorText = null
        infoText = null

        when (val res = AppContainer.profileRepository.get(userId)) {
            is Result.Error -> {
                isLoading = false
                errorText = texts.loadErrorText
            }

            is Result.Ok -> {
                val profile =
                    res.value ?: UserProfile.createNew(id = userId, timeProvider = AppContainer.timeProvider)

                if (res.value == null) {
                    AppContainer.profileRepository.addOrUpdate(profile)
                }

                usernameText = profile.username?.value.orEmpty()

                pickedImageUri = profile.photoUrl?.value

                riotGameName = profile.riotAccount?.gameName.orEmpty()
                riotTagLine = profile.riotAccount?.tagLine.orEmpty()
                riotStatus = profile.riotAccount?.verificationStatus ?: RiotAccountStatus.UNVERIFIED

                selectedRoles = profile.preferences.favoriteRoles
                selectedLanguages = profile.preferences.languages
                selectedSchedules = profile.preferences.playSchedule
                selectedPlaystyles = profile.preferences.playstyle

                riotSummary = riotSummaryText(profile)
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (showEditUsername) {
        editUsernameDialog(
            initial = usernameText,
            title = texts.usernameLabel,
            onDismiss = { showEditUsername = false },
            onSave = { newValue ->
                usernameText = newValue
                errorText = null
                infoText = null
                showEditUsername = false
            },
        )
    }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState()),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(240.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.login_header),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0x00000000), Color(0xAA000000)),
                                ),
                            ),
                )

                Row(
                    modifier =
                        Modifier
                            .align(Alignment.TopStart)
                            .fillMaxWidth()
                            .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.undo),
                            contentDescription = texts.backText,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = usernameText.trim().ifBlank { texts.usernameLabel },
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-28).dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                ElevatedCard(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(modifier = Modifier.size(86.dp)) {
                                val painter =
                                    if (!pickedImageUri.isNullOrBlank()) {
                                        rememberAsyncImagePainter(pickedImageUri)
                                    } else {
                                        painterResource(id = R.drawable.login_header)
                                    }

                                Image(
                                    painter = painter,
                                    contentDescription = null,
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentScale = ContentScale.Crop,
                                )

                                FloatingActionButton(
                                    onClick = { showImagePickerSheet = true },
                                    modifier =
                                        Modifier
                                            .align(Alignment.BottomEnd)
                                            .size(34.dp),
                                    shape = CircleShape,
                                    containerColor = MaterialTheme.colorScheme.primary,
                                ) {
                                    Icon(Icons.Default.CameraAlt, contentDescription = texts.takePictureText)
                                }
                            }

                            Spacer(Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = usernameText.trim().ifBlank { texts.usernameLabel },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f),
                                    )

                                    IconButton(
                                        enabled = !isBusy,
                                        onClick = { showEditUsername = true },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = texts.usernameLabel,
                                        )
                                    }

                                    IconButton(
                                        enabled = !isBusy,
                                        onClick = {
                                            AppContainer.firebaseAuth.signOut()

                                            pickedImageUri = null
                                            usernameText = ""
                                            riotGameName = ""
                                            riotTagLine = ""
                                            riotSummary = null
                                            riotStatus = RiotAccountStatus.UNVERIFIED

                                            onLogout()
                                        },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Logout,
                                            contentDescription = "Logout",
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                ElevatedCard(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = texts.riotTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.weight(0.65f),
                                value = riotGameName,
                                onValueChange = {
                                    riotGameName = it
                                    errorText = null
                                    infoText = null
                                },
                                label = { Text(texts.riotGameNameLabel) },
                                singleLine = true,
                            )

                            OutlinedTextField(
                                modifier = Modifier.weight(0.35f),
                                value = riotTagLine,
                                onValueChange = {
                                    riotTagLine = it
                                    errorText = null
                                    infoText = null
                                },
                                label = { Text(texts.riotTagLabel) },
                                singleLine = true,
                            )
                        }

                        val riotVerified = riotStatus == RiotAccountStatus.VERIFIED
                        val canVerify = !isBusy && riotGameName.isNotBlank() && riotTagLine.isNotBlank()

                        gradientVerifyButton(
                            text = if (isBusy) texts.riotVerifyingText else texts.riotVerifyButtonText,
                            verified = riotVerified,
                            enabled = canVerify,
                            onClick = {
                                scope.launch {
                                    isBusy = true
                                    errorText = null
                                    infoText = null

                                    val cmd =
                                        VerifyRiotAccountCommand(
                                            userId = userId,
                                            gameName = riotGameName.trim(),
                                            tagLine = riotTagLine.trim(),
                                        )

                                    when (val res = AppContainer.profileController.verifyRiotAccount(cmd)) {
                                        is Result.Error -> {
                                            isBusy = false
                                            errorText = res.error.toString()
                                            riotStatus = RiotAccountStatus.UNVERIFIED
                                        }

                                        is Result.Ok -> {
                                            when (val p = AppContainer.profileRepository.get(userId)) {
                                                is Result.Error -> {
                                                    isBusy = false
                                                    errorText = texts.loadErrorText
                                                    riotStatus = RiotAccountStatus.UNVERIFIED
                                                }

                                                is Result.Ok -> {
                                                    isBusy = false
                                                    infoText = texts.riotSavedText

                                                    val prof = p.value
                                                    riotStatus =
                                                        prof?.riotAccount?.verificationStatus
                                                            ?: RiotAccountStatus.UNVERIFIED
                                                    riotSummary = riotSummaryText(prof)
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                        )

                        if (!riotSummary.isNullOrBlank()) {
                            Text(riotSummary.orEmpty(), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                ElevatedCard(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(texts.favoriteRoleTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(texts.favoriteRoleHint, style = MaterialTheme.typography.bodySmall)

                        preferenceChipsFlowRow {
                            LolRole.entries.forEach { role ->
                                matchPreferenceChip(
                                    label = role.name,
                                    selected = selectedRoles.contains(role),
                                    iconRes = role.iconRes,
                                    enabled = selectedRoles.contains(role) || selectedRoles.size < Preferences.MAX_FAVORITE_ROLES,
                                ) {
                                    selectedRoles =
                                        if (selectedRoles.contains(role)) selectedRoles - role else selectedRoles + role
                                }
                            }
                        }

                        Text(texts.scheduleTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        preferenceChipsFlowRow {
                            PlaySchedule.entries.forEach { s ->
                                matchPreferenceChip(
                                    label = s.name,
                                    selected = selectedSchedules.contains(s),
                                    iconRes = s.iconRes,
                                ) {
                                    selectedSchedules = toggleSet(selectedSchedules, s)
                                }
                            }
                        }

                        Text(texts.languageTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        preferenceChipsFlowRow {
                            Language.entries.forEach { l ->
                                matchPreferenceChip(
                                    label = l.name,
                                    selected = selectedLanguages.contains(l),
                                    iconRes = l.iconRes,
                                ) {
                                    selectedLanguages = toggleSet(selectedLanguages, l)
                                }
                            }
                        }

                        Text(texts.playstyleTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        preferenceChipsFlowRow {
                            Playstyle.entries.forEach { p ->
                                matchPreferenceChip(
                                    label = p.name,
                                    selected = selectedPlaystyles.contains(p),
                                    iconRes = p.iconRes,
                                ) {
                                    selectedPlaystyles = toggleSet(selectedPlaystyles, p)
                                }
                            }
                        }
                    }
                }

                if (errorText != null) Text("❌ ${errorText.orEmpty()}")
                if (infoText != null) Text("✅ ${infoText.orEmpty()}")

                Button(
                    enabled = !isBusy,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            isBusy = true
                            errorText = null
                            infoText = null

                            val usernameVo =
                                usernameText.trim().takeIf { it.isNotBlank() }?.let {
                                    runCatching { Username(it) }.getOrNull()
                                }

                            if (usernameText.isNotBlank() && usernameVo == null) {
                                isBusy = false
                                errorText = texts.invalidUsernameText
                                return@launch
                            }

                            val prefs =
                                runCatching {
                                    Preferences(
                                        favoriteRoles = selectedRoles,
                                        languages = selectedLanguages,
                                        playSchedule = selectedSchedules,
                                        playstyle = selectedPlaystyles,
                                    )
                                }.getOrElse {
                                    isBusy = false
                                    errorText = texts.invalidPreferencesText
                                    return@launch
                                }

                            val profile =
                                when (val get = AppContainer.profileRepository.get(userId)) {
                                    is Result.Error -> {
                                        isBusy = false
                                        errorText = texts.loadErrorText
                                        return@launch
                                    }

                                    is Result.Ok ->
                                        get.value ?: UserProfile.createNew(id = userId, timeProvider = AppContainer.timeProvider)
                                }

                            profile.changeUsername(usernameVo, AppContainer.timeProvider)
                            profile.updatePreferences(prefs, AppContainer.timeProvider)

                            val url = pickedImageUri
                            val urlVo = url?.takeIf { it.startsWith("http") }?.let { UserPhotoUrl.from(it) }

                            if (urlVo != null) {
                                profile.changePhotoUrl(urlVo, AppContainer.timeProvider)
                            }

                            when (val saved = AppContainer.profileRepository.addOrUpdate(profile)) {
                                is Result.Error -> {
                                    isBusy = false
                                    errorText = texts.saveErrorText
                                }

                                is Result.Ok -> {
                                    isBusy = false
                                    infoText = texts.profileSavedText
                                }
                            }
                        }
                    },
                ) {
                    Text(if (isBusy) texts.savingText else texts.saveButtonText)
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }

    if (showImagePickerSheet) {
        ModalBottomSheet(
            onDismissRequest = { showImagePickerSheet = false },
            sheetState = sheetState,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = texts.changeImageText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isBusy,
                    onClick = {
                        showImagePickerSheet = false
                        imagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                        )
                    },
                ) {
                    Text(texts.selectGalleryPictureText)
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isBusy,
                    onClick = {
                        showImagePickerSheet = false
                        launchCamera()
                    },
                ) {
                    Text(texts.takePictureText)
                }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showImagePickerSheet = false },
                ) {
                    Text(texts.backText)
                }

                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun editUsernameDialog(
    initial: String,
    title: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var value by rememberSaveable { mutableStateOf(initial) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            TextButton(onClick = { onSave(value) }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

private fun <T> toggleSet(
    set: Set<T>,
    item: T,
): Set<T> = if (set.contains(item)) set - item else set + item

private fun riotSummaryText(profile: UserProfile?): String? {
    val riot = profile?.riotAccount ?: return null
    val soloq = riot.soloq

    if (soloq == null) return "Riot: ${riot.gameName}#${riot.tagLine} · SOLOQ: Unranked"

    val div = soloq.division?.let { " $it" }.orEmpty()
    val total = soloq.wins + soloq.losses
    val wr = if (total == 0) 0 else ((soloq.wins.toDouble() / total.toDouble()) * 100.0).roundToInt()

    return "Riot: ${riot.gameName}#${riot.tagLine}\nSOLOQ: ${soloq.tier}$div · ${soloq.leaguePoints} LP · WR: $wr%"
}

private fun createTempImageUri(context: Context): Uri {
    val imagesDir = File(context.cacheDir, "images").apply { mkdirs() }
    val file = File.createTempFile("profile_", ".jpg", imagesDir)
    val authority = context.packageName + ".fileprovider"
    return FileProvider.getUriForFile(context, authority, file)
}
