package com.example.erabook.fragments.focustimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cancelNotifications
import com.example.erabook.R
import com.example.erabook.adapters.FocusMinutesAdapter
import com.example.erabook.databinding.FragmentFocusTimeBinding

class FocusTimeFragment : Fragment() {
    private lateinit var binding: FragmentFocusTimeBinding
    private lateinit var spinnerAdapter: FocusMinutesAdapter
    private val focusViewModel: FocusViewModel by viewModels()
    private var selectedTime: Long = 0
    private var remainingTimeWhenPaused: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFocusTimeBinding.inflate(
            layoutInflater,
            container, false
        )
        createChannel(
            getString(R.string.erabook_notification_channel_id),
            getString(R.string.erabook_notification_channel_name)
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerAdapter = FocusMinutesAdapter(requireContext())


        val notificationManager = ContextCompat.getSystemService(
            requireContext(),
            NotificationManager::class.java
        ) as NotificationManager

        focusViewModel.remainingTimeInMillis.observe(
            viewLifecycleOwner
        ) { remainingTime ->
            binding.countDownTimer.text =
                String.format("%02d:%02d", (remainingTime / 1000 / 60), (remainingTime / 1000 % 60))
        }
        binding.apply {
            spinnerPickTime.adapter = spinnerAdapter
            val isRunning = focusViewModel.setIsTimerRunning(true)

            giveUp.setOnClickListener {
                if (isRunning) {
                    focusViewModel.setIsTimerRunning(false)
                    focusViewModel.stopCountDownTimer()
                    binding.start.setText(R.string.continue_text)
                    remainingTimeWhenPaused = focusViewModel.remainingTimeInMillis.value ?: 0
                    Toast.makeText(
                        context,
                        getString(R.string.hold_button),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            giveUp.setOnLongClickListener {
                focusViewModel.setIsTimerRunning(false)
                focusViewModel.stopCountDownTimer()
                binding.countDownTimer.setText(R.string.timer_zero)
                spinnerPickTime.setSelection(0)
                binding.start.setText(R.string.start_button)
                remainingTimeWhenPaused = 0
                true
            }
            start.setOnClickListener {
                notificationManager.cancelNotifications()
                focusViewModel.setIsTimerRunning(true)
                focusViewModel.stopCountDownTimer()

                if (remainingTimeWhenPaused > 0) {
                    focusViewModel.startCountDownTimer(remainingTimeWhenPaused, requireContext())
                } else if (selectedTime == 0.toLong()) {
                    Toast.makeText(
                        context,
                        getString(R.string.pick_time),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    focusViewModel.startCountDownTimer((selectedTime * 60 * 1000), requireContext())
                }
            }
            spinnerPickTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedTime = spinnerAdapter.getItem(position)?.minutes?.toLong() ?: 0
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.MAGENTA
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_time_is_up)

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}
