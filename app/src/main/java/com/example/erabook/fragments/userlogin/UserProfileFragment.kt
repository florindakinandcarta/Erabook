package com.example.erabook.fragments.userlogin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.erabook.R
import com.example.erabook.databinding.UserProfileBinding
import com.example.erabook.firebaseActivities.LogInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserProfileFragment : Fragment() {
    private lateinit var binding: UserProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

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
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(requireContext(), LogInActivity::class.java))
        } else {
            binding.apply {
                profileName.text = currentUser.displayName
                emailProfile.text = currentUser.email
                mobileNumber.text = currentUser.phoneNumber
                println(currentUser.displayName)
                logOut.setOnClickListener {
                    Firebase.auth.signOut()
                    startActivity(Intent(requireContext(), LogInActivity::class.java))
                }
                backProfile.setOnClickListener {
                    findNavController().navigate(R.id.profileToHome)
                }
            }
        }
    }
}