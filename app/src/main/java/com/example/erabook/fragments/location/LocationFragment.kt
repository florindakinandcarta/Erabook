package com.example.erabook.fragments.location

import UserInfoViewModel
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
import androidx.fragment.app.viewModels
import com.example.erabook.R
import com.example.erabook.data.models.UserDataRemote
import com.example.erabook.databinding.FragmentLocationBinding
import com.example.erabook.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.api.IMapController
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@AndroidEntryPoint
class LocationFragment : Fragment(), MapListener, LocationListener {
    private lateinit var binding: FragmentLocationBinding
    private lateinit var mMyLocationNewOverlay: MyLocationNewOverlay
    private lateinit var controller: IMapController
    private lateinit var updatedUser: UserDataRemote
    private val userInfoViewModel: UserInfoViewModel by viewModels()
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
        userInfoViewModel.userInfo.observe(viewLifecycleOwner) { userUpdated ->
            this.updatedUser = userUpdated
            if (::updatedUser.isInitialized) {
                initMap()
            }
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
//    private fun getLocation(){
//        binding.osmmap.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
//            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
//                p?.let {
//                    locationViewModel.getLocationPlace(p.latitude, p.longitude)
//                    requireContext().showToast(R.string.wait)
//                }
//                return true
//            }
//
//            override fun longPressHelper(p: GeoPoint?): Boolean {
//                return false
//            }
//        }))
//    }

//        private fun addItemOnTheMap(title: String?,location: String?,latitude: Double, longitude: Double){
//            val items = ArrayList<OverlayItem>()
//            val singlePoint = OverlayItem(title,location,GeoPoint(latitude,longitude))
//            singlePoint.setMarker(
//            getDrawable(requireContext(),R.drawable.location)
//            )
//            items.add(singlePoint)
//                var overlay = ItemizedOverlayWithFocus(
//                    items,
//                    object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
//                        override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
//                            return true
//                        }
//
//                        override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
//                            return false
//                        }
//                    },
//                    context
//                )
//                overlay.setFocusItemsOnTap(true)
//                binding.osmmap.overlays.add(overlay)
//        }

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
        userInfoViewModel.userInfo.observe(viewLifecycleOwner) { user ->
            userInfoViewModel.updateUserDataByEmail(
                updatedUser.copy(
                    userLocation = "${mMyLocationNewOverlay.myLocation}"
                ),
                user.userEmail.toString()
            )
        }
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