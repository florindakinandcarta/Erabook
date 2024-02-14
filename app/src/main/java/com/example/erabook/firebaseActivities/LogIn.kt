package com.example.erabook.firebaseActivities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.erabook.MainActivity
import com.example.erabook.R
import com.example.erabook.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogIn : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var auth: FirebaseAuth
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
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this@LogIn, MainActivity::class.java).apply {
                                putExtra("destination", R.id.userProfileFragment)
                            })
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
                startActivity(Intent(baseContext, Register::class.java))
            }
            backLogin.setOnClickListener {
                finish()
                val intent = Intent(this@LogIn, MainActivity::class.java).apply {
                    putExtra("destination", R.id.homeFragment)
                }
                startActivity(intent)
            }
        }
    }
}