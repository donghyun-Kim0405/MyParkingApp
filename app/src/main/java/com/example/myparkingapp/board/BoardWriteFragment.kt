package com.example.myparkingapp.board

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.myparkingapp.MapUtils
import com.example.myparkingapp.R
import com.example.myparkingapp.UserData
import com.example.myparkingapp.databinding.FragmentBoardMainBinding
import com.example.myparkingapp.databinding.FragmentBoardWriteBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class BoardWriteFragment : Fragment() {



    companion object{
        public fun getInstance(parkingLotNumber:String?) : BoardWriteFragment{

            val bundle = Bundle().apply{
                putSerializable("parkingLotNumber", parkingLotNumber)
            }
            return BoardWriteFragment().apply {
                arguments = bundle
            }
        }
    }

    interface Callbacks{
        public fun onBtnWriteSelected()
    }

    private lateinit var boardWriteViewModel : BoardWriteViewModel
    private var callbacks : BoardWriteFragment.Callbacks? = null
    private val TAG = "BoardWriteFragment"
    lateinit var binding : FragmentBoardWriteBinding
    private var userData : UserData? = null
    var parkingLotNumber : String? = null


    private val titleWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { boardWriteViewModel.title = p0.toString() }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { boardWriteViewModel.title = p0.toString() }
        override fun afterTextChanged(p0: Editable?) { boardWriteViewModel.title = p0.toString() } }

    private val contentWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { boardWriteViewModel.content = p0.toString() }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { boardWriteViewModel.content = p0.toString() }
        override fun afterTextChanged(p0: Editable?) { boardWriteViewModel.content = p0.toString() } }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parkingLotNumber = arguments?.getString("parkingLotNumber")
        Log.e(TAG, parkingLotNumber!!)
        userData = UserData.getInstance()
        boardWriteViewModel=ViewModelProvider(this).get(BoardWriteViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_write, container, false)
        binding.editTitle.addTextChangedListener(titleWatcher)
        binding.editContent.addTextChangedListener(contentWatcher)

        binding.btnWrite.setOnClickListener {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")
            val formatted = current.format(formatter)
            Log.e("BOARD WRITE", "CALLED")

            boardWriteViewModel.writeBoard(parkingLotNumber!!, formatted, callbacks, requireContext())    //need a time
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

}