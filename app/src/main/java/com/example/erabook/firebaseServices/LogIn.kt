package com.example.erabook.firebaseServices

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.erabook.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogIn : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
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
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth


        binding.apply {
            loginButton.setOnClickListener {
                val emailLogIn = emailLoginInput.editText?.text.toString()
                val passwordLogIn = passwordLoginInput.editText?.text.toString()
                if (emailLogIn.isEmpty()) {
                    Toast.makeText(
                        baseContext,
                        "Enter email please!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (passwordLogIn.isEmpty()) {
                    Toast.makeText(
                        baseContext,
                        "Enter password please!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                auth.signInWithEmailAndPassword(emailLogIn.trim(), passwordLogIn)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                baseContext,
                                "Log in successful.",
                                Toast.LENGTH_SHORT,
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
            goToRegister.setOnClickListener {
                val registerIntent = Intent(baseContext, Register::class.java)
                startActivity(registerIntent)
            }
            backLogin.setOnClickListener {
                finish()
                super.onBackPressedDispatcher
            }
        }
    }
}