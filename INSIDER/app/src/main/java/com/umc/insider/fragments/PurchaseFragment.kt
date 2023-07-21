package com.umc.insider.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapFragment
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.MarkerIcons
import com.umc.insider.R
import com.umc.insider.databinding.FragmentPurchaseBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PurchaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PurchaseFragment : Fragment(), OnMapReadyCallback {

    companion object{
        lateinit var naverMap : NaverMap
    }

    private val marker = com.naver.maps.map.overlay.Marker()

    private var _binding : FragmentPurchaseBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPurchaseBinding.inflate(inflater, container, false)

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.mapView) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.mapView, it).commit()
            }

        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(naverMap: NaverMap){
        PurchaseFragment.naverMap = naverMap

        var camPos = CameraPosition(
            com.naver.maps.geometry.LatLng(37.485540, 126.802745),
            15.0)
        naverMap.cameraPosition = camPos

        marker.position = com.naver.maps.geometry.LatLng(37.485540, 126.802745)
        marker.map = Companion.naverMap
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = Color.RED
        marker.width = 70
        marker.height = 90

    }



}