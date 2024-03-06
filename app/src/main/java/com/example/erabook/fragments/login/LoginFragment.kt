package com.example.erabook.fragments.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.erabook.AuthenticationViewModel
import com.example.erabook.MainActivity
import com.example.erabook.R
import com.example.erabook.databinding.FragmentLogInBinding
import com.example.erabook.firebaseActivities.ForgotPasswordActivity
import com.example.erabook.util.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val DESTINATION_HOME = "HOME_FRAGMENT"
private const val DESTINATION_PROFILE = "PROFILE_FRAGMENT"

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private lateinit var auth: FirebaseAuth
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(layoutInflater, container, false)
        auth = Firebase.auth
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListeners()
    }


    private fun setupOnClickListeners() {
        binding.apply {
            goToRegister.setOnClickListener {
                findNavController().navigate(R.id.loginToRegister)
            }
            backLogin.setOnClickListener {
                val intent = Intent(requireContext(), MainActivity::class.java).apply {
                    putExtra(DESTINATION_HOME, R.id.homeFragment)
                }
                startActivity(intent)
            }
            loginButton.setOnClickListener {
                val emailLogIn = emailLoginInput.editText?.text.toString()
                val passwordLogIn = passwordLoginInput.editText?.text.toString()
                if (emailLogIn.isEmpty()) {
                    requireContext().showToast(R.string.enter_email)
                }
                if (passwordLogIn.isEmpty()) {
                    requireContext().showToast(R.string.enter_password)
                }
                auth.signInWithEmailAndPassword(emailLogIn.trim(), passwordLogIn)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(
                                Intent(
                                    requireContext(),
                                    MainActivity::class.java
                                ).apply {
                                    putExtra(DESTINATION_PROFILE, R.id.userProfileFragment)
                                })
                            requireContext().showToast(R.string.login_successful)
                        } else {
                            val exception = task.exception as FirebaseException
                            exception.let {
                                when (it.message) {
                                    getString(R.string.error_code_message_incorrect) -> requireContext().showToast(
                                        R.string.wrong_password_email
                                    )

                                    getString(R.string.error_code_message_connection) -> requireContext().showToast(
                                        R.string.error_connection
                                    )
                                }
                            }
                        }
                    }
            }
            forgotPasswordTextView.setOnClickListener {
                authenticationViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                    if (user != null) {
                        authenticationViewModel.logout()
                    } else {
                        startActivity(
                            Intent(
                                requireContext(),
                                ForgotPasswordActivity::class.java
                            )
                        )
                    }
                }
            }
        }
    }
}