package com.example.myparkingapp.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myparkingapp.main.MainRepository

class BoardMainViewModel : ViewModel(){

    private val mainRepository : MainRepository?
    public var reviews :MutableLiveData<ArrayList<BoardModel>> = MutableLiveData<ArrayList<BoardModel>>()
    init {
        mainRepository = MainRepository.getInstance()
        reviews.value = ArrayList<BoardModel>()
    }
    public fun getReviews(parkingLotNumber:String?){
        mainRepository?.getReviews(parkingLotNumber, reviews)
    }
}