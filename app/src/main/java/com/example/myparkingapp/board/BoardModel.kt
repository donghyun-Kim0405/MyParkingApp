package com.example.myparkingapp.board

import com.google.gson.annotations.SerializedName

public class BoardModel(
    @SerializedName("parkingLotNumber")
    var parkingLotNumber : String="",
    @SerializedName("title")
    var title : String = "",
    @SerializedName("content")
    var content : String = "",
    @SerializedName("time")
    var time : String = "",
    @SerializedName("token")
    var token : String?="",
    @SerializedName("email")
    var email : String? = "",
)





