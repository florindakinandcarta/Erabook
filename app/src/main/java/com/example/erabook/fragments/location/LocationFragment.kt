package com.example.erabook.fragments.location

import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.erabook.R
import com.example.erabook.databinding.FragmentLocationBinding
import com.example.erabook.util.showToast
import org.osmdroid.api.IMapController
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class LocationFragment : Fragment(), MapListener, LocationListener {
    private lateinit var binding: FragmentLocationBinding
    private lateinit var mMyLocationNewOverlay: MyLocationNewOverlay
    private lateinit var controller: IMapController
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                initMap()
            } else {
                requireContext().showToast(R.string.location_denied)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            osmmap.setTileSource(TileSourceFactory.MAPNIK)
            osmmap.mapCenter
            osmmap.setMultiTouchControls(true)
            osmmap.getLocalVisibleRect(Rect())
            mMyLocationNewOverlay =
                MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), osmmap)
            controller = osmmap.controller
        }
        requestLocationPermission()
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        return false
    }

    override fun onLocationChanged(location: Location) {

    }

    private fun initMap() {
        mMyLocationNewOverlay.enableMyLocation()
        mMyLocationNewOverlay.enableFollowLocation()
        mMyLocationNewOverlay.isDrawAccuracyEnabled = true
        mMyLocationNewOverlay.runOnFirstFix {
            requireActivity().runOnUiThread {
                controller.setCenter(mMyLocationNewOverlay.myLocation)
                controller.animateTo(mMyLocationNewOverlay.myLocation)
            }
        }
        controller.setZoom(10.0)
        binding.osmmap.overlays.add(mMyLocationNewOverlay)
        binding.osmmap.addMapListener(this)
    }

    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                "android.permission.ACCESS_FINE_LOCATION"
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        "android.permission.ACCESS_COARSE_LOCATION"
                    ) == PackageManager.PERMISSION_GRANTED -> {
                initMap()
            }

            else -> {
                requestPermissionLauncher.launch(
                    arrayOf(
                        "android.permission.ACCESS_FINE_LOCATION",
                        "android.permission.ACCESS_COARSE_LOCATION"
                    )
                )
            }
        }
    }
}