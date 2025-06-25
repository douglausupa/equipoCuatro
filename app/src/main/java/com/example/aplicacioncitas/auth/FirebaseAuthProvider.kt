package com.example.aplicacioncitas.auth

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseAuthProvider @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : IAuthProvider {
    override val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid
}