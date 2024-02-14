package com.example.erabook.firebaseActivities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.erabook.databinding.RegisterSuccessfulBinding

class RegisterSuccessfulDialogFragment : DialogFragment() {
    private lateinit var binding: RegisterSuccessfulBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterSuccessfulBinding
            .inflate(
                layoutInflater,
                container, false
            )
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            okImageButton.setOnClickListener {
                dialog?.dismiss()
            }
            goToLoginText.setOnClickListener {
                startActivity(Intent(requireContext(), LogInActivity::class.java))
            }
        }

    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext())
    }
}