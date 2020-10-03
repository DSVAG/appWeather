package com.dsvag.weather.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dsvag.weather.R
import com.dsvag.weather.data.adapters.ViewPagerAdapter
import com.dsvag.weather.data.network.ForecastDataSourceImpl
import com.dsvag.weather.data.utils.getAppComponent
import com.dsvag.weather.databinding.ActivityMainBinding
import com.dsvag.weather.ui.fragments.ForecastFragment
import com.dsvag.weather.ui.fragments.TodayFragment
import com.dsvag.weather.ui.fragments.TomorrowFragment
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity() {

    private val binding
            by lazy(LazyThreadSafetyMode.NONE) { ActivityMainBinding.inflate(layoutInflater) }

    private val viewPagerAdapter by lazy { ViewPagerAdapter(this) }

    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    private val apiService by lazy { getAppComponent().apiService }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)

        viewPagerAdapter.setData(listOf(TodayFragment(), TomorrowFragment(), ForecastFragment()))
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Today"
                1 -> tab.text = "Tomorrow"
                2 -> tab.text = "7 Days"
            }
        }.attach()

        val hourlyForecastDataSource = ForecastDataSourceImpl(apiService)

        apiCall(hourlyForecastDataSource)

        binding.swipeRefreshLayout.setOnRefreshListener {
            apiCall(hourlyForecastDataSource)

            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(locationRequestCode)
    private fun secondRequestPermission() {
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            EasyPermissions.requestPermissions(
                this, "", locationRequestCode, Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun apiCall(forecastDataSource: ForecastDataSourceImpl) {
        GlobalScope.launch(Dispatchers.IO) {
            //forecastDataSource.fetchHourlyForecast()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)

        builder.setMessage(getString(R.string.enable_location))
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                    dialog.cancel()
                    startActivity(this)
                }
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }

        val alert: AlertDialog = builder.create()
        alert.show()
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val locationRequestCode = 101
    }
}