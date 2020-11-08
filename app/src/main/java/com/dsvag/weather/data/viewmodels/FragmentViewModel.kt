package com.dsvag.weather.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dsvag.weather.data.di.getAppComponent

class FragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository by lazy { application.getAppComponent().repository }

    val forecast = repository.getForecastFromDb()

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FragmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FragmentViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}