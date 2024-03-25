package com.example.erabook.fragments.userInfo

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
import com.example.erabook.data.models.UserDataRemote
import com.example.erabook.databinding.FragmentUserInfoBinding
import com.example.erabook.util.showToast
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
        userInfoViewModel.userInfo.observe(viewLifecycleOwner) { userUpdated ->
            this.updatedUser = userUpdated
        }
        userInfoViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                requireContext().showToast(error)
            }
        }
        userInfoViewModel.updateMessage.observe(viewLifecycleOwner) { updateMessage ->
            if (updateMessage != null) {
                requireContext().showToast(updateMessage)
                if (updateMessage == R.string.update_message_success){
                    findNavController().navigate(R.id.editToProfile)
                }
            }
        }

        binding.apply {
            updateBirthdayInput.setOnClickListener {
                val dateDialogFragment = DatePickerFragment { selectedDate ->
                    userInfoViewModel.updateUserBirthday(selectedDate)
                    updateBirthdayInput.text =
                        SimpleDateFormat.getDateInstance().format(selectedDate)
                }
                dateDialogFragment.show(parentFragmentManager, "DatePicker")
            }
            updateSubmitInfoButton.setOnClickListener {
                authenticationViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                    userInfoViewModel.updateUserDataByEmail(
                        updatedUser.copy(
                            userName = updateNameInput.editText?.text.toString(),
                            userUsername = updateUsernameInput.editText?.text.toString(),
                            userMobile = updateMobileInput.editText?.text.toString().toIntOrNull() ?: 0
                        ),
                        user?.email.toString()
                    )
                }
            }
            backUserInfo.setOnClickListener {
                findNavController().navigate(R.id.editToProfile)
            }
        }
    }
}