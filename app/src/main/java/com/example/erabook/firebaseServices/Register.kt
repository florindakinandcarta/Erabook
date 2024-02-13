package com.example.erabook.firebaseServices

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.erabook.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

//    public override fun onStart() {
//        super.onStart()
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            TODO("open the fragment that shows the user profile")
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth



        binding.apply {
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
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                baseContext,
                                "Account created.",
                                Toast.LENGTH_SHORT
                            ).show()
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
                val loginIntent = Intent(baseContext, LogIn::class.java)
                startActivity(loginIntent)
            }
            backRegister.setOnClickListener {
                finish()
                super.onBackPressedDispatcher
            }
        }
    }
}