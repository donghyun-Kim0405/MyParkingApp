package com.example.myparkingapp.board

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparkingapp.R
import com.example.myparkingapp.databinding.FragmentBoardMainBinding


class BoardMainFragment : Fragment() {
    companion object{
        public fun getInstance(parkingLotNumber:String?):BoardMainFragment{
            val bundle = Bundle().apply {
                putSerializable("parkingLotNumber", parkingLotNumber)
            }

            return BoardMainFragment().apply {
                arguments = bundle
            }
        }
    }
    interface Callbacks{
        fun onWriteIconSelected()
        fun onReviewSeleceted(boardModel : BoardModel)
    }

    private val TAG = "BoardMainFragment"
    var parkingLotNumber:String? = null
    lateinit var binding : FragmentBoardMainBinding
    private var callbacks : BoardMainFragment.Callbacks? = null
    lateinit var boardAdapter: BoardAdapter
    private val viewModel : BoardMainViewModel by lazy {
        ViewModelProvider(this).get(BoardMainViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parkingLotNumber = arguments?.getString("parkingLotNumber")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_main, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        boardAdapter = BoardAdapter(ArrayList<BoardModel>())
        binding.recyclerView.adapter = boardAdapter

        binding.btnImage.setOnClickListener {
            Log.e("BOARDACTIVITY", "ONWRITEICONSELECTED CALLED")
            callbacks?.onWriteIconSelected()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getReviews(parkingLotNumber)
        viewModel.reviews.observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })

    }


    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


    private fun updateUI(items : ArrayList<BoardModel>){
        binding.recyclerView.adapter = BoardAdapter(items)

    }
    inner class BoardHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.item_title)
        val content = itemView.findViewById<TextView>(R.id.item_content)
        val email = itemView.findViewById<TextView>(R.id.item_email)
        val time = itemView.findViewById<TextView>(R.id.item_time)
        lateinit var item : BoardModel

        init {
            itemView.setOnClickListener {
                Log.e(TAG, "Click Listener Called")
                callbacks?.onReviewSeleceted(item)
            }
        }

        public fun bind(_item : BoardModel){
            item = _item
            title.text = _item.title
            content.text = _item.content
            time.text = _item.time
            email.text = _item.email
        }
    }

    inner class BoardAdapter(val items : ArrayList<BoardModel>) : RecyclerView.Adapter<BoardHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardHolder {
            val view = layoutInflater.inflate(R.layout.board_item, parent, false)
            return BoardHolder(view)
        }
        override fun onBindViewHolder(holder: BoardHolder, position: Int) {
            holder.bind(items.get(position))
        }
        override fun getItemCount(): Int {
            return items.size
        }
    }
}