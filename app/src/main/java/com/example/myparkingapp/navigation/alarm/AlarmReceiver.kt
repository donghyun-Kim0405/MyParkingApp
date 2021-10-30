package com.example.myparkingapp.navigation.alarm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.myparkingapp.Auth.IntroActivity
import com.example.myparkingapp.R


@RequiresApi(Build.VERSION_CODES.O)



class AlarmReceiver : BroadcastReceiver(){

    companion object {
        const val TAG = "AlarmReceiver"
        const val NOTIFICATION_ID = 0
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }

    private lateinit var wakeLock : PowerManager.WakeLock
    lateinit var notificationManager : NotificationManager



    @SuppressLint("InvalidWakeLockTag")
    override fun onReceive(context: Context?, intent: Intent?) {

        val powerManager = context?.getSystemService(Context.POWER_SERVICE) as PowerManager
        notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()
        deliverNotification(context)
    }

    private fun createNotificationChannel(){
        val notificationChannel = NotificationChannel(AlarmReceiver.PRIMARY_CHANNEL_ID, "parkingApp notification", NotificationManager.IMPORTANCE_HIGH).apply {
            enableLights(true)
            lightColor= Color.RED
            enableVibration(true)
            description="주차시간 완료 알람"
        }//notification channel 생성
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun deliverNotification(context : Context){
        //val contentIntent = Intent(context, AlarmActivity::class.java)
        val time = TimeUtil.getCurrentTime()
        val presentTime = time[2]+" "+time[0]+"시 "+time[1]+"분"
        val contentIntent = Intent(context, PopupActivity::class.java).apply {
            putExtra("time", presentTime)
        }
        val pendingIntent = PendingIntent.getActivity(context,
            AlarmReceiver.NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(context,
            AlarmReceiver.PRIMARY_CHANNEL_ID
        ).apply {
            setSmallIcon(R.drawable.icon_alarm)
            setContentTitle("MyParkingApp")
            setContentText("주차 시간을 확인해주세요!")
            setCategory(NotificationCompat.CATEGORY_ALARM)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setLocalOnly(true)
            setContentIntent(pendingIntent)
            setPriority(NotificationCompat.PRIORITY_HIGH)
            setAutoCancel(true)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            setFullScreenIntent(pendingIntent, true)
        }
        notificationManager.notify(AlarmReceiver.NOTIFICATION_ID, notificationBuilder.build())
    }
}