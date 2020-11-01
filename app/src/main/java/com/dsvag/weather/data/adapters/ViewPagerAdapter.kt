package com.dsvag.weather.data.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dsvag.weather.ui.fragments.ForecastFragment
import com.dsvag.weather.ui.fragments.TodayFragment
import com.dsvag.weather.ui.fragments.TomorrowFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodayFragment()
            1 -> TomorrowFragment()
            2 -> ForecastFragment()
            else -> error("unknown position $position")
        }
    }

    override fun getItemCount() = 3
}