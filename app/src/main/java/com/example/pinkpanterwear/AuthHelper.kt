package com.example.pinkpanterwear

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import kotlin.Result // Import Kotlin's Result class

class AuthHelper {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Registers a new user with email and password.
     * @return Result of the operation containing FirebaseUser on success.
     */
    suspend fun registerUser(email: String, password: String): Result<FirebaseUser> {
        return try {
            val userCredential = auth.createUserWithEmailAndPassword(email, password).await()
            userCredential.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Firebase user is null after registration"))
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
            userCredential.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Firebase user is null after login"))
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
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    /**
     * Checks if a user is currently authenticated.
     * @return True if a user is authenticated, false otherwise.
     */
    fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    /**
     * Observe changes in authentication state.
     * @return Flow of FirebaseUser (nullable).
     */
    // Note: Observing auth state is often done differently,
    // e.g., using FirebaseAuth.AuthStateListener or Flow wrappers
    // provided by libraries. This is a simplified example.
    // For production, consider a more robust approach.
    // For a simple check, use getCurrentUser() or listen via AuthStateListener.

    // Example of how you might use an AuthStateListener (not a suspend function or part of the class return):
    /*
    fun addAuthSateListener(listener: FirebaseAuth.AuthStateListener) {
        auth.addAuthStateListener(listener)
    }

    fun removeAuthStateListener(listener: FirebaseAuth.AuthStateListener) {
        auth.removeAuthStateListener(listener)
    }
    */
}