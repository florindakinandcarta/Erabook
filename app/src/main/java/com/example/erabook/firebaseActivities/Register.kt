package com.example.erabook.firebaseActivities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.erabook.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
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
            register.setOnClickListener {
                val email = emailInput.editText?.text.toString()
                val password = passwordInput.editText?.text.toString()

                if (email.isEmpty()) {
                    Toast.makeText(
                        baseContext,
                        "Enter email please!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                if (password.isEmpty()) {
                    Toast.makeText(
                        baseContext,
                        "Enter password please!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val dialog = RegisterSuccessfulDialogFragment()
                            dialog.show(supportFragmentManager, "RegistrationSuccessDialog")
                        } else {
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
            alreadyHaveLogin.setOnClickListener {
                startActivity(Intent(baseContext, LogIn::class.java))
            }
            backRegister.setOnClickListener {
                finish()
                super.onBackPressedDispatcher
            }
        }
    }
}