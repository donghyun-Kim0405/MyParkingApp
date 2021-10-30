package com.example.myparkingapp.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.myparkingapp.R
import com.example.myparkingapp.databinding.ActivityBoardBinding
import com.example.myparkingapp.databinding.FragmentBoardWriteBinding

class BoardActivity : AppCompatActivity(), BoardMainFragment.Callbacks , BoardWriteFragment.Callbacks{

    lateinit var binding : ActivityBoardBinding
    private var parkingLotNumber : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board)

        parkingLotNumber = intent.getStringExtra("parkingLotNumber")

        if(supportFragmentManager.findFragmentById(R.id.fragment_container)==null){
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, BoardMainFragment.getInstance(parkingLotNumber)).commit()
        }
    }

    override fun onWriteIconSelected() {
        val fragment = BoardWriteFragment.getInstance(parkingLotNumber)
        Log.e("BOARDACTIVITY", "ONWRITEICONSELECTED CALLED")
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("board").commit()
    }

    override fun onBtnWriteSelected() {
        val fragment = BoardMainFragment.getInstance(parkingLotNumber)
        supportFragmentManager.popBackStack()
    }

    override fun onReviewSeleceted(boardModel:BoardModel) {
        val fragment = BoardDetailFragment.getInstance(boardModel)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("board").commit()
    }
}
