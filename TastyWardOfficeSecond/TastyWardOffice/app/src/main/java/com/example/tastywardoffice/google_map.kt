package com.example.tastywardoffice

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tastywardoffice.databinding.FragmentGoogleMapBinding
import com.example.tastywardoffice.network.*
import com.example.tastywardoffice.overview.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import com.google.android.gms.maps.GoogleMap


//시작시 위치와 맵 정보 저장
private const val KEY_CAMERA_POSITION = "camera_position"
private const val KEY_LOCATION = "location"

class google_map : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private var latiTude = 37.56
    private var longItude = 126.97

    lateinit var geocoder: Geocoder
    lateinit var GoogleMap: GoogleMap
    lateinit var adress: MutableList<Address>
    lateinit var mContext: Context
    lateinit var storeName: String
    lateinit var testDTO: MyDTO

    private lateinit var mView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mainViewModel: MainViewModel

    private val TAG = "MapFragment"
    private val multiplePermissionCode = 100
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            mContext = context
        }
        Log.d(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        Log.d(TAG, "onCreate")
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentGoogleMapBinding.inflate(inflater)
        mView = binding.mapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        checkLocationPermission()


        //viewModel 관련
//        val nameObserver = Observer<Int> { it ->
//            binding.testText2.text = it.toString()
//        }
//        mainViewModel.height.observe(viewLifecycleOwner, nameObserver)
//
//
//        binding.testButton.setOnClickListener {
//            Log.d(TAG, mainViewModel.wholeData.value.toString())
//        }

        //현위치 버튼
        binding.myLocationButton.setOnClickListener {

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        latiTude = location.latitude
                        longItude = location.longitude
                    } else {
                        Log.d(TAG, "fail")
                    }
                }
            GoogleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(latiTude, longItude), 16f)
            )
        }

        binding.searchButton.setOnClickListener {
            binding.searchButton.setBackgroundColor(Color.parseColor("#afe3ff"))
        }


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        mView.onStart()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        GoogleMap = googleMap
        setDefaultLocation()
        totalShopData()


        googleMap.setOnInfoWindowClickListener(this)
//        googleMap.setOnMarkerClickListener(this)

        googleMap.setOnCameraIdleListener {

////            googleMap.addMarker(MarkerOptions()
////                .position(googleMap.cameraPosition.target)
////                .title("${googleMap.cameraPosition.target}")
////            )
//
//
//
//            val myLocation = Location("")
//            myLocation.latitude = googleMap.cameraPosition.target.latitude
//            myLocation.longitude = googleMap.cameraPosition.target.longitude
//
//            val location2 = Location("")
//            location2.latitude = 37.510371
//            location2.longitude = 126.945948
//
//            val location3 = Location("")
//            location3.latitude = 37.513184
//            location3.longitude = 126.944434
//
//            val location4 = Location("")
//            location4.latitude = 37.53752522054594
//            location4.longitude = 126.97773076593876
//
//            val locationTest2 = myLocation.distanceTo(location2) / 1000
//            val locationTest3 = myLocation.distanceTo(location3) / 1000
//            val locationTest4 = myLocation.distanceTo(location4) / 1000
//
//            Log.d("testdistance", locationTest2.toString())
//
//            val locationHashMap = hashMapOf<Float, Location>()
//            locationHashMap[locationTest2] = location2
//            locationHashMap[locationTest3] = location3
//            locationHashMap[locationTest4] = location4
//
//            Log.d("zoomtest", googleMap.cameraPosition.zoom.toString())
//
//
//
//            if (googleMap.cameraPosition.zoom >= 13){
//                for ((key, value) in locationHashMap) {
//                    if (key <= 0.5) {
//                        googleMap.addMarker(
//                            MarkerOptions()
//                                .position(LatLng(value.latitude, value.longitude))
//                                .title("1")
//                        )
//                    }
//                }
//            } else {
//                Toast.makeText(mContext, "지도를 확대해 주세요", Toast.LENGTH_SHORT).show()
//            }
//
//            val current = LatLng(latiTude, longItude)
//            GoogleMap.addMarker(
//                MarkerOptions()
//                    .position(current)
//                    .title("현재위치")
//            )
        }

        geocoder = Geocoder(mContext, Locale.KOREA)
        adress = geocoder.getFromLocation(latiTude, longItude, 1)

    }

    //퍼미션 체크 기능인데 밑에있음
//    private fun checkPermissions() {
//        var rejectedPermissionList = ArrayList<String>()
//
//        for(permission in requiredPermissions) {
//            if(ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
//                rejectedPermissionList.add(permission)
//            }
//        }
//
//        if(rejectedPermissionList.isNotEmpty()) {
//            val array = arrayOfNulls<String>(rejectedPermissionList.size)
//            ActivityCompat.requestPermissions(mContext as Activity, rejectedPermissionList.toArray(array), multiplePermissionCode)
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int,
//                                            permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            multiplePermissionCode -> {
//                if(grantResults.isNotEmpty()) {
//                    for((i, permission) in permissions.withIndex()) {
//                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                            //권한 획득 실패
//                            Log.i("TAG", "The user has denied to $permission")
//                            Log.i("TAG", "I can't work for you anymore then. ByeBye!")
//                        }
//                    }
//                }
//            }
//        }
//    }


    private fun testViewModelData() {
        for (i in mainViewModel.wholeData.value!!.stores) {
            Log.d(TAG, i.storeId)
        }
    }

    private fun totalShopData() {
        val tempData = RequestType("whole_stores")
        TastyWardApi.service.getWholeData(tempData).enqueue(object : Callback<WholeData> {
            override fun onResponse(call: Call<WholeData>, response: Response<WholeData>) {
                if (response.isSuccessful) {
                    for (i in response.body()!!.stores) {
                        GoogleMap.addMarker(
                            MarkerOptions()
                                .position(
                                    LatLng(
                                        i.storeGEOPoints.latitude,
                                        i.storeGEOPoints.longitude
                                    )
                                )
                                .title(i.storeId)
                                .snippet("성공입니다")
                        )?.showInfoWindow()
                    }
                } else {
                    val result: WholeData? = response.body()
                    Log.d("YMC", "onResponse 실패 " + result?.toString())
                }
            }

            override fun onFailure(call: Call<WholeData>, t: Throwable) {
                Log.d("YMC", "onFailure 에러 " + t.message.toString())
            }
        })
    }

    //지도 처음 띄웠을때 서울로 위치되도록
    private fun setDefaultLocation() {
        val defaultLocation = LatLng(latiTude, longItude)
        GoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 16f))
    }

    //퍼미션 체크 및 권한 요청 후 현위치로 이동 함수
    @SuppressLint("MissingPermission")
    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "위치 허가 완료")
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        latiTude = location.latitude
                        longItude = location.longitude
                        GoogleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(LatLng(latiTude, longItude), 16f)
                        )
                        Log.d(TAG, "$latiTude , $longItude")
                    } else {
                        Log.d(TAG, "fail")
                    }
                }
        } else {
            val rejectedPermissionList = ArrayList<String>()

            for (permission in requiredPermissions) {
                if (ContextCompat.checkSelfPermission(
                        mContext,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    rejectedPermissionList.add(permission)
                }
            }

            if (rejectedPermissionList.isNotEmpty()) {
                val array = arrayOfNulls<String>(rejectedPermissionList.size)
                ActivityCompat.requestPermissions(
                    mContext as Activity,
                    rejectedPermissionList.toArray(array),
                    multiplePermissionCode
                )
            }
            Toast.makeText(mContext, "위치권한을 확인해 주세요", Toast.LENGTH_SHORT).show()
        }
    }

    //지도 클릭시 좌표
//    override fun onMapClick(p0: LatLng) {
//        Log.d("Fposition", p0.toString())
//        Log.d("Fposition", GoogleMap.cameraPosition.target.toString())
//        val position = GoogleMap.cameraPosition.target
//        GoogleMap.addMarker(MarkerOptions()
//            .position(p0)
//        )
//        GoogleMap.addMarker(MarkerOptions()
//            .position(position)
//        )
//    }

    //마커 정보 클릭시 세부 메뉴로 이동
    override fun onInfoWindowClick(p0: Marker) {
        val action = google_mapDirections.actionGoogleMapToDetailMenu3(
            p0.title.toString(),
            "https://search.pstatic.net/common/?src=http%3A%2F%2Fpost.phinf.naver.net%2FMjAxODExMTZfMjE0%2FMDAxNTQyMzU4MDY1OTE3.mke0JLFBO4jS-hJojejDruHQmJkV7b4gKs3oRfn7tdIg.1LxHXj9zP7M09hPrht0iW17TRKkmCAgV6kEjTgPtPDcg.JPEG%2FI1P4kSElZvKVehuLxO8qMBSTUkIU.jpg&type=sc960_832",
            p0.position
        )

        findNavController().navigate(action)
    }

    //마커 클릭시 그쪽으로 확대
//    override fun onMarkerClick(p0: Marker): Boolean {
//        p0.showInfoWindow()
//        GoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(p0.position, 16F))
//        return true
//    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        mView.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        mView.onPause()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")

        mView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d(TAG, "onLowMemory")
        mView.onLowMemory()
    }

    override fun onDestroy() {
        mView.onDestroy()
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

}