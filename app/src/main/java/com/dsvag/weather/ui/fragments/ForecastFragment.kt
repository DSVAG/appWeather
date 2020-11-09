package com.dsvag.weather.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dsvag.weather.R
import com.dsvag.weather.data.adapters.DailyAdapter
import com.dsvag.weather.data.models.Units
import com.dsvag.weather.data.models.request.Forecast
import com.dsvag.weather.data.viewmodels.FragmentViewModel
import com.dsvag.weather.databinding.FragmentForecastBinding

class ForecastFragment : Fragment() {

    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this, FragmentViewModel.Factory(requireNotNull(this.activity).application)
        ).get(FragmentViewModel::class.java)
    }

    private val dailyAdapter by lazy { DailyAdapter() }

    private val units by lazy {
        Units(getString(R.string.celsius), getString(R.string.mps), getString(R.string.degree))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForecastBinding.inflate(inflater, container, false)

        initRv()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                binding.root.context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                binding.root.context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.onLocationPermissionGranted()
        }

        viewModel.forecast.observe(viewLifecycleOwner) { newForecast: Forecast? ->
            if (newForecast != null) {
                dailyAdapter.setData(
                    newForecast.daily,
                    newForecast.timezoneOffset / 3600,
                    units
                )
            } else {
                Log.e(TAG, "Forecast null")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initRv() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        binding.recyclerView.adapter = dailyAdapter
    }

    companion object {
        private val TAG = ForecastFragment::class.simpleName
    }
}