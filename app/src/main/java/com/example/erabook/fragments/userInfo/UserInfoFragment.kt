package com.example.erabook.fragments.userInfo

import UserInfoViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.erabook.databinding.FragmentUserInfoBinding

class UserInfoFragment : Fragment() {
    private lateinit var binding: FragmentUserInfoBinding
    private val userInfoViewModel: UserInfoViewModel by viewModels()
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
//        userInfoViewModel.userInfo.observe(viewLifecycleOwner) { user ->
//            println(user.userMobile)
//        }
        binding.apply {
            updateSubmitInfoButton.setOnClickListener {
                println("clicked")
                val updatedUserData =
                    userInfoViewModel.userInfo.value?.copy()
                updatedUserData?.apply {
                    userName = updateNameInput.editText?.text.toString().trim()
                    userUsername = updateUsernameInput.editText?.text.toString().trim()
                    userMobile = updateMobileInput.editText?.text.toString().toIntOrNull() ?: 0
                }
//                updateBirthdayInput.editText?.addTextChangedListener {
//                    //TODO(date picker dialog)
//                }
                updatedUserData?.let { userInfoViewModel.updateUserDataByDocumentId(it) }
            }
        }
    }
}