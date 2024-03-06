package com.example.erabook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthenticationViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    val userLiveData: LiveData<FirebaseUser?> = MutableLiveData()

    init {
        firebaseAuth.addAuthStateListener { firebaseAuth ->
            (userLiveData as MutableLiveData).value = firebaseAuth.currentUser

        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}