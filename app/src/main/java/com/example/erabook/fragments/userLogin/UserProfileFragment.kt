package com.example.erabook.fragments.userLogin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.erabook.R
import com.example.erabook.databinding.UserProfileBinding
import com.example.erabook.firebaseActivities.LogInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.File
import java.util.Date

class UserProfileFragment : Fragment() {
    private lateinit var binding: UserProfileBinding
    private lateinit var auth: FirebaseAuth
    private var photoName: String? = null
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
        setOnCLickListener()
        if (currentUser == null) {
            startActivity(Intent(requireContext(), LogInActivity::class.java))
        } else {
            binding.apply {
                profileName.text = currentUser.displayName
                emailProfile.text = currentUser.email
                mobileNumber.text = currentUser.phoneNumber
            }
        }
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
                photoName = "IMG_${Date()}.JPG"
                val photoFile = File(requireContext().applicationContext.filesDir, photoName)
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    " com.example.erabook.fileprovider",
                    photoFile
                )
                takePhoto.launch(photoUri)
            }

        }
    }

    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { didTakePhoto: Boolean ->
        if (didTakePhoto && photoName != null) {
//            TODO("take photo or upload")
        }
    }
}
