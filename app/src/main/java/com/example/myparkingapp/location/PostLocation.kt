package com.example.myparkingapp.location

import com.google.gson.annotations.SerializedName

data class PostLocation(
    @SerializedName("lat") var lat : String,
    @SerializedName("lon") var lon : String
)