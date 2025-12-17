package com.pamn.ggmatch.app

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.LoginUserCommandHandler
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.RegisterUserCommandHandler
import com.pamn.ggmatch.app.architecture.control.matching.FindPotentialMatchesUseCase
import com.pamn.ggmatch.app.architecture.control.matchmaking.commandsHandlers.UpsertMatchPreferencesCommandHandler
import com.pamn.ggmatch.app.architecture.control.swipe.commandsHandlers.SwipeProfileCommandHandler // ⭐️ NUEVO IMPORT
import com.pamn.ggmatch.app.architecture.io.preferences.FirebaseMatchPreferencesRepository
import com.pamn.ggmatch.app.architecture.io.preferences.MatchPreferencesRepository
import com.pamn.ggmatch.app.architecture.io.profile.FirebaseProfileRepository
import com.pamn.ggmatch.app.architecture.io.profile.ProfileRepository
import com.pamn.ggmatch.app.architecture.io.swipe.FirebaseSwipeInteractionsRepository // ⭐️ NUEVO IMPORT
import com.pamn.ggmatch.app.architecture.io.swipe.SwipeInteractionsRepository // ⭐️ NUEVO IMPORT
import com.pamn.ggmatch.app.architecture.io.user.AuthRepository
import com.pamn.ggmatch.app.architecture.io.user.FirebaseAuthRepository
import com.pamn.ggmatch.app.architecture.io.user.FirebaseUserRepository
import com.pamn.ggmatch.app.architecture.io.user.UserRepository
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.time.SystemTimeProvider
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider
import com.pamn.ggmatch.app.controllers.AuthController
import com.pamn.ggmatch.app.controllers.MatchPreferencesController

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

    lateinit var findPotentialMatchesUseCase: FindPotentialMatchesUseCase
        private set

    lateinit var matchPreferencesRepository: MatchPreferencesRepository
        private set

    lateinit var upsertMatchPreferencesHandler: UpsertMatchPreferencesCommandHandler
        private set

    lateinit var matchPreferencesController: MatchPreferencesController
        private set

    lateinit var swipeInteractionsRepository: SwipeInteractionsRepository //
        private set

    lateinit var swipeProfileCommandHandler: SwipeProfileCommandHandler //
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

        userRepository =
            FirebaseUserRepository(
                firestore = firestore,
            )

        authRepository =
            FirebaseAuthRepository(
                auth = firebaseAuth,
                userRepository = userRepository,
                timeProvider = timeProvider,
            )

        profileRepository =
            FirebaseProfileRepository(
                firestore = firestore,
            )

        swipeInteractionsRepository =
            FirebaseSwipeInteractionsRepository(
                firestore = firestore,
            )

        matchPreferencesRepository =
            FirebaseMatchPreferencesRepository(
                firestore = firestore,
            )

        upsertMatchPreferencesHandler =
            UpsertMatchPreferencesCommandHandler(
                matchPreferencesRepository = matchPreferencesRepository,
                timeProvider = timeProvider,
            )

        matchPreferencesController =
            MatchPreferencesController(
                upsertMatchPreferences = upsertMatchPreferencesHandler,
            )

        findPotentialMatchesUseCase =
            FindPotentialMatchesUseCase(
                repository = profileRepository,
            )

        authController =
            AuthController(
                registerUser = RegisterUserCommandHandler(authRepository),
                loginUser = LoginUserCommandHandler(authRepository),
            )

        initialized = true
    }
}
