package com.pamn.ggmatch.app

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.LoginUserCommandHandler
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.LoginWithGoogleCommandHandler
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.RegisterUserCommandHandler
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.SendPasswordResetEmailCommandHandler
import com.pamn.ggmatch.app.architecture.io.user.AuthRepository
import com.pamn.ggmatch.app.architecture.io.user.FirebaseAuthRepository
import com.pamn.ggmatch.app.architecture.io.user.FirebaseUserRepository
import com.pamn.ggmatch.app.architecture.io.user.UserRepository
import com.pamn.ggmatch.app.architecture.sharedKernel.time.SystemTimeProvider
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider
import com.pamn.ggmatch.app.controllers.AuthCommandHandlers

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

    lateinit var authRepository: AuthRepository
        private set

    lateinit var authCommandHandlers: AuthCommandHandlers
        private set

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

        authCommandHandlers =
            AuthCommandHandlers(
                registerUser =
                    RegisterUserCommandHandler(
                        authRepository = authRepository,
                    ),
                loginUser =
                    LoginUserCommandHandler(
                        authRepository = authRepository,
                    ),
                loginWithGoogle =
                    LoginWithGoogleCommandHandler(
                        authRepository = authRepository,
                    ),
                resetPassword =
                    SendPasswordResetEmailCommandHandler(
                        authRepository = authRepository,
                    ),
            )

        initialized = true
    }
}
