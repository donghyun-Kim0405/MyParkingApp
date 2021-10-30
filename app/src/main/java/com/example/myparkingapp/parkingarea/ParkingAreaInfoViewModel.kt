package com.example.myparkingapp.parkingarea

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myparkingapp.main.MainRepository
import com.example.myparkingapp.navigation.bookmark.BookmarkData

class ParkingAreaInfoViewModel : ViewModel() {
    private val mainRepository : MainRepository? = MainRepository.getInstance()
    public val bookmarkFlag = MutableLiveData<Boolean>().apply {
        value = false
    }

    public fun saveBookmark(bookmarkData : BookmarkData) { bookmarkFlag.value=mainRepository?.saveBookmark(bookmarkData)}
    public fun checkBookmark(address : String?){ bookmarkFlag.value = mainRepository?.checkBookmark(address)}
    public fun deleteBookmark(address : String?){bookmarkFlag.value=mainRepository?.deleteBookmark(address)}


}