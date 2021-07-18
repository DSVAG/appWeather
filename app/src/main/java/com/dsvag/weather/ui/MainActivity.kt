package com.dsvag.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dsvag.weather.data.utils.fastLazy
import com.dsvag.weather.databinding.ActivityMainBinding
import com.dsvag.weather.ui.adapters.ViewPagerAdapter
import com.dsvag.weather.utils.showShortToast
import com.dsvag.weather.utils.viewBinding
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val viewModel by viewModels<MainViewModel>()

    private val viewPagerAdapter by fastLazy { ViewPagerAdapter(this) }

    private val tabLayoutMediator by fastLazy {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Today"
                1 -> tab.text = "Tomorrow"
                2 -> tab.text = "7 Days"
            }
        }
    }

    private val fusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewPager.adapter = viewPagerAdapter
        tabLayoutMediator.attach()

        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collect(::stateObserver)
        }

        checkPermissions()
        registerNetworkCallback()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationRequestCode && grantResults.isNotEmpty()) {
            viewModel.setPermissionsStatus(grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }

    private fun stateObserver(state: MainViewModel.State) {
        with(state) {
            if (permissionStatus && networkStatus) {
                lastLocation()
            } else {
                if (!permissionStatus) {
                    viewModel.setError(MainViewModel.ErrorType.Permissions)
                    requestPermissions(appPermissions, locationRequestCode)
                }

                if (!networkStatus) {
                    viewModel.setError(MainViewModel.ErrorType.Network)
                }

            }

            //Put in resources
            when (errorType) {
                MainViewModel.ErrorType.Network -> showShortToast("Turn on network and try again")
                MainViewModel.ErrorType.Server -> showShortToast("Server not response")
                MainViewModel.ErrorType.Location -> showShortToast("Can't find your location")
                MainViewModel.ErrorType.Permissions -> {
                    showShortToast("Grant permission")
                    checkPermissions()
                }
                MainViewModel.ErrorType.Other -> showShortToast("Unknown error. Restart app")
            }
        }
    }

    private fun checkPermissions() {
        viewModel.setPermissionsStatus(
            appPermissions.all { permission ->
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
            }
        )
    }

    private fun registerNetworkCallback() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {

            override fun onLosing(network: Network, maxMsToLive: Int) {
                viewModel.setNetworkStatus(false)
            }

            override fun onAvailable(network: Network) {
                viewModel.setNetworkStatus(true)
            }

            override fun onLost(network: Network) {
                viewModel.setNetworkStatus(false)
            }

            override fun onUnavailable() {
                viewModel.setNetworkStatus(false)
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun lastLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            viewModel.fetchForecast(location)
        }
    }

    companion object {
        private const val locationRequestCode = 534
        private val appPermissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    }
}