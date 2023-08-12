package com.umc.insider.purchase

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.requestLocationUpdates
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.MarkerIcons
import com.umc.insider.R
import com.umc.insider.databinding.ActivityPurchaseBinding

class PurchaseActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var binding : ActivityPurchaseBinding

    companion object{
        private const val REQUEST_LOCATION_PERMISSION = 1
        lateinit var naverMap : NaverMap
    }
    private val marker = com.naver.maps.map.overlay.Marker()

    // 권한 요청을 한다.
    private fun requestLocationPermission() {
        // ACCESS_FINE_LOCATION 은 GPS 위치 권한 / ACCESS_COARSE_LOCATION 은 네트워크 위치 권한입니다.
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_purchase)

        val productName = intent.getStringExtra("productName")
        val productWeight = intent.getStringExtra("productWeight")
        val productPrice = intent.getStringExtra("productPrice")

        binding.productName.text = productName
        binding.productWeight.text = productWeight
        binding.productPrice.text = productPrice

        // 지도 사용하기
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.mapView) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.mapView, it).commit()
            }

        mapFragment.getMapAsync(this)

        val mapView = mapFragment.view
        mapView?.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }

        // 권한 요청
        requestLocationPermission()

        initview()
    }

    private fun initview() {
        with(binding) {

            mylocationView.setOnClickListener {
                if (ActivityCompat.checkSelfPermission(
                        this@PurchaseActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@PurchaseActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@setOnClickListener
                }
                val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@PurchaseActivity)
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            val latitude = location.latitude
                            val longitude = location.longitude

                            // 내 위치로 지도 이동하기
                            val currentLatLng = LatLng(latitude, longitude)
                            naverMap.moveCamera(CameraUpdate.scrollTo(currentLatLng).animate(CameraAnimation.Easing))
                        }
                    }
            }
        }
    }

    // location 권한 결과
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                // 권한이 수락됨
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // permission granted! You may not need to call here for location update
                } else {

                }
                return
            }
        }
    }

    // 지도 설정하기
    override fun onMapReady(naverMap: NaverMap) {
        PurchaseActivity.naverMap = naverMap
        naverMap.uiSettings.isLocationButtonEnabled = true

        // 사용자가 입력한 위도와 경도에 마커를 표시하고 지도를 이동하기
        val userLatLng = LatLng(37.485540, 126.802745)
        val userMarker = com.naver.maps.map.overlay.Marker()
        userMarker.position = userLatLng
        userMarker.map = naverMap
        userMarker.icon = MarkerIcons.BLACK
        userMarker.iconTintColor = Color.BLUE
        userMarker.width = 70
        userMarker.height = 90
        naverMap.moveCamera(CameraUpdate.scrollTo(userLatLng).animate(CameraAnimation.Easing))

        // FusedLocationApi 사용
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // 권한이 수락됨
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    // 내 위치에 마커 생성하기
                    val currentLatLng = LatLng(latitude, longitude)
                    marker.position = currentLatLng
                    marker.map = PurchaseActivity.naverMap
                    marker.icon = MarkerIcons.BLACK
                    marker.iconTintColor = Color.RED
                    marker.width = 70
                    marker.height = 90
                }
            }
    }

}