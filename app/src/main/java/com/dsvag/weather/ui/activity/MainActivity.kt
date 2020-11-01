package com.dsvag.weather.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dsvag.weather.data.adapters.ViewPagerAdapter
import com.dsvag.weather.data.di.getAppComponent
import com.dsvag.weather.databinding.ActivityMainBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding
            by lazy(LazyThreadSafetyMode.NONE) { ActivityMainBinding.inflate(layoutInflater) }

    private val viewPagerAdapter by lazy { ViewPagerAdapter(this) }

    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    private val repository by lazy { getAppComponent().repository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Today"
                1 -> tab.text = "Tomorrow"
                2 -> tab.text = "7 Days"
            }
        }.attach()

        getLocation()
        apiCall()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationRequestCode && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                requestPermissions()
            }
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, appPermissions, locationRequestCode)
    }

    private fun checkGps(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("SetTextI18n")
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "Permission deny")
            requestPermissions()
            return
        }
        if (checkGps()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        repository.saveLocation(location.latitude, location.longitude)
                    } else {
                        Log.e(TAG, "Location null")
                    }
                }
//                .addOnCompleteListener { task: Task<Location> ->
//                    if (task.result != null) {
//                        GlobalScope.launch(Dispatchers.IO) {
//                            repository.saveLocation(task.result.latitude, task.result.longitude)
//                        }
//                        //repository.saveLocation(task.result.latitude, task.result.longitude)
//                    } else {
//                        Log.e(TAG, "Task result null", task.exception)
//                    }
//                }
        } else {
            createLocationDialog()
        }
    }

    private fun createLocationDialog() {
        val locationRequest = LocationRequest.create()

        val builder = LocationSettingsRequest.Builder().apply {
            addLocationRequest(locationRequest)
            setAlwaysShow(true)
        }

        val result =
            LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        try {
                            val resolvable = exception as ResolvableApiException
                            resolvable.startResolutionForResult(this, locationRequestCode)
                        } catch (e: IntentSender.SendIntentException) {
                            Log.e(TAG, "", e)
                        } catch (e: ClassCastException) {
                            Log.e(TAG, "", e)
                        }
                }
            }
        }
    }

    private fun apiCall() {
        val (lat, lon) = repository.getLocation()

        GlobalScope.launch(Dispatchers.IO) {
            repository.getForecast(lat, lon, "metric", "alerts")
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val locationRequestCode = 534
        private val appPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}