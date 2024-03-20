package com.example.erabook.fragments.login

import UserInfoViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.erabook.R
import com.example.erabook.data.models.UserDataRemote
import com.example.erabook.databinding.FragmentRegisterBinding
import com.example.erabook.util.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val userInfoViewModel: UserInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    requireContext().showToast(R.string.enter_email)
                    return@setOnClickListener
                }
                if (password.isEmpty()) {
                    requireContext().showToast(R.string.enter_password)
                    return@setOnClickListener
                }
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            UserDataRemote(
                                userEmail = email,
                            ).let {
                                userInfoViewModel.addUserData(it)
                            }
                            emailInput.editText?.setText("")
                            passwordInput.editText?.setText("")
                            usernameInput.editText?.setText("")
                            confirmPasswordInput.editText?.setText("")
                            requireActivity().showToast(R.string.signup_succ)
                        } else if (password.length < 6) {
                            requireContext().showToast(R.string.password_to_short)
                        } else {
                            requireContext().showToast(R.string.auth_failed)
                        }
                    }
            }
            alreadyHaveLoginTextview.setOnClickListener {
                findNavController().navigate(R.id.registerToLogin)
            }
            backRegister.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}