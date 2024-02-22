package com.example.erabook.firebaseActivities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.erabook.R
import com.example.erabook.databinding.ActivityForgotPasswordBinding
import com.example.erabook.util.showToast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            sendEmailButton.setOnClickListener {
                val userEmailInput = emailForgotInput.editText?.text.toString()
                Firebase.auth.sendPasswordResetEmail(userEmailInput)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showToast(R.string.email_sent, duration = Toast.LENGTH_LONG)
                            finish()
                        }else{
                            showToast(R.string.email_not_sent, duration = Toast.LENGTH_LONG)
                        }
                    }
            }
        }
    }

}