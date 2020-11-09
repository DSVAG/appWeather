package com.dsvag.weather.data.repositorys

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationRepository(
    private val fusedLocationClient: FusedLocationProviderClient,
) {

    @SuppressLint("MissingPermission")
    fun fetchLocation(): Flow<Location> = callbackFlow {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                offer(location)
            } else {
                Log.e(TAG, "location null")
            }
        }

        awaitClose {}
    }

    companion object {
        private val TAG = LocationRepository::class.simpleName
    }
}