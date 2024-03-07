package com.example.erabook.fragments.login

import UserInfoViewModel
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
import com.example.erabook.data.firebasedb.UserDataRemote
import com.example.erabook.databinding.FragmentLogInBinding
import com.example.erabook.firebaseActivities.ForgotPasswordActivity
import com.example.erabook.util.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

private const val DESTINATION_HOME = "HOME_FRAGMENT"
private const val DESTINATION_PROFILE = "PROFILE_FRAGMENT"
private const val RC_SIGN_IN = 9001

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private val userInfoViewModel: UserInfoViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authenticationViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                findNavController().navigate(R.id.loginToProfile)
            }
        }
        authenticationViewModel.initializeGoogleSignInClient(requireContext())
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
            googleButton.setOnClickListener {
                authenticationViewModel.signOutAndSignIn(this@LoginFragment)
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
                authenticationViewModel.signInWithEmailAndPassword(emailLogIn.trim(), passwordLogIn)
                authenticationViewModel.isTaskSuccessful.observe(viewLifecycleOwner) { task ->
                    if (task == true) {
                        findNavController().navigate(R.id.loginToProfile)
                        requireContext().showToast(R.string.login_successful)
                    } else {
                        authenticationViewModel.exception.let { message ->
                            when (message.value) {
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
                startActivity(
                    Intent(
                        requireContext(),
                        ForgotPasswordActivity::class.java
                    )
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                authenticationViewModel.firebaseAuthWithGoogle(account.idToken, requireContext(),
                    {
                        UserDataRemote(
                            userEmail = account?.email,
                        ).let {
                            userInfoViewModel.addUserData(it)
                        }
                        requireContext().showToast(R.string.login_successful)
                    },
                    {
                        requireContext().showToast(R.string.auth_failed)
                    }
                )
            } catch (e: ApiException) {
                println("Error: $e")
            }
        }
    }
}