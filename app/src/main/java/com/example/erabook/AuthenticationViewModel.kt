package com.example.erabook

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

private const val RC_SIGN_IN = 9001

class AuthenticationViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _userLiveData = MutableLiveData<FirebaseUser?>()
    val userLiveData: LiveData<FirebaseUser?> = _userLiveData
    private val _isTaskSuccessful = MutableLiveData<Boolean>()
    val isTaskSuccessful: LiveData<Boolean?> = _isTaskSuccessful
    private val _exception = MutableLiveData<String>()
    val exception: LiveData<String> = _exception
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    init {
        firebaseAuth.addAuthStateListener { firebaseAuth ->
            _userLiveData.value = firebaseAuth.currentUser
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isTaskSuccessful.value = true
                } else {
                    _isTaskSuccessful.value = false
                    _exception.value = task.exception?.message
                }
            }
    }

    fun googleSignInOptions(context: Context) {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
    }

    fun signOut(fragment: Fragment) {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener {
                val signInIntent = mGoogleSignInClient.signInIntent
                fragment.startActivityForResult(
                    signInIntent,
                    RC_SIGN_IN
                )
            }
    }

    fun firebaseAuthWithGoogle(
        idToken: String?,
        successCallback: () -> Unit,
        errorCallback: () -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    successCallback()
                } else {
                    errorCallback()
                }

            }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}