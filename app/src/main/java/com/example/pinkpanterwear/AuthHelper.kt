package com.example.pinkpanterwear

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import java.io.IOException

class AuthHelper {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Registers a new user with email and password.
     * @return Result of the operation containing FirebaseUser on success.
     */
    suspend fun registerUser(email: String, password: String): Result<FirebaseUser> {
        return try {
            val userCredential = auth.createUserWithEmailAndPassword(email, password).await()
            val user = userCredential.user
            if (user != null) Result.success(user)
            else Result.failure(Exception("Firebase user is null after registration"))
        } catch (e: IOException) {
            Result.failure(Exception("Network error during registration", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Logs in an existing user with email and password.
     * @return Result of the operation containing FirebaseUser on success.
     */
    suspend fun loginUser(email: String, password: String): Result<FirebaseUser> {
        return try {
            val userCredential = auth.signInWithEmailAndPassword(email, password).await()
            val user = userCredential.user
            if (user != null) Result.success(user)
            else Result.failure(Exception("Firebase user is null after login"))
        } catch (e: IOException) {
            Result.failure(Exception("Network error during login", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Logs out the current user.
     */
    fun logoutUser() {
        auth.signOut()
    }

    /**
     * Get the current authenticated Firebase user.
     */
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    /**
     * Checks if a user is currently authenticated.
     * @return True if a user is authenticated, false otherwise.
     */
    fun isAuthenticated(): Boolean = auth.currentUser != null

    /**
     * Checks if the current authenticated user is an admin.
     * Temporary: checks against a hardcoded admin email.
     */
    fun isCurrentUserAdmin(): Boolean {
        return auth.currentUser?.email == "admin@example.com"
    }
}