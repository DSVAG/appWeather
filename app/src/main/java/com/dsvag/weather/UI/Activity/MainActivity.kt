package com.dsvag.weather.UI.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.dsvag.weather.Data.Adapters.ViewPagerAdapter
import com.dsvag.weather.Data.Models.Hourly.HourlyRequest
import com.dsvag.weather.Data.Network.ApiHourlyForecast
import com.dsvag.weather.Data.Network.ConnectivityInterceptorImpl
import com.dsvag.weather.Data.Network.HourlyForecastDataSourceImpl
import com.dsvag.weather.R
import com.dsvag.weather.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var connectivityInterceptor: ConnectivityInterceptorImpl
    private lateinit var sharedPreferences: SharedPreferences

    private val preferencesName = "SomeData"

    private val viewPagerAdapter by lazy { ViewPagerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = viewPagerAdapter

        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> tab.text = "Today"
                    1 -> tab.text = "Tomorrow"
                    2 -> tab.text = "7 Days"
                }
            })
        tabLayoutMediator.attach()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        connectivityInterceptor = ConnectivityInterceptorImpl(this)

        sharedPreferences = this.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

        val apiService = ApiHourlyForecast(connectivityInterceptor)
        val hourlyForecastDataSource = HourlyForecastDataSourceImpl(apiService)

        apiCall(hourlyForecastDataSource)


        /*binding.swipeRefreshLayout.setOnRefreshListener {
            Log.d(TAG, "Activity refreshed")

            getLastLocation()
            apiCall(hourlyForecastDataSource)

            binding.swipeRefreshLayout.isRefreshing = false
        }*/
    }

    public override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    private fun apiCall(hourlyForecastDataSource: HourlyForecastDataSourceImpl) {
        Log.d(TAG, "Try get location")
        GlobalScope.launch(Dispatchers.IO) {
            val lat = sharedPreferences.getFloat("Lat", 91.0F).toDouble()
            val lon = sharedPreferences.getFloat("Lon", 181.0F).toDouble()

            if (lat < 90 && lon < 180) {
                hourlyForecastDataSource.fetchHourlyForecast(lat, lon)
                Log.d(TAG, "Location correct format")
            } else {
                Log.d(TAG, "Location incorrect format")
                getLastLocation()
                return@launch
            }
        }
        Log.d(TAG, "Try api call")
        if (connectivityInterceptor.isOnline())
            hourlyForecastDataSource.downloadHourlyForecast.observe(this, Observer {
                saveData(it)
            })
        else {
            Toast.makeText(this, "Network error. Check Network state.", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        Log.d(TAG, "Try get location")
        fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                saveLocation(task.result!!.latitude, task.result!!.longitude)
            } else {
                Log.w(TAG, "getLastLocation:exception", task.exception)
                buildAlertMessageNoGps()
            }
        }
    }

    private fun saveData(hourly: HourlyRequest) {
        Log.d(TAG, "Try save data")
        val editor = sharedPreferences.edit()
        editor.putString("Hourly", Gson().toJson(hourly))
        editor.apply()
    }

    private fun saveLocation(lat: Double, lon: Double) {
        Log.d(TAG, "Try save location")
        val editor = sharedPreferences.edit()
        editor.putFloat("Lat", lat.toFloat())
        editor.putFloat("Lon", lon.toFloat())
        editor.apply()
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            locationRequestCode
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            Log.d(TAG, "Displaying permission rationale to provide additional context.")
            buildAlertMessageNoPermission()

        } else {
            Log.d(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionResult")
        if (requestCode == locationRequestCode) {
            when {
                grantResults.isEmpty() -> {
                    Log.d(TAG, "User interaction was cancelled.")
                    MainActivity().finishAffinity()
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    getLastLocation()
                }
                else -> {
                    MainActivity().finishAffinity()
                }
            }
        }
    }

    private fun buildAlertMessageNoPermission() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        builder.setMessage("App hasn't permission, enable it to stable app work")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                Intent(
                    ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${this.packageName}")
                ).apply {
                    addCategory(Intent.CATEGORY_DEFAULT)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(this)
                }
            }.setNegativeButton("No") { _, _ -> this.finish() }

        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialog.cancel()
            }.setNegativeButton("No") { dialog, _ -> dialog.cancel() }

        val alert: AlertDialog = builder.create()
        alert.show()
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val locationRequestCode = 101
    }
}