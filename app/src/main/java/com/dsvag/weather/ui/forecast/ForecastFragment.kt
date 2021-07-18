package com.dsvag.weather.ui.forecast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dsvag.weather.R
import com.dsvag.weather.databinding.FragmentForecastBinding
import com.dsvag.weather.models.forecast.Daily
import com.dsvag.weather.ui.MainViewModel
import com.dsvag.weather.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast) {

    private val binding by viewBinding(FragmentForecastBinding::bind)

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {

        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collect { state ->
                state.forecast?.let { setData(it.daily) }
            }
        }

    }

    private fun setData(daily: List<Daily>) {

    }

}