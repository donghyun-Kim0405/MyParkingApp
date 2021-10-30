package com.example.myparkingapp.main

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myparkingapp.parkingarea.ParkingArea

class MainViewModel : ViewModel(){

    private val mainRepository : MainRepository? = MainRepository.getInstance()

    public var currentLocation = MutableLiveData<Location>()
    public var parkingAreaData = MutableLiveData<ArrayList<ParkingArea>>()

    public fun getPlaceData(target : String){
        mainRepository!!.getPlaceData(target, currentLocation)
    }

    public fun updateParkingAreaData(location:Location){
        mainRepository!!.getParkingAreaInfo(location, parkingAreaData)
    }

    public fun logout(){
        //sqlWrite!!.execSQL("UPDATE userTBL SET uuid='${uuid}';")    //내부 데이터베이스에 서버로 부터 받은 uuid저장 -> splash에서 서버로 유효성 검사 수행
        mainRepository!!.logout()
    }

}