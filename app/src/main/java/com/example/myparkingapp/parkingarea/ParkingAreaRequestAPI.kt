package com.example.myparkingapp.parkingarea

import com.example.myparkingapp.location.PostLocation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ParkingAreaRequestAPI {

    @POST("/download/parkingAreaData")
    public fun getParkingAreaInfo(@Body postLocation : PostLocation) : Call<ArrayList<ParkingArea>>

}