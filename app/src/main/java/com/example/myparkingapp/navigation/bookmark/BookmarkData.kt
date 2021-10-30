package com.example.myparkingapp.navigation.bookmark

import com.google.gson.annotations.SerializedName

data class BookmarkData(
    @SerializedName("name")
    val name : String? = null,
    @SerializedName("address")
    val address : String? = null,
    @SerializedName("phone")
    val phone : String? = null,
    @SerializedName("fee")
    val fee : String? =null
)
