package com.umc.insider.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapFragment
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
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

    override fun onMapReady(naverMap: NaverMap){
        PurchaseFragment.naverMap = naverMap

        var camPos = CameraPosition(
            com.naver.maps.geometry.LatLng(34.38, 128.55),
            9.0)
        naverMap.cameraPosition = camPos
    }



}