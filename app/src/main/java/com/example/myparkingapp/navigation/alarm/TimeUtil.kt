package com.example.myparkingapp.navigation.alarm

import java.text.SimpleDateFormat
import java.util.*

class TimeUtil {
    companion object{
        public fun getCurrentTime(): List<String> {
            val now = System.currentTimeMillis()
            val date = Date(now)
            val format = SimpleDateFormat("h:m:aa")
            val time = format.format(date).split(':')
            return time
        }

        public fun getAlarmTime(hour:String, min:String, ampm:String): Long {
            var targetHour:String=hour
            if(ampm.equals("오후")) { targetHour = (hour.toInt()+12).toString() }

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, targetHour.toInt())
                set(Calendar.MINUTE, min.toInt())
            }
            return calendar.timeInMillis
        }
    }



}