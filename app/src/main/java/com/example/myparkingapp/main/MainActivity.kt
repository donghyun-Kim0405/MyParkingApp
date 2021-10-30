package com.example.myparkingapp.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myparkingapp.*
import com.example.myparkingapp.R
import com.example.myparkingapp.databinding.ActivityMainBinding
import com.example.myparkingapp.location.FusedLocationManager
import com.example.myparkingapp.parkingarea.ParkingArea
import com.example.myparkingapp.parkingarea.ParkingAreaInfoActivity
import com.example.myparkingapp.navigation.MyNavigation
import net.daum.mf.map.api.MapView


@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity(), MapUtils.Callbacks, MyNavigation.Callback, FusedLocationManager.Callback{

    private val PERMISSIONS=arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.INTERNET,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.READ_EXTERNAL_STORAGE)
    private lateinit var actionBar : ActionBar
    private var userData: UserData? = null
    private lateinit var binding : ActivityMainBinding
    private var mapView : MapView?=null
    private var mapUtils : MapUtils? = null
    private val viewModel : MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private lateinit var fusedLocationManager: FusedLocationManager
    private lateinit var myNavigation : MyNavigation

    //oncreate
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MainRepository.init(this)   //Database에 접근하기 위한 Repository객체 생성메서드 호출
        myNavigation = MyNavigation(this, binding)
        fusedLocationManager = FusedLocationManager(this)
        userData = UserData.getInstance()
        myNavigation.setNavView()
        setActionBar()
        getPermission()
        fusedLocationManager.setLocationClient()

        viewModel.currentLocation.observe(this, Observer {
            mapUtils?.moveToCenter(it)
            viewModel.updateParkingAreaData(it) //check
        })
        viewModel.parkingAreaData.observe(this, Observer {
            mapUtils?.updateMarker(it)
        })
        binding.appBarMain.mainContentLayout.btnSearch.setOnClickListener {
            viewModel.getPlaceData(binding.appBarMain.mainContentLayout.editText.text.toString())
        }
    }//oncreate

    override fun onResume() {
        super.onResume()

        if(mapView==null){
            mapView = MapView(this).also {
                mapUtils = MapUtils.getInstance(it, this)
                mapUtils?.setCustomBalloon()
                binding.appBarMain.mainContentLayout.mapView.addView(it)
                viewModel.parkingAreaData.value?.let { mapUtils?.updateMarker(it) }
            }
        }
    }

    private fun setActionBar(){
        setSupportActionBar(binding.appBarMain.toolbar)
        actionBar = supportActionBar!!
        actionBar.setDisplayShowCustomEnabled(false)    //기존의 title지우기
        actionBar.setDisplayHomeAsUpEnabled(true)   //뒤로가기 버튼이 생기게 하기 위해 True설정
        actionBar.setHomeAsUpIndicator(R.drawable.icon_nav_menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{   //home키 - toolbar에 추가된 외쪽 상단의 버튼을 의미 따로 id를 지정하지 않음
                binding.drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getPermission(){
        if(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED||
            checkSelfPermission(android.Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED||
            checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED||checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, PERMISSIONS, 100)}
    }

    override fun onBalloonSelected(parkingArea: ParkingArea) {
        var intent = Intent(this, ParkingAreaInfoActivity::class.java).apply { putExtra("parkingAreaData", parkingArea) }
        binding.appBarMain.mainContentLayout.mapView.removeView(mapView)
        mapView=null
        startActivity(intent)
    }

    override fun onLogoutSelected() {   // MyNavigation - callback
        viewModel.logout()
    }
    override fun setMapByLocation(location: Location) { //FuesedLocationManager - callback
        mapUtils?.moveToCenter(location)
        viewModel.updateParkingAreaData(location) //check
    }
}