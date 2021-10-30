package com.example.myparkingapp

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class UserData private constructor(){
    companion object{
        private var email:String?=null
        private var token :String? = null
        private var instance: UserData? = null
        public fun getInstance() : UserData?{
            if(instance == null){
                Log.e("UserData", "CREATED!!!!!!!!!!!!!!!!")
                instance = UserData()
            }
            return instance
        }
    }

    public fun getToken():String? = token
    public fun getEmail():String? = email

    public fun login(_token : String?, _email : String?){
        token = _token
        email = _email
    }
    public fun logout(){
        token=null
        email=null
    }
}