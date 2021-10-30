package com.example.myparkingapp.Auth

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RequestAuthAPI {

    @POST("/user/join")
    public fun getJoinCall(@Body data : JoinData) : Call<ResponseData>

    @POST("/user/login")
    public fun getLoginCall(@Body data : LoginData) : Call<ResponseData>

    @POST("/user/auth")
    public fun getAuthCall(@Body data : AuthData) :  Call<ResponseData>

}