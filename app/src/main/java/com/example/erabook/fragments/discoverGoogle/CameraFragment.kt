package com.example.erabook.fragments.discoverGoogle

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.erabook.R
import com.example.erabook.databinding.FragmentCameraBinding
import com.example.erabook.util.REQUESTED_PERMISSIONS
import com.example.erabook.util.convertImageProxyToBitmap
import com.example.erabook.util.showToast
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraFragment : Fragment() {
    private lateinit var binding: FragmentCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private val sharedViewModel: SharedGoogleBooksViewModel by viewModels({ requireActivity() })
    private lateinit var barcodeScanner: BarcodeScanner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestCameraPermissions()
        }
        binding = FragmentCameraBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_QR_CODE
            )
            .build()
        barcodeScanner = BarcodeScanning.getClient(options)
        cameraExecutor = Executors.newSingleThreadExecutor()
        binding.apply {
            imageCaptureButton.setOnClickListener {
                takePhoto()
                requireContext().showToast(R.string.wait)
            }
        }
    }

    private fun allPermissionsGranted() = REQUESTED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder()
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture
                )
            } catch (e: Exception) {
                Log.d("Binding error", "Use case binding failed: $e")
            }
        }, ContextCompat.getMainExecutor(requireContext()))

    }

    private fun requestCameraPermissions() {
        activityResultLauncher.launch(REQUESTED_PERMISSIONS)
    }

    private fun takePhoto() {
        imageCapture?.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val bitmap = image.convertImageProxyToBitmap()
                    image.close()
                    processImage(bitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    requireContext().showToast(R.string.default_error)
                    Log.d("Error capture", "Photo capture failed: ${exception.message}")
                }
            }
        )
    }

    private fun processImage(bitMap: Bitmap?) {
        bitMap?.let { image ->
            val inputImage = InputImage.fromBitmap(image, 0)
            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        val barcodeValue = "=isbn:${barcode.rawValue}"
                        sharedViewModel.fetchBooksWithISBN(barcodeValue)
                        parentFragmentManager.popBackStack()
                    }
                }
                .addOnFailureListener { e ->
                    requireContext().showToast(R.string.scanning_failed)
                    Log.d("Barcode fail.", "Barcode scanning failed: ${e.message}")
                }
        }
    }

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var permissionGranted = true
        permissions.entries.forEach {
            if (it.key in REQUESTED_PERMISSIONS && !it.value) {
                permissionGranted = false
            }
            if (!permissionGranted) {
                requireContext().showToast(R.string.camera_denied)
            } else {
                startCamera()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}