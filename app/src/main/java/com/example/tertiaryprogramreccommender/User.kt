package com.example.tertiaryprogramreccommender

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class User : ViewModel() {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val mutableAuthState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> get() = mutableAuthState

    init {
        validateUser()
    }

    private fun validateUser() {
        mutableAuthState.value = if (auth.currentUser != null) {
            AuthState.Authenticated
        } else {
            AuthState.Unauthenticated
        }
    }

    // Handle login
    fun handleLogin(email: String, password: String) {
        when {
            email.isEmpty() -> {
                mutableAuthState.value = AuthState.Error("Email cannot be empty.")
                return
            }
            !validateEmail(email) -> {
                mutableAuthState.value = AuthState.Error("Invalid email format.")
                return
            }
            password.isEmpty() -> {
                mutableAuthState.value = AuthState.Error("Password cannot be empty.")
                return
            }
            password.length < 6 -> {
                mutableAuthState.value = AuthState.Error("Password must be at least 6 characters.")
                return
            }
            else -> {
                mutableAuthState.value = AuthState.Loading
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        mutableAuthState.value = if (task.isSuccessful) {
                            AuthState.Authenticated
                        } else {
                            AuthState.Error(task.exception?.localizedMessage ?: "Login failed.")
                        }
                    }
            }
        }
    }

    // Handle signup
    fun handleSignup(username: String, email: String, password: String, confirmPassword: String) {
        when {
            username.isEmpty() -> {
                mutableAuthState.value = AuthState.Error("Username cannot be empty.")
                return
            }
            email.isEmpty() -> {
                mutableAuthState.value = AuthState.Error("Email cannot be empty.")
                return
            }
            !validateEmail(email) -> {
                mutableAuthState.value = AuthState.Error("Invalid email format.")
                return
            }
            password.isEmpty() -> {
                mutableAuthState.value = AuthState.Error("Password cannot be empty.")
                return
            }
            password.length < 6 -> {
                mutableAuthState.value = AuthState.Error("Password must be at least 6 characters.")
                return
            }
            confirmPassword.isEmpty() -> {
                mutableAuthState.value = AuthState.Error("Please confirm your password.")
                return
            }
            password != confirmPassword -> {
                mutableAuthState.value = AuthState.Error("Passwords do not match.")
                return
            }
            else -> {
                mutableAuthState.value = AuthState.Loading
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                val databaseReference = FirebaseDatabase.getInstance().reference
                                val userMap = mapOf(
                                    "username" to username,
                                    "email" to email
                                )
                                databaseReference.child("users").child(userId).setValue(userMap)
                                    .addOnCompleteListener { dbTask ->
                                        mutableAuthState.value = if (dbTask.isSuccessful) {
                                            AuthState.Authenticated
                                        } else {
                                            AuthState.Error("Failed to save user data: ${dbTask.exception?.message}")
                                        }
                                    }
                            } else {
                                mutableAuthState.value = AuthState.Error("Failed to get user ID.")
                            }
                        } else {
                            mutableAuthState.value = handleFirebaseException(task.exception)
                        }
                    }
            }
        }
    }


    // Handle error message
    fun handlePromptMessageError() {
        if (mutableAuthState.value is AuthState.Error) {
            mutableAuthState.value = AuthState.Unauthenticated
        }
    }

    fun fetchUsernameFromDatabase(userId: String, onResult: (String?) -> Unit) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("users/$userId/username")
        databaseRef.get().addOnSuccessListener { snapshot ->
            onResult(snapshot.value as? String)
        }.addOnFailureListener {
            onResult(null)
        }
    }


    // Send password reset email
    fun handleForgotPassword(email: String, callback: (Boolean, String) -> Unit) {
        if (email.isEmpty()) {
            callback(false, "Please enter a valid email address.")
            return
        }

        if (!validateEmail(email)) {
            callback(false, "Invalid email format.")
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Reset link sent successfully!")
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Error sending reset link"
                    callback(false, errorMessage)
                }
            }
    }

    // Edit account (update username, email, or password)
    fun handleEditAccount(username: String? = null, password: String? = null, newPassword: String? = null) {
        username?.let { updateUsername(it) }
        if (password != null && newPassword != null) updatePassword(password, newPassword)
    }

    //update username
    private fun updateUsername(username: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("users/$userId")
            databaseReference.child("username").setValue(username)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mutableAuthState.value = AuthState.Authenticated
                    } else {
                        mutableAuthState.value = AuthState.Error("Failed to update username.")
                    }
                }
        } else {
            mutableAuthState.value = AuthState.Error("User not authenticated.")
        }
    }


    private fun updatePassword(currentPassword: String, newPassword: String) {
        val user = auth.currentUser  ?: run {
            mutableAuthState.value = AuthState.Error("User  not authenticated.")
            return
        }

        if (newPassword.length < 6) {
            mutableAuthState.value = AuthState.Error("New password must be at least 6 characters long.")
            return
        }

        val email = user.email ?: run {
            mutableAuthState.value = AuthState.Error("User  email not found.")
            return
        }

        val credential = EmailAuthProvider.getCredential(email, currentPassword)

        user.reauthenticate(credential)
            .addOnCompleteListener { reAuthTask ->
                if (reAuthTask.isSuccessful) {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { updateTask ->
                            mutableAuthState.value = if (updateTask.isSuccessful) {
                                AuthState.Authenticated
                            } else {
                                AuthState.Error("Failed to update password: ${updateTask.exception?.localizedMessage}")
                            }
                        }
                } else {
                    mutableAuthState.value = AuthState.Error("Re-authentication failed. Please check your current password.")
                }
            }.addOnFailureListener { e ->
                mutableAuthState.value = AuthState.Error("Error during re-authentication: ${e.localizedMessage}")
            }
    }

    fun verifyCurrentPassword(currentPassword: String, onResult: (Boolean) -> Unit) {
        val user = auth.currentUser
        user?.let {
            val credential = EmailAuthProvider.getCredential(it.email!!, currentPassword)
            it.reauthenticate(credential).addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
        } ?: onResult(false)
    }



    // Handle sign out

    fun handleSignOut() {
        auth.signOut()
        mutableAuthState.value = AuthState.Unauthenticated
    }

    // Validate email format
    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Handle Firebase exceptions
    private fun handleFirebaseException(exception: Exception?): AuthState {
        val errorMessage = when {
            exception?.message?.contains("email address is already in use") == true ->
                "Email already exists. Please use a different email."
            exception?.message?.contains("network error") == true ->
                "Network error. Please check your connection and try again."
            else -> exception?.localizedMessage ?: "An unexpected error occurred."
        }
        return AuthState.Error(errorMessage)
    }

    // AuthState class to represent different states
    open class AuthState {
        object Authenticated : AuthState()
        object Unauthenticated : AuthState()
        object Loading : AuthState()
        object Idle : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
