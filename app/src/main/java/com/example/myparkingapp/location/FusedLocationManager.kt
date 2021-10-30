package com.example.myparkingapp.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
class FusedLocationManager(val context : Context) {
    interface Callback{
        fun setMapByLocation(location : Location)
    }

    private var callback : Callback? = null
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var locationRequest : com.google.android.gms.location.LocationRequest
    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            callback?.setMapByLocation(locationResult.lastLocation)
        }
    }
    init{
        callback = context as Callback
    }


    public fun setLocationClient(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = com.google.android.gms.location.LocationRequest.create()
        locationRequest.priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        val interval = 60*100
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }


}