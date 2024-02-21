package com.example.erabook.fragments.userInfo

import UserInfoViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.erabook.R
import com.example.erabook.data.firebasedb.UserDataRemote
import com.example.erabook.databinding.FragmentUserInfoBinding
import com.example.erabook.util.GetCurrentUser

class UserInfoFragment : Fragment() {
    private lateinit var binding: FragmentUserInfoBinding
    private val userInfoViewModel: UserInfoViewModel by viewModels()
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

        binding.apply {
            updateSubmitInfoButton.setOnClickListener {

//                updateBirthdayInput.editText?.addTextChangedListener {
//                    //TODO(date picker dialog)
//                }
                updatedUser.apply {
                    userName = updateNameInput.editText?.text.toString()
                    userUsername = updateUsernameInput.editText?.text.toString()
                    userMobile = updateMobileInput.editText?.text.toString().toIntOrNull() ?: 0
                }
                userInfoViewModel.updateUserDataByEmail(
                    updatedUser,
                    GetCurrentUser.getCurrentUser()?.email.toString()
                )

            }
            backUserInfo.setOnClickListener {
                findNavController().navigate(R.id.editToProfile)
            }
        }
    }
}