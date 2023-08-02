package com.umc.insider.purchase

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.MarkerIcons
import com.umc.insider.R
import com.umc.insider.databinding.ActivityPurchaseBinding

class PurchaseActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding : ActivityPurchaseBinding

    companion object{
        lateinit var naverMap : NaverMap
    }

    private val marker = com.naver.maps.map.overlay.Marker()

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

        initview()
    }

    private fun initview(){
        with(binding){

        }
    }

    // 지도 설정하기
    override fun onMapReady(naverMap: NaverMap){
        PurchaseActivity.naverMap = naverMap

        var camPos = CameraPosition(
            LatLng(37.485540, 126.802745),
            15.0)
        naverMap.cameraPosition = camPos

        marker.position = com.naver.maps.geometry.LatLng(37.485540, 126.802745)
        marker.map = PurchaseActivity.naverMap
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = Color.RED
        marker.width = 70
        marker.height = 90
    }

}