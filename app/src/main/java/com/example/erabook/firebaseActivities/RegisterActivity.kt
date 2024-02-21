package com.example.erabook.firebaseActivities

import UserInfoViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.erabook.R
import com.example.erabook.data.firebasedb.UserDataRemote
import com.example.erabook.databinding.ActivityRegisterBinding
import com.example.erabook.util.showToast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val userInfoViewModel: UserInfoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.apply {
            emailInput.editText?.addTextChangedListener { editable ->
                if (editable.toString().trim()
                        .isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(
                        editable.toString().trim()
                    )
                        .matches()
                ) {
                    emailMessage.visibility = View.VISIBLE
                } else {
                    emailMessage.visibility = View.GONE
                }
            }
            confirmPasswordInput.editText?.addTextChangedListener { passwordEditable ->
                if (passwordInput.editText?.text.toString().trim() != passwordEditable.toString()
                        .trim()
                ) {
                    passwordMessage.visibility = View.VISIBLE
                } else {
                    passwordMessage.visibility = View.GONE
                }
            }
            setupOnClickListeners()

        }
    }
    private fun setupOnClickListeners() {
        binding.apply {
            registerButton.setOnClickListener {
                val email = emailInput.editText?.text.toString()
                val password = passwordInput.editText?.text.toString()

                if (email.isEmpty()) {
                    showToast(R.string.enter_email)
                    return@setOnClickListener
                }
                if (password.isEmpty()) {
                    showToast(R.string.enter_password)
                    return@setOnClickListener
                }
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val addDocumentForUser = UserDataRemote(
                                userUid = "",
                                userName = "",
                                userEmail = email,
                                userMobile = 0,
                                userBirthday = Timestamp.now(),
                                userProfileImg = "",
                                userUsername = "",
                            )
                            addDocumentForUser.let {
                                userInfoViewModel.addUserData(it)
                            }
                            val dialog = RegisterSuccessfulDialogFragment()
                            dialog.show(supportFragmentManager, "RegistrationSuccessDialog")
                        } else if (password.length < 6) {
                            showToast(R.string.password_to_short)
                        } else {
                            showToast(R.string.auth_failed)
                        }
                    }
            }
            alreadyHaveLoginTextview.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LogInActivity::class.java))
                finish()
            }
            backRegister.setOnClickListener {
                super.onBackPressedDispatcher
                finish()
            }
        }
    }
}