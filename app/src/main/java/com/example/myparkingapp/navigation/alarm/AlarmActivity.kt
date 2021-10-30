package com.example.myparkingapp.navigation.alarm

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myparkingapp.R
import com.example.myparkingapp.databinding.ActivityAlarmBinding
import java.text.SimpleDateFormat
import java.util.*

class AlarmActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAlarmBinding
    private var alarmManager : AlarmManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        setCurrentTime()
        binding.btnAddHour.setOnClickListener {
            binding.editHour.apply { if(text.toString().equals("12")) setText("1") else setText((text.toString().toInt()+1).toString()) }
        }
        binding.btnSubHour.setOnClickListener {
            binding.editHour.apply { if(text.toString().equals("1")) setText("12") else setText((text.toString().toInt()-1).toString()) }
        }
        binding.btnAddMin.setOnClickListener {
            binding.editMin.apply { if(text.toString().equals("60")) setText("1") else setText((text.toString().toInt()+1).toString()) }
        }
        binding.btnSubMin.setOnClickListener {
            binding.editMin.apply { if(text.toString().equals("1")) setText("60") else setText((text.toString().toInt()-1).toString()) }
        }
        binding.btnAmpm.setOnClickListener {
            binding.btnAmpm.apply { if(text == "오후") text = "오전" else text = "오후" }
        }

        binding.btnSetAlarm.setOnClickListener {
            if(binding.btnSetAlarm.text.toString().equals("알람설정")){
                setAlarm()
                binding.btnSetAlarm.text = "알람취소"
            }else{
                cancelAlarm()
                binding.btnSetAlarm.text = "알람설정"
            }
        }

    }

    override fun onResume() {
        super.onResume()

    }
    private fun cancelAlarm(){
        val receiverIntent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, receiverIntent, PendingIntent.FLAG_NO_CREATE)
        if(pendingIntent!=null&&alarmManager!=null) alarmManager!!.cancel(pendingIntent)
    }

    private fun setAlarm(){
        val receiverIntent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT)   // need to care request Code
        val alarmTime = TimeUtil.getAlarmTime(binding.editHour.text.toString(), binding.editMin.text.toString(), binding.btnAmpm.text.toString())

        alarmManager?.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+5000, pendingIntent)
        //alarmManager?.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
        Toast.makeText(baseContext, "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun setCurrentTime(){
        val time = TimeUtil.getCurrentTime()
        binding.btnAmpm.text = time[2]
        binding.editHour.setText(time[0])
        binding.editMin.setText(time[1])
    }

}