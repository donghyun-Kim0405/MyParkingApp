package com.example.myparkingapp.board

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.myparkingapp.R
import com.example.myparkingapp.databinding.FragmentBoardDetailBinding


class BoardDetailFragment : Fragment() {

    companion object{
        public fun getInstance(boardModel:BoardModel) : BoardDetailFragment{
            val bundle = Bundle().apply {
                putSerializable("parkingLotNumber", boardModel.parkingLotNumber)
                putSerializable("title", boardModel.title)
                putSerializable("content", boardModel.content)
                putSerializable("email", boardModel.email)
                putSerializable("time", boardModel.time)
            }
            return BoardDetailFragment().apply { arguments = bundle }
        }
    }

    lateinit var binding : FragmentBoardDetailBinding
    var title : String? = null
    var parkingLotNumber : String? = null
    var content : String? = null
    var time : String? = null
    var email : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString("title")
        time = arguments?.getString("time")
        email = arguments?.getString("email")
        content = arguments?.getString("content")
        parkingLotNumber = arguments?.getString("parkingLotNumber")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_detail, container, false)

        binding.textTitle.text = title
        binding.textContent.text = content
        binding.textEmail.text = email
        binding.textTime.text = time

        return binding.root
    }
}