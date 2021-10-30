package com.example.myparkingapp.navigation.bookmark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparkingapp.R
import com.example.myparkingapp.databinding.ActivityBookmarkBinding
import com.example.myparkingapp.parkingarea.ParkingAreaInfoActivity

/*
주차장명
주소
전화번호
가격
 */
class BookmarkActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBookmarkBinding
    private lateinit var adapter : BookmarkAdapter
    private val viewModel : BookmarkViewModel by lazy {
        ViewModelProvider(this).get(BookmarkViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bookmark)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = BookmarkAdapter(ArrayList<BookmarkData>())
        viewModel.getBookmarkData()
    }
    override fun onResume() {
        super.onResume()

        viewModel.bookmarkDatas.observe(this, Observer {
            updateUI(it)
        })
        viewModel.parkingArea.observe(this, Observer {
            if(it.numberOfParkingLots!=null){
                val intent = Intent(this, ParkingAreaInfoActivity::class.java)
                intent.putExtra("parkingAreaData", it)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun updateUI(items : ArrayList<BookmarkData>){
        adapter = BookmarkAdapter(items)
        binding.recyclerView.adapter = adapter
    }

    inner class BookmarkHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.bookmarkName)
        val address : TextView = itemView.findViewById(R.id.bookmarkAddress)
        val phone : TextView = itemView.findViewById(R.id.bookmarkPhone)
        val fee : TextView = itemView.findViewById(R.id.bookmarkFee)
        var bookmarkData : BookmarkData? = null

        fun bind(item : BookmarkData){
            bookmarkData = item
            name.text = item.name.toString()
            address.text = item.address.toString()
            phone.text = item.phone.toString()
            fee.text = item.fee.toString()
        }
    }

    inner class BookmarkAdapter(val items:ArrayList<BookmarkData>) : RecyclerView.Adapter<BookmarkHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkHolder {
            val view = layoutInflater.inflate(R.layout.bookmark_item, parent, false)
            return BookmarkHolder(view)
        }

        override fun onBindViewHolder(holder: BookmarkHolder, position: Int) {
            holder.bind(items.get(position))
        }

        override fun getItemCount(): Int {
            return items.size
        }

    }
}