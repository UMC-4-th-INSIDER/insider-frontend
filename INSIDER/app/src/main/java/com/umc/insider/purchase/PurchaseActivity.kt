package com.umc.insider.purchase

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.requestLocationUpdates
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.MarkerIcons
import com.umc.insider.ChatRoomActivity
import com.umc.insider.R
import com.umc.insider.auth.UserManager
import com.umc.insider.databinding.ActivityPurchaseBinding
import com.umc.insider.retrofit.RetrofitInstance
import com.umc.insider.retrofit.api.ChattingInterface
import com.umc.insider.retrofit.api.GoodsInterface
import com.umc.insider.retrofit.api.UserInterface
import com.umc.insider.retrofit.api.WishListInterface
import com.umc.insider.retrofit.model.ChatRoomsPostReq
import com.umc.insider.retrofit.model.WishListPostReq
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class PurchaseActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var binding : ActivityPurchaseBinding
    private var sellerId : Long? = null
    private var goods_id : Long? = null

    companion object{
        private const val REQUEST_LOCATION_PERMISSION = 1
        lateinit var naverMap : NaverMap
    }

    private val marker = com.naver.maps.map.overlay.Marker()

    private var purchaseLat = 37.488540
    private var purchaseLng = 126.802745
    private var zip = ""

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

        goods_id = intent.getStringExtra("goods_id")!!.toLong()

        val user_id = UserManager.getUserIdx(this)!!.toLong()
        val goodsAPI = RetrofitInstance.getInstance().create(GoodsInterface::class.java)
        val userAPI = RetrofitInstance.getInstance().create(UserInterface::class.java)
        val wishListAPI = RetrofitInstance.getInstance().create(WishListInterface::class.java)

        val wishListPostReq = WishListPostReq(user_id ,goods_id!!)

        lifecycleScope.launch {

            try {
                val response = withContext(Dispatchers.IO){
                    wishListAPI.checkWishList(user_id,goods_id!!)
                }
                withContext(Dispatchers.Main) { binding.favoriteBtn.isChecked = response }
            }catch (e : Exception){

            }
        }

        val goodsGet = lifecycleScope.launch {

            try {
                val response = withContext(Dispatchers.IO){
                    goodsAPI.getGoodsById(goods_id!!)
                }
                sellerId = response.users_id.id
                withContext(Dispatchers.Main){
                    // 나중에 name
                    binding.productName.text = response.name
                    if(response.weight.isNullOrBlank()){
                        binding.productUnit.text = "(개당)"
                    }else{
                        binding.productUnit.text = "(100g당)"
                    }
                    binding.PurchaseExpirationDate.text= response.shelf_life
                    if (response.sale_price != null){
                        binding.productPrice.text = "${response.sale_price}원"
                    }else{
                        binding.productPrice.text = "${response.price}원"
                    }
                    binding.purchaseLocation.text = response.detailAddress
                    zip = response.userZipCode.toString()

                    Glide.with(binding.PurchaseImage.context)
                        .load(response.img_url)
                        .placeholder(null)
                        .into(binding.PurchaseImage)
                }
            }catch (e : Exception){

            }

        }

        binding.favoriteBtn.setOnCheckedChangeListener { _, isChecked ->

            if(isChecked){

                CoroutineScope(Dispatchers.IO).launch {

                    try {

                        val response = wishListAPI.addGoodsToWishList(wishListPostReq)

                        if(response.isSuccessful){
                            withContext(Dispatchers.Main){
                                withContext(Dispatchers.Main){
                                    Toast.makeText(this@PurchaseActivity, "찜목록에 추가했습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }catch (e : Exception){

                    }
                }

            }
            if(!isChecked){

                CoroutineScope(Dispatchers.IO).launch {

                    try {

                        val response = wishListAPI.deleteWishList(userId = user_id, goodsId = goods_id!!)

                        if (response.isSuccessful){
                            withContext(Dispatchers.Main){
                                Toast.makeText(this@PurchaseActivity, "찜목록에 삭제했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }


                    }catch (e : Exception){
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@PurchaseActivity, "찜목록에 삭제했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }

        lifecycleScope.launch{

            goodsGet.join()

            try{
                val zipResponse = withContext(Dispatchers.IO){
                    userAPI.getLatLng(zip)
                }
                purchaseLat = zipResponse.latitude
                purchaseLng = zipResponse.longitude

                // 지도 사용하기
                val fm = supportFragmentManager
                val mapFragment = fm.findFragmentById(R.id.mapView) as MapFragment?
                    ?: MapFragment.newInstance().also {
                        fm.beginTransaction().add(R.id.mapView, it).commit()
                    }

                mapFragment.getMapAsync(this@PurchaseActivity)

                val mapView = mapFragment.view
                mapView?.setOnTouchListener { v, event ->
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    false
                }

            }catch(e: Exception){
                Log.e("API", "Failed to get latitude and longitude", e)
            }
        }


        /*
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
        }*/

        // 권한 요청
        requestLocationPermission()

        initview()
    }

    private fun initview() {
        with(binding) {

            // 내위치로 이동
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

            // 구매 위치로 이동
            purchaseView.setOnClickListener {
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
                            val userLatLng = LatLng(purchaseLat,purchaseLng)
                            naverMap.moveCamera(CameraUpdate.scrollTo(userLatLng).animate(CameraAnimation.Easing))
                        }
                    }
            }

            chattingBtn.setOnClickListener {

                val chattingAPI = RetrofitInstance.getInstance().create(ChattingInterface::class.java)

                val sellerId = sellerId!!
                val buyerId = UserManager.getUserIdx(this@PurchaseActivity)!!.toLong()
                val chatRoomsPostReq = ChatRoomsPostReq(sellerId,buyerId,goods_id!!)

                CoroutineScope(Dispatchers.IO).launch{
                    try {
                        val response = chattingAPI.createChatRoom(chatRoomsPostReq)
                        if (response.isSuccessful){
                            Log.d("chat", "api 호출 성공")
                            withContext(Dispatchers.Main){
                                val intent = Intent(this@PurchaseActivity, ChatRoomActivity::class.java)
                                intent.putExtra("chatRoom_id",response.body()!!.result!!.id.toString())
                                intent.putExtra("goods_id",goods_id.toString())
                                startActivity(intent)
                            }
                        }else{
                            Log.d("chat", "api 호출 실패")
                        }
                    }catch (e : Exception){
                        Log.d("chat", "네트워크 에러")

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
        val userLatLng = LatLng(purchaseLat, purchaseLng)
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
                    Log.d("LOCATIONNN", "lat : ${location.latitude}")
                    Log.d("LOCATIONNN", "lon : ${location.longitude}")

                    // 내 위치에 마커 생성하기
                    val currentLatLng = LatLng(latitude, longitude)
                    val currentMarker = com.naver.maps.map.overlay.Marker()
                    currentMarker.position = currentLatLng
                    currentMarker.map = PurchaseActivity.naverMap
                    currentMarker.icon = MarkerIcons.BLACK
                    currentMarker.iconTintColor = Color.RED
                    currentMarker.width = 70
                    currentMarker.height = 90
                }
            }
    }

}
