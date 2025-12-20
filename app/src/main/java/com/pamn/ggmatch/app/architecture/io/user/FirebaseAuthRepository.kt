package com.pamn.ggmatch.app.architecture.io.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.pamn.ggmatch.app.architecture.model.user.Email
import com.pamn.ggmatch.app.architecture.model.user.User
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val timeProvider: TimeProvider,
) : AuthRepository {
    override suspend fun register(
        email: Email,
        password: String,
    ): Result<User, AppError> =
        try {
            val authResult =
                auth.createUserWithEmailAndPassword(email.value, password).await()

            val firebaseUser =
                authResult.user
                    ?: return Result.Error(
                        AppError.Unexpected("User creation succeeded but Firebase user is null"),
                    )

            val userId = UserId(firebaseUser.uid)

            val user =
                User.register(
                    id = userId,
                    email = email,
                    timeProvider = timeProvider,
                )

            when (val saveResult = userRepository.add(user)) {
                is Result.Ok -> Result.Ok(user)
                is Result.Error -> saveResult
            }
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.Error(AppError.Unexpected("Email already in use", e))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.Error(AppError.Unexpected("Invalid email or password format", e))
        } catch (e: Exception) {
            Result.Error(AppError.Unexpected("Failed to register user", e))
        }

    override suspend fun login(
        email: Email,
        password: String,
    ): Result<User, AppError> =
        try {
            val authResult =
                auth.signInWithEmailAndPassword(email.value, password).await()

            val firebaseUser =
                authResult.user
                    ?: return Result.Error(
                        AppError.Unexpected("Login succeeded but Firebase user is null"),
                    )

            val userId = UserId(firebaseUser.uid)

            when (val userResult = userRepository.get(userId)) {
                is Result.Ok -> {
                    val user = userResult.value
                    if (user == null) {
                        Result.Error(
                            AppError.Unexpected(
                                "User profile not found for uid=${userId.value}",
                                null,
                            ),
                        )
                    } else {
                        Result.Ok(user)
                    }
                }
                is Result.Error -> userResult
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.Error(AppError.Unexpected("User not found or disabled", e))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.Error(AppError.Unexpected("Invalid credentials", e))
        } catch (e: Exception) {
            Result.Error(AppError.Unexpected("Failed to login", e))
        }

    override suspend fun loginWithGoogle(idToken: String): Result<User, AppError> =
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()

            val firebaseUser =
                authResult.user
                    ?: return Result.Error(AppError.Unexpected("Google sign-in succeeded but Firebase user is null"))

            val emailValue =
                firebaseUser.email
                    ?: return Result.Error(AppError.Unexpected("Google user has no email (unexpected)"))

            val userId = UserId(firebaseUser.uid)
            val email = Email(emailValue)

            when (val userResult = userRepository.get(userId)) {
                is Result.Error -> userResult
                is Result.Ok -> {
                    val existing = userResult.value
                    if (existing != null) {
                        Result.Ok(existing)
                    } else {
                        val user =
                            User.registerWithGoogle(
                                id = userId,
                                email = email,
                                timeProvider = timeProvider,
                            )

                        when (val save = userRepository.add(user)) {
                            is Result.Ok -> Result.Ok(user)
                            is Result.Error -> save
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Result.Error(AppError.Unexpected("Failed to login with Google", e))
        }

    override suspend fun logout(): Result<Unit, AppError> =
        try {
            auth.signOut()
            Result.Ok(Unit)
        } catch (e: Exception) {
            Result.Error(AppError.Unexpected("Failed to logout", e))
        }

    override suspend fun getCurrentUser(): Result<User?, AppError> {
        val firebaseUser = auth.currentUser ?: return Result.Ok(null)

        val userId = UserId(firebaseUser.uid)
        return userRepository.get(userId)
    }
}
