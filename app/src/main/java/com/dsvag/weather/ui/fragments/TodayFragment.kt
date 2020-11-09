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
import com.dsvag.weather.data.adapters.PrecipitationAdapter
import com.dsvag.weather.data.adapters.TemperatureAdapter
import com.dsvag.weather.data.adapters.WindAdapter
import com.dsvag.weather.data.models.Units
import com.dsvag.weather.data.models.request.Forecast
import com.dsvag.weather.data.models.request.Hourly
import com.dsvag.weather.data.utils.convertEpochToOffsetDateTime
import com.dsvag.weather.data.utils.dateWithTimeFormat
import com.dsvag.weather.data.utils.timeFormat
import com.dsvag.weather.data.viewmodels.FragmentViewModel
import com.dsvag.weather.databinding.FragmentTodayBinding

class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this, FragmentViewModel.Factory(requireNotNull(this.activity).application)
        ).get(FragmentViewModel::class.java)
    }

    private lateinit var forecast: Forecast
    private val daily get() = forecast.daily[0]

    private val units by lazy {
        Units(getString(R.string.celsius), getString(R.string.mps), getString(R.string.degree))
    }

    private val temperatureAdapter by lazy { TemperatureAdapter() }
    private val windAdapter by lazy { WindAdapter() }
    private val precipitationAdapter by lazy { PrecipitationAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)

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

        binding.additional.precipitation.precipitationRv.setHasFixedSize(true)
        binding.additional.precipitation.precipitationRv.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.additional.precipitation.precipitationRv.adapter = precipitationAdapter
    }

    private fun setData() {
        forecastUntil6()

        val currentTime =
            convertEpochToOffsetDateTime(forecast.current.dt, (forecast.timezoneOffset / 3600))

        binding.current.date.text = currentTime.format(dateWithTimeFormat)
        binding.current.temp.text = String.format("%.0f", forecast.current.temp)
        binding.current.degree.text = units.temp
        binding.current.feelsLike.text =
            String.format("Fells like %.0f", forecast.current.feelsLike).plus(units.degree)
        binding.current.condition.text = forecast.current.weather[0].description

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
        binding.additional.detail.humidity.text = forecast.current.humidity.toString().plus("%")
        binding.additional.detail.devPoint.text =
            String.format("%.0f", forecast.current.dewPoint).plus(units.temp)
        binding.additional.detail.pressure.text = forecast.current.pressure.toString().plus("hPa")
        binding.additional.detail.uvIndex.text = String.format("%.0f", forecast.current.uvi)
        binding.additional.detail.cloudiness.text = forecast.current.clouds.toString().plus("%")
        binding.additional.detail.visibility.text = forecast.current.visibility.toString().plus("m")

        val sunRise =
            convertEpochToOffsetDateTime(forecast.current.sunrise, forecast.timezoneOffset / 3600)

        val sunSet =
            convertEpochToOffsetDateTime(forecast.current.sunset, forecast.timezoneOffset / 3600)

        binding.additional.detail.sunRise.text = sunRise.format(timeFormat)
        binding.additional.detail.sunSet.text = sunSet.format(timeFormat)

        val t = sunSet.hour * 60 + sunSet.minute - sunRise.hour * 60 - sunRise.minute
        binding.additional.detail.solarDay.text = (t / 60).toString().plus(":" + t % 60)
    }

    private fun setPrecipitationData() {
        var precipitationSum = 0.0
        forecast.minutely.forEach { minutely ->
            precipitationSum += minutely.precipitation
        }
        binding.additional.precipitation.pop.text = String.format("%.0f", daily.pop * 100).plus("%")
        binding.additional.precipitation.dailyVolume.text =
            String.format("%.2f mm", precipitationSum)
    }

    private fun setWindData() {
        binding.additional.wind.windSpeed.text = forecast.current.windSpeed.toString()
        binding.additional.wind.windDirection.rotation =
            (forecast.current.windDeg + 180).toFloat() % 360
        binding.additional.wind.windUnits.text = units.windSpeed
    }

    private fun forecastUntil6() {
        val currentTime =
            convertEpochToOffsetDateTime(forecast.current.dt, forecast.timezoneOffset / 3600)

        val hourly = mutableListOf<Hourly>()

        forecast.hourly.forEach { hour ->
            val tmpTime = convertEpochToOffsetDateTime(hour.dt, 0)

            if ((currentTime.dayOfYear == tmpTime.dayOfYear) && (currentTime.hour <= tmpTime.hour)) {
                hourly.add(hour)
            } else if ((currentTime.dayOfYear + 1 == tmpTime.dayOfYear) && (tmpTime.hour <= 6)) {
                hourly.add(hour)
            }
        }

        temperatureAdapter.setData(hourly)
        precipitationAdapter.setData(forecast.minutely, forecast.timezoneOffset)
        windAdapter.setData(hourly)
    }

    companion object {
        private val TAG = TodayFragment::class.simpleName
    }
}