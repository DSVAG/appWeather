package com.dsvag.weather.ui.tomorrow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dsvag.weather.R
import com.dsvag.weather.databinding.FragmentTomorrowBinding
import com.dsvag.weather.models.forecast.ForecastResponse
import com.dsvag.weather.ui.MainViewModel
import com.dsvag.weather.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TomorrowFragment : Fragment(R.layout.fragment_tomorrow) {

    private val binding by viewBinding(FragmentTomorrowBinding::bind)

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collect { state ->
                state.forecast?.let { setData(it) }
            }
        }

    }

    private fun setData(forecast: ForecastResponse) {}
//        forecastAfter6()
//
//        val currentTime =
//            convertEpochToOffsetDateTime(daily.dt, (forecastResponse.timezoneOffset / 3600))
//
//        binding.current.date.text = currentTime.format(dateFormat)
//        binding.current.temp.visibility = View.INVISIBLE
//        binding.current.degree.visibility = View.INVISIBLE
//        binding.current.feelsLike.text =
//            String.format("Fells like %.0f", daily.feelsLike.day).plus(units.degree)
//        binding.current.condition.text = daily.weather[0].description
//
//        binding.current.maxMinTemp.text = StringBuilder().apply {
//            append(String.format("min %.0f", daily.temp.min).plus(units.degree))
//            append(String.format("  max %.0f", daily.temp.max).plus(units.degree))
//        }
//
//        val url = "https://openweathermap.org/img/wn/${daily.weather[0].icon}@2x.png"
//        Glide.with(binding.root).load(url).centerInside().into(binding.current.icon)
//
//        setDetailData()
//        setPrecipitationData()
//        setWindData()
//    }
//
//    private fun setDetailData() {
//        binding.additional.detail.humidity.text = daily.humidity.toString().plus("%")
//        binding.additional.detail.devPoint.text =
//            String.format("%.0f", daily.dewPoint).plus(units.temp)
//        binding.additional.detail.pressure.text = daily.pressure.toString().plus("hPa")
//        binding.additional.detail.uvIndex.text = String.format("%.0f", daily.uvi)
//        binding.additional.detail.cloudiness.text = daily.clouds.toString().plus("%")
//        binding.additional.detail.visibilityLabel.visibility = View.GONE
//        binding.additional.detail.visibility.visibility = View.GONE
//
//        val sunRise =
//            convertEpochToOffsetDateTime(daily.sunrise, forecastResponse.timezoneOffset / 3600)
//
//        val sunSet =
//            convertEpochToOffsetDateTime(daily.sunset, forecastResponse.timezoneOffset / 3600)
//
//        binding.additional.detail.sunRise.text = sunRise.format(timeFormat)
//        binding.additional.detail.sunSet.text = sunSet.format(timeFormat)
//
//        val t = sunSet.hour * 60 + sunSet.minute - sunRise.hour * 60 - sunRise.minute
//        binding.additional.detail.solarDay.text = (t / 60).toString().plus(":" + t % 60)
//    }
//
//    private fun setPrecipitationData() {
//        binding.additional.precipitation.root.visibility = View.GONE
//    }
//
//    private fun setWindData() {
//        binding.additional.wind.windSpeed.text = daily.windSpeed.toString()
//        binding.additional.wind.windDirection.rotation = (daily.windDeg + 180).toFloat() % 360
//        binding.additional.wind.windUnits.text = units.windSpeed
//    }
//
//    private fun forecastAfter6() {
//        val currentTime =
//            convertEpochToOffsetDateTime(forecastResponse.current.dt, forecastResponse.timezoneOffset / 3600)
//
//        val hourly = mutableListOf<Hourly>()
//
//        forecastResponse.hourly.forEach { hour ->
//            val tmpTime = convertEpochToOffsetDateTime(hour.dt, 0)
//
//            if ((currentTime.dayOfYear + 1 == tmpTime.dayOfYear) && (tmpTime.hour > 6)) {
//                hourly.add(hour)
//            } else if ((currentTime.dayOfYear + 2 == tmpTime.dayOfYear) && (tmpTime.hour <= 6)) {
//                hourly.add(hour)
//            }
//        }
//
//        temperatureAdapter.setData(hourly)
//        windAdapter.setData(hourly)
//    }

}