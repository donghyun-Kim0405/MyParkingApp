package com.example.myparkingapp.main

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ParkingAppDBHelper(context: Context) : SQLiteOpenHelper(context, "ParkingAppDB", null, 1){
    override fun onCreate(p0: SQLiteDatabase?) {

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

}