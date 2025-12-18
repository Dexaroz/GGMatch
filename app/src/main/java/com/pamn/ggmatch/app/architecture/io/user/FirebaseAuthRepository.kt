package com.pamn.ggmatch.app.architecture.io.user

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.pamn.ggmatch.app.architecture.model.user.Email
import com.pamn.ggmatch.app.architecture.model.user.User
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.model.user.Username
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume

class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val timeProvider: TimeProvider,
) : AuthRepository {
    override suspend fun register(
        email: Email,
        password: String,
        username: Username,
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
                    username = username,
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

    @OptIn(InternalCoroutinesApi::class)
    override suspend fun loginWithGoogle(idToken: String): Result<UserId, AppError> =
        suspendCancellableCoroutine { cont ->
            val credential = GoogleAuthProvider.getCredential(idToken, null)

            auth.signInWithCredential(credential)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid
                    val res =
                        if (uid == null) Result.Error(AppError.Unexpected("Missing Firebase UID"))
                        else Result.Ok(UserId(uid))

                    cont.tryResume(res)?.let { token -> cont.completeResume(token) }
                }
                .addOnFailureListener { e ->
                    val appError = when (e) {
                        is FirebaseAuthInvalidCredentialsException ->
                            AppError.Unexpected("Invalid Google credentials")
                        is FirebaseNetworkException ->
                            AppError.Unexpected("Network error")
                        else ->
                            AppError.Unexpected(e.message ?: "Unknown Firebase error")
                    }

                    cont.tryResume(Result.Error(appError))?.let { token -> cont.completeResume(token) }
                }
        }

    @OptIn(InternalCoroutinesApi::class)
    override suspend fun sendPasswordResetEmail(email: Email): Result<Unit, AppError> =
        suspendCancellableCoroutine { cont ->
            auth.sendPasswordResetEmail(email.value)
                .addOnSuccessListener {
                    cont.tryResume(Result.Ok(Unit))?.let { token -> cont.completeResume(token) }
                }
                .addOnFailureListener { e ->
                    val appError = when (e) {
                        is FirebaseAuthInvalidUserException ->
                            AppError.Unexpected("No user found for that email")
                        is FirebaseAuthInvalidCredentialsException ->
                            AppError.Unexpected("Invalid email")
                        is FirebaseNetworkException ->
                            AppError.Unexpected("Network error")
                        else ->
                            AppError.Unexpected(e.message ?: "Unknown Firebase error")
                    }

                    cont.tryResume(Result.Error(appError))?.let { token -> cont.completeResume(token) }
                }
        }
}
