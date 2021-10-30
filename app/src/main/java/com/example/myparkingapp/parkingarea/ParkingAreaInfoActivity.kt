package com.example.myparkingapp.parkingarea

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myparkingapp.MapUtils
import com.example.myparkingapp.R
import com.example.myparkingapp.board.BoardActivity
import com.example.myparkingapp.databinding.ActivityParkingAreaInfoBinding
import com.example.myparkingapp.navigation.bookmark.BookmarkData
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class ParkingAreaInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityParkingAreaInfoBinding
    private var mapView : MapView? = null
    private var mapUtils : MapUtils? = null
    var parkingArea : ParkingArea? = null
    private val viewModel : ParkingAreaInfoViewModel by lazy {
        ViewModelProvider(this).get(ParkingAreaInfoViewModel::class.java)
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_parking_area_info)
        parkingArea = intent.getSerializableExtra("parkingAreaData") as ParkingArea?

        setParkingAreaInfo()
        viewModel.checkBookmark(parkingArea?.locationSupportNumber) //bookmark 기록 있는지 확인

        viewModel.bookmarkFlag.observe(this, Observer {
            updateBookmark(it)
        })

        binding.imgBookmark.setOnClickListener {
            if(viewModel.bookmarkFlag.value!!){
                viewModel.deleteBookmark(parkingArea?.locationSupportNumber)
            }else{
                viewModel.saveBookmark(BookmarkData(parkingArea?.parkingAreaName,parkingArea?.locationSupportNumber,parkingArea?.phoneNumber, parkingArea?.basicParkingFee))
            }
        }

        binding.btnParkingAreaInfo.setOnClickListener {
            binding.layoutFee.visibility = View.INVISIBLE
            binding.layoutOperation.visibility = View.INVISIBLE
            binding.layoutParkingAreaInfo.visibility = View.VISIBLE
        }

        binding.btnOperation.setOnClickListener {
            binding.layoutFee.visibility = View.INVISIBLE
            binding.layoutParkingAreaInfo.visibility = View.INVISIBLE
            binding.layoutOperation.visibility = View.VISIBLE
        }

        binding.btnFee.setOnClickListener {
            binding.layoutOperation.visibility = View.INVISIBLE
            binding.layoutParkingAreaInfo.visibility = View.INVISIBLE
            binding.layoutFee.visibility = View.VISIBLE
        }

        binding.btnReview.setOnClickListener {
            val intent = Intent(this, BoardActivity::class.java)
            intent.putExtra("parkingLotNumber", parkingArea!!.parkingAreaManagementNumber)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if(parkingArea==null) parkingArea = intent.getSerializableExtra("parkingAreaData") as ParkingArea?

        setMapView(parkingArea!!.latitude, parkingArea!!.longitude)
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.removeView(mapView)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setMapView(lat : Double, lon : Double){
        mapView = MapView(this).also {
            it.mapType = MapView.MapType.Satellite
            it.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(lat, lon), 0, true)
            it.onDragEvent(null)
            binding.mapView.addView(it)
        }
    }
    private fun updateBookmark(flag : Boolean){
        if(flag){
            binding.imgBookmark.setImageResource(R.drawable.icon_bookmark_selected)
        }else{
            binding.imgBookmark.setImageResource(R.drawable.icon_bookmark_unselected)
        }
    }

    private fun setParkingAreaInfo(){
        binding.parkingAreaName.text=binding.parkingAreaName.text.toString()+parkingArea!!.parkingAreaName
        binding.parkingLotClassification.text = binding.parkingLotClassification.text.toString()+parkingArea!!.parkingLotClassification
        binding.parkingType.text = binding.parkingType.text.toString()+parkingArea!!.parkingType
        binding.locationSupportNumber.text = binding.locationSupportNumber.text.toString()+parkingArea!!.locationSupportNumber
        binding.numberOfParkingLots.text = binding.numberOfParkingLots.text.toString()+parkingArea!!.numberOfParkingLots
        binding.phoneNumber.text = binding.phoneNumber.text.toString()+parkingArea!!.phoneNumber
        binding.specialNote.text = binding.specialNote.text.toString()+parkingArea!!.specialNote

        binding.operatingDays.text = binding.operatingDays.text.toString()+parkingArea!!.operatingDays
        binding.weekdayOperationStartTime.text = binding.weekdayOperationStartTime.text.toString()+parkingArea!!.weekdayOperationStartTime
        binding.weekdayOperationEndTime.text = binding.weekdayOperationEndTime.text.toString()+parkingArea!!.weekdayOperationEndTime
        binding.saturdayOperationStartTime.text =binding.saturdayOperationStartTime.text.toString()+parkingArea!!.saturdayOperationStartTime
        binding.saturdayOperationEndTime.text =binding.saturdayOperationEndTime.text.toString()+parkingArea!!.saturdayOperationEndTime
        binding.holidayOperationStartTime.text = binding.holidayOperationStartTime.text.toString()+parkingArea!!.holidayOperationStartTime
        binding.holidayOperationEndTime.text = binding.holidayOperationEndTime.text.toString()+parkingArea!!.holidayOperationEndTime

        binding.rateInformation.text = binding.rateInformation.text.toString()+parkingArea!!.rateInformation
        binding.basicParkingFee.text = binding.basicParkingFee.text.toString()+parkingArea!!.basicParkingFee
        binding.additionalCharge.text = binding.additionalCharge.text.toString()+parkingArea!!.additionalCharge
        binding.additionalUnitTime.text = binding.additionalUnitTime.text.toString()+parkingArea!!.additionalUnitTime
        binding.applicationTimeForParkingTicketPerDay.text = binding.applicationTimeForParkingTicketPerDay.text.toString()+parkingArea!!.applicationTimeForParkingTicketPerDay
        binding.oneDayParkingTicketFee.text = binding.oneDayParkingTicketFee.text.toString()+parkingArea!!.oneDayParkingTicketFee
        binding.monthlyTicketFee.text = binding.monthlyTicketFee.text.toString()+parkingArea!!.monthlyTicketFee
        binding.paymentMethod.text = binding.paymentMethod.text.toString()+parkingArea!!.paymentMethod
    }
}

