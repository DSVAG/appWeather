package com.dsvag.weather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dsvag.weather.data.models.Hourly.HourlyRequest
import com.dsvag.weather.databinding.FragmentTodayBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    private lateinit var forecast: HourlyRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun utcToDate(dt: Long): LocalDateTime {
        return Instant.ofEpochSecond(dt).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    companion object {
        const val TAG = "TodayFragment"
    }
}