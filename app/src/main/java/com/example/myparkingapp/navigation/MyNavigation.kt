package com.example.myparkingapp.navigation

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.myparkingapp.Auth.IntroActivity
import com.example.myparkingapp.Auth.LoginActivity
import com.example.myparkingapp.R
import com.example.myparkingapp.UserData
import com.example.myparkingapp.databinding.ActivityMainBinding
import com.example.myparkingapp.main.MainViewModel
import com.example.myparkingapp.navigation.alarm.AlarmActivity
import com.example.myparkingapp.navigation.bookmark.BookmarkActivity
import com.google.android.material.navigation.NavigationView

class MyNavigation(val context : Context, val binding : ActivityMainBinding) : NavigationView.OnNavigationItemSelectedListener{

    interface Callback{
        fun onLogoutSelected()
    }

    private lateinit var btnNavState : TextView
    private lateinit var textEmail : TextView
    private var userData : UserData? = null
    private var callback : Callback? = null

    init{
        callback = context as Callback
    }


    public fun setNavView(){

        userData = UserData.getInstance()
        btnNavState = binding.navView.getHeaderView(0).findViewById(R.id.navLogStateBtn)
        textEmail = binding.navView.getHeaderView(0).findViewById(R.id.text_email)
        binding.navView.setNavigationItemSelectedListener(this)
        val token = userData?.getToken()

        if(token!=null){
            btnNavState.text = "로그아웃하기"
            textEmail.visibility = View.VISIBLE
            textEmail.text = userData?.getEmail()

            btnNavState.setOnClickListener {
                val intent = Intent(context, IntroActivity::class.java)
                userData?.logout()
                callback?.onLogoutSelected()
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent)
            }//클릭시 로그아웃 수행

        }else{
            textEmail.visibility = View.GONE
            btnNavState.text = "로그인하기"
            btnNavState.setOnClickListener {
                val intent = Intent(context, LoginActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent)}
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_timer ->{
                context.startActivity(Intent(context, AlarmActivity::class.java))
                return true
            }
            R.id.nav_bookmark->{
                context.startActivity(Intent(context, BookmarkActivity::class.java))
                Log.e("nav_bookmarkCalled", "success")
                return true
            }

        }
        return true
    }
}