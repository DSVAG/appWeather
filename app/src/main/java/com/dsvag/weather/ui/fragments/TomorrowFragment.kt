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
import com.bumptech.glide.Glide
import com.dsvag.weather.R
import com.dsvag.weather.data.adapters.TemperatureAdapter
import com.dsvag.weather.data.adapters.WindAdapter
import com.dsvag.weather.data.models.Units
import com.dsvag.weather.data.models.request.Daily
import com.dsvag.weather.data.models.request.Forecast
import com.dsvag.weather.data.models.request.Hourly
import com.dsvag.weather.data.utils.convertEpochToOffsetDateTime
import com.dsvag.weather.data.utils.dateFormat
import com.dsvag.weather.data.utils.timeFormat
import com.dsvag.weather.data.viewmodels.FragmentViewModel
import com.dsvag.weather.databinding.FragmentTomorrowBinding

class TomorrowFragment : Fragment() {

    private var _binding: FragmentTomorrowBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this, FragmentViewModel.Factory(requireNotNull(this.activity).application)
        ).get(FragmentViewModel::class.java)
    }

    private lateinit var forecast: Forecast
    private val daily: Daily get() = forecast.daily[1]

    private val units by lazy {
        Units(getString(R.string.celsius), getString(R.string.mps), getString(R.string.degree))
    }

    private val temperatureAdapter by lazy { TemperatureAdapter() }
    private val windAdapter by lazy { WindAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTomorrowBinding.inflate(inflater, container, false)

        initRecyclerViews()
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
                forecast = newForecast
                setData()
            } else {
                Log.e(TAG, "Forecast null")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initRecyclerViews() {
        binding.current.hourlyTemp.setHasFixedSize(true)
        binding.current.hourlyTemp.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.current.hourlyTemp.adapter = temperatureAdapter

        binding.additional.wind.windRv.setHasFixedSize(true)
        binding.additional.wind.windRv.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.additional.wind.windRv.adapter = windAdapter
    }

    private fun setData() {
        forecastAfter6()

        val currentTime =
            convertEpochToOffsetDateTime(daily.dt, (forecast.timezoneOffset / 3600))

        binding.current.date.text = currentTime.format(dateFormat)
        binding.current.temp.visibility = View.GONE
        binding.current.degree.visibility = View.GONE
        binding.current.feelsLike.text =
            String.format("Fells like %.0f", daily.feelsLike.day).plus(units.degree)
        binding.current.condition.text = daily.weather[0].description

        binding.current.maxMinTemp.text = StringBuilder().apply {
            append(String.format("min %.0f", daily.temp.min).plus(units.degree))
            append(String.format("  max %.0f", daily.temp.max).plus(units.degree))
        }

        val url = "https://openweathermap.org/img/wn/${daily.weather[0].icon}@2x.png"
        Glide.with(binding.root).load(url).centerInside().into(binding.current.icon)

        setDetailData()
        setPrecipitationData()
        setWindData()
    }

    private fun setDetailData() {
        binding.additional.detail.humidity.text = daily.humidity.toString().plus("%")
        binding.additional.detail.devPoint.text =
            String.format("%.0f", daily.dewPoint).plus(units.temp)
        binding.additional.detail.pressure.text = daily.pressure.toString().plus("hPa")
        binding.additional.detail.uvIndex.text = String.format("%.0f", daily.uvi)
        binding.additional.detail.cloudiness.text = daily.clouds.toString().plus("%")
        binding.additional.detail.visibilityLabel.visibility = View.GONE
        binding.additional.detail.visibility.visibility = View.GONE

        val sunRise =
            convertEpochToOffsetDateTime(daily.sunrise, forecast.timezoneOffset / 3600)

        val sunSet =
            convertEpochToOffsetDateTime(daily.sunset, forecast.timezoneOffset / 3600)

        binding.additional.detail.sunRise.text = sunRise.format(timeFormat)
        binding.additional.detail.sunSet.text = sunSet.format(timeFormat)

        val t = sunSet.hour * 60 + sunSet.minute - sunRise.hour * 60 - sunRise.minute
        binding.additional.detail.solarDay.text = (t / 60).toString().plus(":" + t % 60)
    }

    private fun setPrecipitationData() {
        binding.additional.precipitation.root.visibility = View.GONE
    }

    private fun setWindData() {
        binding.additional.wind.windSpeed.text = daily.windSpeed.toString()
        binding.additional.wind.windDirection.rotation = (daily.windDeg + 180).toFloat() % 360
        binding.additional.wind.windUnits.text = units.windSpeed
    }

    private fun forecastAfter6() {
        val currentTime =
            convertEpochToOffsetDateTime(forecast.current.dt, forecast.timezoneOffset / 3600)

        val hourly = mutableListOf<Hourly>()

        forecast.hourly.forEach { hour ->
            val tmpTime = convertEpochToOffsetDateTime(hour.dt, 0)

            if ((currentTime.dayOfYear + 1 == tmpTime.dayOfYear) && (tmpTime.hour > 6)) {
                hourly.add(hour)
            } else if ((currentTime.dayOfYear + 2 == tmpTime.dayOfYear) && (tmpTime.hour <= 6)) {
                hourly.add(hour)
            }
        }

        temperatureAdapter.setData(hourly)
        windAdapter.setData(hourly)
    }

    companion object {
        private val TAG = TomorrowFragment::class.simpleName
    }
}