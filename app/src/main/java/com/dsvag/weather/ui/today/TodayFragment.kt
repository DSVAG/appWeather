package com.dsvag.weather.ui.today

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dsvag.weather.R
import com.dsvag.weather.databinding.FragmentTodayBinding
import com.dsvag.weather.models.forecast.ForecastResponse
import com.dsvag.weather.ui.MainViewModel
import com.dsvag.weather.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TodayFragment : Fragment(R.layout.fragment_today) {

    private val binding by viewBinding(FragmentTodayBinding::bind)

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collect { state ->
                state.forecast?.let { setData(it) }
            }
        }

    }

    private fun setData(forecast: ForecastResponse) {}
//        forecastUntil6Days()
//
//        val currentTime = convertEpochToOffsetDateTime(forecast.current.dt, (forecast.timezoneOffset / 3600))
//
//        binding.current.date.text = currentTime.format(dateWithTimeFormat)
//        binding.current.temp.text = String.format("%.0f", forecast.current.temp)
//        binding.current.degree.text = getString(R.string.celsius)
//        binding.current.feelsLike.text = String.format("Fells like %.0f", forecast.current.feelsLike).plus(getString(R.string.degree))
//        binding.current.condition.text = forecast.current.weather[0].description
//
//        binding.current.maxMinTemp.text = StringBuilder().apply {
//            append(String.format("min %.0f", forecast.daily.first().temp.min))
//            append(getString(R.string.degree))
//            append(String.format("  max %.0f", forecast.daily.first().temp.max))
//            append(getString(R.string.degree))
//        }
//
//        val url = "https://openweathermap.org/img/wn/${forecast.daily[0].weather[0].icon}@2x.png"
//        binding.current.icon.load(url)
//        setDetailData()
//        setPrecipitationData()
//        setWindData()
//    }
//
//    private fun setDetailData() {
//        binding.additional.detail.humidity.text = forecastResponse.current.humidity.toString().plus("%")
//        binding.additional.detail.devPoint.text =
//            String.format("%.0f", forecastResponse.current.dewPoint).plus(units.temp)
//        binding.additional.detail.pressure.text = forecastResponse.current.pressure.toString().plus("hPa")
//        binding.additional.detail.uvIndex.text = String.format("%.0f", forecastResponse.current.uvi)
//        binding.additional.detail.cloudiness.text = forecastResponse.current.clouds.toString().plus("%")
//        binding.additional.detail.visibility.text = forecastResponse.current.visibility.toString().plus("m")
//
//        val sunRise =
//            convertEpochToOffsetDateTime(forecastResponse.current.sunrise, forecastResponse.timezoneOffset / 3600)
//
//        val sunSet =
//            convertEpochToOffsetDateTime(forecastResponse.current.sunset, forecastResponse.timezoneOffset / 3600)
//
//        binding.additional.detail.sunRise.text = sunRise.format(timeFormat)
//        binding.additional.detail.sunSet.text = sunSet.format(timeFormat)
//
//        val t = sunSet.hour * 60 + sunSet.minute - sunRise.hour * 60 - sunRise.minute
//        binding.additional.detail.solarDay.text = (t / 60).toString().plus(":" + t % 60)
//    }
//
//    private fun setPrecipitationData() {
//        var precipitationSum = 0.0
//        forecastResponse.minutely.forEach { minutely ->
//            precipitationSum += minutely.precipitation
//        }
//        binding.additional.precipitation.pop.text = String.format("%.0f", daily.pop * 100).plus("%")
//        binding.additional.precipitation.dailyVolume.text =
//            String.format("%.2f mm", precipitationSum)
//    }
//
//    private fun setWindData() {
//        binding.additional.wind.windSpeed.text = forecastResponse.current.windSpeed.toString()
//        binding.additional.wind.windDirection.rotation =
//            (forecastResponse.current.windDeg + 180).toFloat() % 360
//        binding.additional.wind.windUnits.text = units.windSpeed
//    }
//
//    private fun forecastUntil6Days() {
//        val currentTime =
//            convertEpochToOffsetDateTime(forecastResponse.current.dt, forecastResponse.timezoneOffset / 3600)
//
//        val hourly = mutableListOf<Hourly>()
//
//        forecastResponse.hourly.forEach { hour ->
//            val tmpTime = convertEpochToOffsetDateTime(hour.dt, 0)
//
//            if ((currentTime.dayOfYear == tmpTime.dayOfYear) && (currentTime.hour <= tmpTime.hour)) {
//                hourly.add(hour)
//            } else if ((currentTime.dayOfYear + 1 == tmpTime.dayOfYear) && (tmpTime.hour <= 6)) {
//                hourly.add(hour)
//            }
//        }
//
//        temperatureAdapter.setData(hourly)
//        precipitationAdapter.setData(forecastResponse.minutely, forecastResponse.timezoneOffset)
//        windAdapter.setData(hourly)
//    }
}