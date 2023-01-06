package com.example.tastywardoffice


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.tastywardoffice.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val TAG = "MainActivityTest"
    private val multiplePermissionCode = 100
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()

        binding.bottomNavigation.setupWithNavController(navController)

    }

    @SuppressLint("MissingPermission")
    private fun checkLocationPermission() {
            if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
                Log.d(TAG, "위치 허가 완료")
        } else {
            val rejectedPermissionList = ArrayList<String>()

            for (permission in requiredPermissions) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    rejectedPermissionList.add(permission)
                }
            }

            if (rejectedPermissionList.isNotEmpty()) {
                val array = arrayOfNulls<String>(rejectedPermissionList.size)
                ActivityCompat.requestPermissions(
                    this,
                    rejectedPermissionList.toArray(array),
                    multiplePermissionCode
                )
            }
            Toast.makeText(this, "위치권한을 확인해 주세요", Toast.LENGTH_SHORT).show()
        }
    }
}


