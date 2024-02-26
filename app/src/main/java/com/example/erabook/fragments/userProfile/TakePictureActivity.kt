package com.example.erabook.fragments.userProfile


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.core.content.ContextCompat
import com.example.erabook.R
import com.example.erabook.util.showToast
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TakePictureActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutorService: ExecutorService

    companion object {
        private const val TAG = "ERABOOK"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUESTED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }
        cameraExecutorService = Executors.newSingleThreadExecutor()

    }

    private fun allPermissionsGranted() = REQUESTED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun startCamera() {}

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUESTED_PERMISSIONS)
    }
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutorService.shutdown()
    }

    @SuppressLint("SuspiciousIndentation")
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUESTED_PERMISSIONS && !it.value){
                    permissionGranted = false
                }
                if (!permissionGranted){
                    showToast(R.string.error_permission_denied)
                }else{
                    startCamera()
                }
            }

        }
}