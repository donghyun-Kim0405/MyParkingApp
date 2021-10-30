package com.example.myparkingapp.navigation.bookmark

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myparkingapp.main.MainRepository
import com.example.myparkingapp.parkingarea.ParkingArea

class BookmarkViewModel : ViewModel(){
    private val mainRepository : MainRepository? = MainRepository.getInstance()
    public val bookmarkDatas : MutableLiveData<ArrayList<BookmarkData>> = MutableLiveData()
    public val parkingArea : MutableLiveData<ParkingArea> = MutableLiveData()

    public fun getBookmarkData(){
        bookmarkDatas.value = mainRepository?.getBookmarkData()
    }

}