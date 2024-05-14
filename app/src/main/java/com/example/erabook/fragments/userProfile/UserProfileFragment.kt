package com.example.erabook.fragments.userProfile

import UserInfoViewModel
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.erabook.R
import com.example.erabook.activities.AuthenticationViewModel
import com.example.erabook.databinding.UserProfileBinding
import com.example.erabook.fragments.location.LocationViewModel
import com.example.erabook.util.REQUESTED_PERMISSIONS
import com.example.erabook.util.loadCircularImageWithGlide
import com.example.erabook.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class UserProfileFragment : Fragment() {
  private lateinit var binding: UserProfileBinding
  private val userInfoViewModel: UserInfoViewModel by viewModels()
  private val authenticationViewModel: AuthenticationViewModel by viewModels()
  private val locationViewModel: LocationViewModel by viewModels()
  private var photoName: String? = null
  private var photoFile: File? = null
  private var photoUri: Uri? = null

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {
    binding = UserProfileBinding.inflate(layoutInflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    authenticationViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
      photoName = user?.email
      photoName?.let { name ->
        photoFile = File(requireContext().applicationContext.filesDir, name)
      }
      photoFile?.let { file ->
        photoUri =
            FileProvider.getUriForFile(requireContext(), "com.example.erabook.fileprovider", file)
      }
    }
    getLocationName()
    setOnCLickListener()
    updateUI()
  }

  private fun areAllPermissionsGranted() =
      REQUESTED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
      }

  private fun setOnCLickListener() {
    binding.apply {
      logOut.setOnClickListener {
        authenticationViewModel.logout()
        findNavController().navigate(R.id.userProfileToLogin)
      }
      editProfile.setOnClickListener { findNavController().navigate(R.id.userProfileToEdit) }
      photoCamera.setOnClickListener {
        if (areAllPermissionsGranted()) {
          takePhoto.launch(photoUri)
        } else {
          requestCameraPermissions()
        }
      }
      locationButton.setOnClickListener { findNavController().navigate(R.id.userProfileToLocation) }
    }
  }

  private fun requestCameraPermissions() {
    activityResultLauncher.launch(REQUESTED_PERMISSIONS)
  }

  private val activityResultLauncher =
      registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions
        ->
        var permissionGranted = true
        permissions.entries.forEach {
          if (it.key in REQUESTED_PERMISSIONS && !it.value) {
            permissionGranted = false
          }
          if (!permissionGranted) {
            requireContext().showToast(R.string.camera_denied)
          } else {
            takePhoto.launch(photoUri)
          }
        }
      }

  private fun updateUI() {
    userInfoViewModel.userInfo.observe(viewLifecycleOwner) { userData ->
      binding.apply {
        profileName.text = userData.userName
        emailProfile.text = userData.userEmail
        mobileNumber.text = userData.userMobile.toString()
        profileImg.loadCircularImageWithGlide(userData.userProfileImg)
      }
    }
    userInfoViewModel.updatePicture.observe(viewLifecycleOwner) { updatePicture ->
      if (updatePicture != true) {
        requireContext().showToast(R.string.update_message_error)
      } else {
        binding.loader.visibility = View.GONE
        requireContext().showToast(R.string.profile_successful)
      }
    }
  }
  private val takePhoto =
      registerForActivityResult(ActivityResultContracts.TakePicture()) { didTakePhoto ->
        if (didTakePhoto && photoName != null) {
          photoName?.let { name ->
            if (photoFile?.exists() == true) {
              photoFile?.let { file ->
                userInfoViewModel.compressImage(requireContext(), file, name)
                binding.loader.visibility = View.VISIBLE
              }
            }
          }
        }
      }
  private fun getLocationName() {
    userInfoViewModel.userInfo.observe(viewLifecycleOwner) { userData ->
      if (userData.userLocation != null && userData.userLocation.size > 2) {
        val latitude = userData.userLocation[0].toDouble()
        val longitude = userData.userLocation[1].toDouble()
        locationViewModel.getLocationPlace(latitude, longitude)
      }
      locationViewModel.osm.observe(viewLifecycleOwner) { osm ->
        binding.locationCity.text = osm?.address?.let { "${it.municipality}, ${it.country}" }
      }
    }
  }
}
