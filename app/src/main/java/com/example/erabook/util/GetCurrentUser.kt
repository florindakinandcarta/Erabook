package com.example.erabook.util

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object GetCurrentUser{
    private var auth = Firebase.auth

    fun getCurrentUser(): FirebaseUser?{
        return auth.currentUser
    }
}