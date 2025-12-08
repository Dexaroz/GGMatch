package com.pamn.ggmatch.app

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.LoginUserCommandHandler
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.RegisterUserCommandHandler
import com.pamn.ggmatch.app.architecture.control.swipe.ProfilePresenterImplementation
import com.pamn.ggmatch.app.architecture.io.user.AuthRepository
import com.pamn.ggmatch.app.architecture.io.user.FirebaseAuthRepository
import com.pamn.ggmatch.app.architecture.io.user.FirebaseUserRepository
import com.pamn.ggmatch.app.architecture.io.user.UserRepository
import com.pamn.ggmatch.app.architecture.model.profile.Profile
import com.pamn.ggmatch.app.architecture.model.profile.ProfileNavigator
import com.pamn.ggmatch.app.architecture.sharedKernel.time.SystemTimeProvider
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider
import com.pamn.ggmatch.app.controllers.AuthController

object AppContainer {
    private var initialized = false

    // -------------------------
    // 🔥 AUTH / FIREBASE
    // -------------------------
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

    lateinit var authController: AuthController
        private set

    lateinit var profiles: List<Profile>
        private set

    lateinit var presenter: ProfilePresenterImplementation
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

        authController =
            AuthController(
                registerUser = RegisterUserCommandHandler(authRepository),
                loginUser = LoginUserCommandHandler(authRepository),
            )

        profiles =
            listOf(
                Profile(
                    id = 1,
                    name = "Laura Martínez",
                    nickname = "lau21",
                    age = 21,
                    description = "Amo los perros y la fotografía",
                    imageRes = R.drawable.profile_picture,
                    backgroundImageRes = R.drawable.twisted,
                ),
                Profile(
                    id = 2,
                    name = "Carlos Rivera",
                    nickname = "carlitos",
                    age = 22,
                    description = "Me encanta viajar y probar comida nueva",
                    imageRes = R.drawable.profile_picture,
                    backgroundImageRes = R.drawable.jinx,
                ),
                Profile(
                    id = 3,
                    name = "Marta Gómez",
                    nickname = "marti",
                    age = 19,
                    description = "Fan del cine y atardeceres",
                    imageRes = R.drawable.profile_picture,
                    backgroundImageRes = R.drawable.thresh,
                ),
            )

        val navigator = ProfileNavigator(profiles)

        presenter =
            ProfilePresenterImplementation(
                view = null,
                navigator = navigator,
            )

        presenter.init()

        initialized = true
    }
}
