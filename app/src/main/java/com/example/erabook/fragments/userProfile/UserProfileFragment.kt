package com.example.erabook.fragments.userProfile

import UserInfoViewModel
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.erabook.R
import com.example.erabook.databinding.UserProfileBinding
import com.example.erabook.firebaseActivities.LogInActivity
import com.example.erabook.util.GetCurrentUser
import com.example.erabook.util.showToast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserProfileFragment : Fragment() {
    private lateinit var binding: UserProfileBinding
    private val userInfoViewModel: UserInfoViewModel by viewModels()
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
                Firebase.auth.signOut()
                startActivity(Intent(requireContext(), LogInActivity::class.java))
            }
            backProfile.setOnClickListener {
                findNavController().navigate(R.id.profileToHome)
            }
            editProfile.setOnClickListener {
                findNavController().navigate(R.id.userProfileToEdit)
            }
            profileImg.setOnClickListener {
            }

        }
    }

    private fun userStatus() {
        if (GetCurrentUser.getCurrentUser() == null) {
            startActivity(Intent(requireContext(), LogInActivity::class.java))
        } else {
            userInfoViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                if (error != null) {
                    requireContext().showToast(error)
                }
            }
            userInfoViewModel.userInfo.observe(viewLifecycleOwner) { userData ->
                binding.apply {
                    profileName.text = userData.userName
                    emailProfile.text = userData.userEmail
                    mobileNumber.text = userData.userMobile.toString()
                }
            }
        }
    }

}
