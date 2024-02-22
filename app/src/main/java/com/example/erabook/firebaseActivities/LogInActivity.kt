package com.example.erabook.firebaseActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.erabook.MainActivity
import com.example.erabook.R
import com.example.erabook.databinding.ActivityLogInBinding
import com.example.erabook.util.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val DESTINATION_HOME = "HOME_FRAGMENT"
private const val DESTINATION_PROFILE = "PROFILE_FRAGMENT"

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        binding.apply {
            goToRegister.setOnClickListener {
                startActivity(Intent(this@LogInActivity, RegisterActivity::class.java))
            }
            backLogin.setOnClickListener {
                finish()
                val intent = Intent(this@LogInActivity, MainActivity::class.java).apply {
                    putExtra(DESTINATION_HOME, R.id.homeFragment)
                }
                startActivity(intent)
            }
            loginButton.setOnClickListener {
                val emailLogIn = emailLoginInput.editText?.text.toString()
                val passwordLogIn = passwordLoginInput.editText?.text.toString()
                if (emailLogIn.isEmpty()) {
                    showToast(R.string.enter_email)
                }
                if (passwordLogIn.isEmpty()) {
                    showToast(R.string.enter_password)
                }
                auth.signInWithEmailAndPassword(emailLogIn.trim(), passwordLogIn)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(
                                Intent(
                                    this@LogInActivity,
                                    MainActivity::class.java
                                ).apply {
                                    putExtra(DESTINATION_PROFILE, R.id.userProfileFragment)
                                })
                            showToast(R.string.login_successful)
                            finish()
                        } else {
                            val exception = task.exception as FirebaseException
                            exception.let {
                                when (it.message) {
                                    getString(R.string.error_code_message_incorrect) -> showToast(R.string.wrong_password_email)
                                    getString(R.string.error_code_message_connection) -> showToast(R.string.error_connection)
                                }
                            }
                        }
                    }
            }
            forgotPasswordTextView.setOnClickListener {
                startActivity(
                    Intent(
                        this@LogInActivity,
                        ForgotPasswordActivity::class.java
                    )
                )
                finish()
            }
        }
    }
}