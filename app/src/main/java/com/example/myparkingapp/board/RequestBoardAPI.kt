package com.example.myparkingapp.board

import com.example.myparkingapp.Auth.JoinData
import com.example.myparkingapp.Auth.LoginData
import com.example.myparkingapp.Auth.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RequestBoardAPI {
    @POST("/board/write")
    public fun getWriteBoardCall(@Body boardModel: BoardModel) : Call<ResponseData>

    @POST("/board/get")
    public fun getBoardCall(@Body boardModel: BoardModel) :  Call<ArrayList<BoardModel>>
}


