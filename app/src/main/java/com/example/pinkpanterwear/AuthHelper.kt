package com.example.pinkpanterwear

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.mockk
import kotlinx.coroutines.tasks.await

class AuthHelper {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val mockAdminUser: FirebaseUser = mockk(relaxed = true) {
        every { email } returns "admin@example.com"
        every { uid } returns "admin_uid"
    }

    private var useMock: Boolean = true

    suspend fun registerUser(email: String, password: String): Result<FirebaseUser> {
        if (useMock) {
            return Result.success(mockAdminUser)
        }
        return try {
            val userCredential = auth.createUserWithEmailAndPassword(email, password).await()
            userCredential.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Firebase user is null after registration"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<FirebaseUser> {
        if (useMock) {
            return Result.success(mockAdminUser)
        }
        return try {
            val userCredential = auth.signInWithEmailAndPassword(email, password).await()
            userCredential.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Firebase user is null after login"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logoutUser() {
        if (!useMock) {
            auth.signOut()
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return if (useMock) mockAdminUser else auth.currentUser
    }

    fun isAuthenticated(): Boolean {
        return if (useMock) true else auth.currentUser != null
    }

    fun isCurrentUserAdmin(): Boolean {
        val user = getCurrentUser()
        return user?.email == "admin@example.com"
    }

    fun setUseMock(shouldUseMock: Boolean) {
        this.useMock = shouldUseMock
    }
}