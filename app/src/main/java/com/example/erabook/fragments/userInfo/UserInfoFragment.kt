package com.example.erabook.fragments.userInfo

import UserInfoViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.erabook.R
import com.example.erabook.activities.AuthenticationViewModel
import com.example.erabook.data.models.UserDataRemote
import com.example.erabook.databinding.FragmentUserInfoBinding
import com.example.erabook.util.showToast
import com.rommansabbir.networkx.extension.isInternetConnectedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class UserInfoFragment : Fragment() {
    private lateinit var binding: FragmentUserInfoBinding
    private val userInfoViewModel: UserInfoViewModel by viewModels()
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private lateinit var updatedUser: UserDataRemote
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            isInternetConnectedFlow.collectLatest {
                when (it) {
                    true -> {
                    }

                    else -> {
                        requireContext().showToast(R.string.update_connection)
                    }
                }
            }
        }
        setupOnClickListeners()
        loadData()
    }

    private fun setupOnClickListeners() {
        binding.apply {
            updateBirthdayInput.setOnClickListener {
                val dateDialogFragment = DatePickerFragment { selectedDate ->
                    userInfoViewModel.updateUserBirthday(selectedDate)
                    updateBirthdayInput.text =
                        SimpleDateFormat.getDateInstance().format(selectedDate)
                }
                dateDialogFragment.show(parentFragmentManager, "DatePicker")
            }
            val name = updateNameInput.editText?.text.toString()
            val username = updateUsernameInput.editText?.text.toString()
            val mobile = updateMobileInput.editText?.text.toString().toIntOrNull()
            updateSubmitInfoButton.setOnClickListener {
                if (name.isNotEmpty() && username.isNotEmpty() && mobile != null) {
                    authenticationViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                        userInfoViewModel.updateUserDataByEmail(
                            updatedUser.copy(
                                userName = name,
                                userUsername = username,
                                userMobile = mobile
                            ),
                            user?.email.toString()
                        )
                    }
                }
                else{
                    requireContext().showToast(R.string.required_fields)
                }
            }
            backUserInfo.setOnClickListener {
                findNavController().navigate(R.id.editToProfile)
            }
        }
    }

    private fun loadData() {
        userInfoViewModel.userInfo.observe(viewLifecycleOwner) { userUpdated ->
            this.updatedUser = userUpdated
        }
        userInfoViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                requireContext().showToast(error)
            }
        }
        userInfoViewModel.updateMessage.observe(viewLifecycleOwner) { updateMessage ->
            if (updateMessage != true ) {
                requireContext().showToast(R.string.update_message_error)
            }else{
                    requireContext().showToast(R.string.update_message_success)
                    findNavController().navigate(R.id.editToProfile)
                }
            }
        userInfoViewModel.updatePicture.observe(viewLifecycleOwner){updatePicture ->
            if (updatePicture != true){
                requireContext().showToast(R.string.update_message_error)
            }else{
                requireContext().showToast(R.string.profile_successful)
            }

        }

    }
}