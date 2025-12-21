package com.pamn.ggmatch.app

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.LoginUserCommandHandler
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.LoginWithGoogleCommandHandler
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.RegisterUserCommandHandler
import com.pamn.ggmatch.app.architecture.control.chats.commandHandlers.EnsureConversationForMatchCommandHandler
import com.pamn.ggmatch.app.architecture.control.chats.commandHandlers.SendMessageCommandHandler
import com.pamn.ggmatch.app.architecture.control.matchmaking.commandsHandlers.UpsertMatchPreferencesCommandHandler
import com.pamn.ggmatch.app.architecture.control.profile.commandsHandlers.EnsureUserProfileExistsCommandHandler
import com.pamn.ggmatch.app.architecture.control.profile.commandsHandlers.UpsertUserProfileCommandHandler
import com.pamn.ggmatch.app.architecture.control.profile.commandsHandlers.VerifyRiotAccountCommandHandler
import com.pamn.ggmatch.app.architecture.io.chats.ChatRepository
import com.pamn.ggmatch.app.architecture.io.chats.FirebaseChatRepository
import com.pamn.ggmatch.app.architecture.io.images.CloudinaryProfileImageStrategy
import com.pamn.ggmatch.app.architecture.io.images.ProfileImageStrategy
import com.pamn.ggmatch.app.architecture.io.preferences.FirebaseMatchPreferencesRepository
import com.pamn.ggmatch.app.architecture.io.preferences.MatchPreferencesRepository
import com.pamn.ggmatch.app.architecture.io.profile.FirebaseProfileRepository
import com.pamn.ggmatch.app.architecture.io.profile.ProfileRepository
import com.pamn.ggmatch.app.architecture.io.swipe.FirebaseSwipeHistoryRepository
import com.pamn.ggmatch.app.architecture.io.swipe.SwipeHistoryRepository
import com.pamn.ggmatch.app.architecture.io.user.AuthRepository
import com.pamn.ggmatch.app.architecture.io.user.FirebaseAuthRepository
import com.pamn.ggmatch.app.architecture.io.user.FirebaseUserRepository
import com.pamn.ggmatch.app.architecture.io.user.UserRepository
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.time.SystemTimeProvider
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider
import com.pamn.ggmatch.app.controllers.AuthController
import com.pamn.ggmatch.app.controllers.ChatController
import com.pamn.ggmatch.app.controllers.MatchPreferencesController
import com.pamn.ggmatch.app.controllers.ProfileController
import com.pamn.ggmatch.app.riotApi.OkHttpRiotApiClient
import okhttp3.OkHttpClient

object AppContainer {
    private var initialized = false

    lateinit var timeProvider: TimeProvider
        private set

    lateinit var firestore: FirebaseFirestore
        private set

    lateinit var firebaseAuth: FirebaseAuth
        private set

    lateinit var userRepository: UserRepository
        private set

    lateinit var profileRepository: ProfileRepository
        private set

    lateinit var authRepository: AuthRepository
        private set

    lateinit var authController: AuthController
        private set

    lateinit var matchPreferencesRepository: MatchPreferencesRepository
        private set

    lateinit var upsertMatchPreferencesHandler: UpsertMatchPreferencesCommandHandler
        private set

    lateinit var matchPreferencesController: MatchPreferencesController
        private set

    lateinit var swipeInteractionsRepository: SwipeHistoryRepository
        private set

    lateinit var profileController: ProfileController
        private set

    lateinit var profileImageStrategy: ProfileImageStrategy
        private set

    lateinit var chatRepository: ChatRepository
        private set

    lateinit var chatController: ChatController
        private set

    lateinit var ensureConversationForMatchHandler: EnsureConversationForMatchCommandHandler
        private set

    lateinit var sendMessageHandler: SendMessageCommandHandler
        private set

    val currentUserId: UserId
        get() =
            firebaseAuth.currentUser?.uid?.let(::UserId)
                ?: throw IllegalStateException("Current user not authenticated.")

    fun init(context: Context) {
        if (initialized) return

        FirebaseApp.initializeApp(context)

        timeProvider = SystemTimeProvider()
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        userRepository = FirebaseUserRepository(firestore)
        profileRepository = FirebaseProfileRepository(firestore)

        val riotApiClient =
            OkHttpRiotApiClient(
                http = OkHttpClient(),
                apiKey = "RGAPI-4c96b4f9-e170-4363-aa27-c913f7e8bd49",
            )

        val verifyRiotAccountHandler =
            VerifyRiotAccountCommandHandler(
                riotApi = riotApiClient,
                profileRepository = profileRepository,
                timeProvider = timeProvider,
            )

        authRepository =
            FirebaseAuthRepository(
                auth = firebaseAuth,
                userRepository = userRepository,
                timeProvider = timeProvider,
            )

        swipeInteractionsRepository = FirebaseSwipeHistoryRepository(firestore)

        matchPreferencesRepository = FirebaseMatchPreferencesRepository(firestore)

        upsertMatchPreferencesHandler =
            UpsertMatchPreferencesCommandHandler(
                matchPreferencesRepository = matchPreferencesRepository,
                timeProvider = timeProvider,
            )

        matchPreferencesController =
            MatchPreferencesController(
                upsertMatchPreferences = upsertMatchPreferencesHandler,
            )

        chatRepository =
            FirebaseChatRepository(
                firestore = firestore,
            )

        val ensureUserProfileExistsHandler =
            EnsureUserProfileExistsCommandHandler(
                profileRepository = profileRepository,
                timeProvider = timeProvider,
            )

        val upsertUserProfileHandler =
            UpsertUserProfileCommandHandler(
                profileRepository = profileRepository,
                timeProvider = timeProvider,
            )

        profileController =
            ProfileController(
                ensureProfileExists = ensureUserProfileExistsHandler,
                upsertUserProfile = upsertUserProfileHandler,
                verifyRiotAccount = verifyRiotAccountHandler,
            )

        authController =
            AuthController(
                registerUser = RegisterUserCommandHandler(authRepository, ensureUserProfileExistsHandler),
                loginUser = LoginUserCommandHandler(authRepository),
                loginWithGoogle = LoginWithGoogleCommandHandler(authRepository),
            )

        profileImageStrategy =
            CloudinaryProfileImageStrategy(
                http = OkHttpClient(),
                cloudName = "dykbo5mzo",
                uploadPreset = "ggmatch_avatar_unsigned",
            )

        ensureConversationForMatchHandler =
            EnsureConversationForMatchCommandHandler(
                chatRepository = chatRepository
            )

        sendMessageHandler =
            SendMessageCommandHandler(
                chatRepository = chatRepository,
                timeProvider = timeProvider
            )

        chatController =
            ChatController(
                chatRepository = chatRepository,
                ensureConversation = ensureConversationForMatchHandler,
                sendMessage = sendMessageHandler
            )

        initialized = true
    }
}
