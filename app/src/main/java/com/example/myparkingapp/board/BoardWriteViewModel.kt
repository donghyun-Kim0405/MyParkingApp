package com.example.myparkingapp.board

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myparkingapp.main.MainRepository
import com.example.myparkingapp.UserData

class BoardWriteViewModel : ViewModel(){


    private val mainRepository : MainRepository?
    val userData = UserData.getInstance()
    var title : String=""
    var content : String=""
    init {
        mainRepository = MainRepository.getInstance()
    }

    //boardWriteViewModel.writeBoard(parkingLotNumber!!, formatted, callbacks, requireContext())
    public fun writeBoard(parkingLotNumber : String ,time: String, callbacks:BoardWriteFragment.Callbacks?, context:Context){ // uuid 필요
        mainRepository?.writeBoard(BoardModel(parkingLotNumber, title, content, time, userData?.getToken()), callbacks, context)
    }
}