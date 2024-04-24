package com.example.erabook.fragments.focustimer

import android.app.NotificationManager
import android.content.Context
import android.os.CountDownTimer
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erabook.R
import kotlinx.coroutines.launch
import sendNotification

class FocusViewModel : ViewModel() {
    private var countdownTimer: CountDownTimer? = null
    private val _remainingTimeInMillis = MutableLiveData<Long>()
    val remainingTimeInMillis: LiveData<Long> = _remainingTimeInMillis


    fun startCountDownTimer(
        totalTimeInMillis: Long,
        applicationContext: Context
    ) {
        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.cancelAll()
        viewModelScope.launch {
            countdownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _remainingTimeInMillis.postValue(millisUntilFinished)
                }

                override fun onFinish() {
                    _remainingTimeInMillis.postValue(0)
                    notificationManager.sendNotification(
                        applicationContext.getString(R.string.notification_time_is_up),
                        applicationContext
                    )
                }
            }
            countdownTimer?.start()
        }
    }


    fun stopCountDownTimer() {
        countdownTimer?.cancel()
        _remainingTimeInMillis.postValue(0)
    }
    fun pauseCountDownTimer(){
        countdownTimer?.cancel()
    }
    fun setIsTimerRunning(isRunning: Boolean): Boolean {
        return isRunning
    }

}