package com.example.erabook.fragments.userProfile

import UserInfoViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.erabook.activities.AuthenticationViewModel
import com.example.erabook.R
import com.example.erabook.databinding.UserProfileBinding

class UserProfileFragment : Fragment() {
    private lateinit var binding: UserProfileBinding
    private val userInfoViewModel: UserInfoViewModel by viewModels()
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserProfileBinding.inflate(
            layoutInflater,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnCLickListener()
        userStatus()
    }

    private fun setOnCLickListener() {
        binding.apply {
            logOut.setOnClickListener {
                authenticationViewModel.logout()
                findNavController().navigate(R.id.userProfileToLogin)
            }
            editProfile.setOnClickListener {
                findNavController().navigate(R.id.userProfileToEdit)
            }
            profileImg.setOnClickListener {
            }

        }
    }

    private fun userStatus() {
        userInfoViewModel.userInfo.observe(viewLifecycleOwner) { userData ->
            binding.apply {
                profileName.text = userData.userName
                emailProfile.text = userData.userEmail
                mobileNumber.text = userData.userMobile.toString()
            }
        }
    }
}

