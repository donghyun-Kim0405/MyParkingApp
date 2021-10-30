package com.example.myparkingapp.main

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.myparkingapp.*
import com.example.myparkingapp.Auth.ResponseData
import com.example.myparkingapp.board.BoardModel
import com.example.myparkingapp.board.BoardWriteFragment
import com.example.myparkingapp.board.RequestBoardAPI
import com.example.myparkingapp.location.LocationRestAPI
import com.example.myparkingapp.parkingarea.ParkingArea
import com.example.myparkingapp.parkingarea.ParkingAreaRequestAPI
import com.example.myparkingapp.location.PostLocation
import com.example.myparkingapp.navigation.bookmark.BookmarkData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainRepository private constructor(context:Context){

    companion object{
        private var INSTANCE: MainRepository?=null   //Singleton패턴을 위한 INSTANCE변수

        fun init(context: Context){
            if(INSTANCE ==null){
                INSTANCE = MainRepository(context)   //MainActivity가 onCreate될경우 호출
            }

        }
        fun getInstance() : MainRepository?{  //ViewModel에서 객체를 얻기위해 호출
            return INSTANCE ?:
            throw Exception("Repository sould be initialized")
        }
    }
    private val TAG = "REPOSITORY"
    private val BASE_URL = "https://dapi.kakao.com/"
    private val RESTAPI_KEY=""
    private val userData = UserData.getInstance()


    private var parkingAppDBHelper = ParkingAppDBHelper(context)
    private var sqlWrite:SQLiteDatabase? = null
    private var sqlRead : SQLiteDatabase? = null
    private var cursor : Cursor? = null

    fun logout(){
        sqlWrite = parkingAppDBHelper.writableDatabase
        sqlWrite!!.execSQL("UPDATE userTBL SET token=NULL;")    //내부 데이터베이스에 서버로 부터 받은 uuid저장 -> splash에서 서버로 유효성 검사 수행
    }

    public fun getPlaceData(target : String, mutableLiveData: MutableLiveData<Location>){
        val retrofit = Retrofit.Builder() // Retrofit 구성
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(LocationRestAPI::class.java)
        val call = api.getPlaceInfo(RESTAPI_KEY, target)

        call.enqueue(object : Callback<ResultPlaceInfo> {
            override fun onResponse(
                call: Call<ResultPlaceInfo>,
                response: Response<ResultPlaceInfo>
            ) {
                // 통신 성공 (검색 결과는 response.body()에 담겨있음)
                Log.d("Test", "Raw: ${response.raw()}")
                Log.d("Test", "Body: ${response.body()}")
                if(response.body()!=null){
                    var body : ResultPlaceInfo = response.body()!!

                    if(body.documents!=null&&body.documents.size!=0){
                        var result=body.documents.get(0)
                        var location = Location("current")

                        location.longitude=result.x.toDouble()
                        location.latitude=result.y.toDouble()
                        mutableLiveData.value = location

                        Log.e("result", result.toString())

                    }
                    /*for(info in body.documents){
                        mapView.addPOIItem(createMarker(info.y.toDouble(), info.x.toDouble()))
                    }*/
                }
            }

            override fun onFailure(call: Call<ResultPlaceInfo>, t: Throwable) {
                // 통신 실패
                Log.w("MainActivity", "통신 실패: ${t.message}")
            }
        })
    }

    fun getParkingAreaInfo(location : Location, parkingAreaData: MutableLiveData<ArrayList<ParkingArea>>){
        val retrofit = Retrofit.Builder().baseUrl("http://13.209.7.225:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ParkingAreaRequestAPI::class.java)
        val call = api.getParkingAreaInfo(PostLocation(location.latitude.toString(), location.longitude.toString()))
        call.enqueue(object : Callback<ArrayList<ParkingArea>>{
            override fun onResponse(
                call: Call<ArrayList<ParkingArea>>,
                response: Response<ArrayList<ParkingArea>>
            ) {
                parkingAreaData.value = response.body()
            }

            override fun onFailure(call: Call<ArrayList<ParkingArea>>, t: Throwable) {
                Log.e("PARKINGAREA FAIL", t.message.toString())
            }
        })
    }

    fun writeBoard(boardModel: BoardModel, callbacks: BoardWriteFragment.Callbacks?, context:Context) {
        val retrofit = Retrofit.Builder().baseUrl("http://13.209.7.225:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(RequestBoardAPI::class.java)
        val call = api.getWriteBoardCall(boardModel)

        call.enqueue(object : Callback<ResponseData>{
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {

                val code = response.body()?.code
                if(code==200){
                    Toast.makeText(context, "작성완료.", Toast.LENGTH_SHORT).show()
                    callbacks?.onBtnWriteSelected()
                }else{
                    Toast.makeText(context, "로그인 상태를 확인해 주세요.", Toast.LENGTH_SHORT).show()
                }

            }
            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Toast.makeText(context, "네트워크 상태를 확인해주세요!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getReviews(parkingLotNumber:String?, mutableLiveData: MutableLiveData<ArrayList<BoardModel>>){
        val retrofit = Retrofit.Builder().baseUrl("http://13.209.7.225:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(RequestBoardAPI::class.java)
        val call = api.getBoardCall(BoardModel(parkingLotNumber!!))

        call.enqueue(object : Callback<ArrayList<BoardModel>>{
            override fun onResponse(
                call: Call<ArrayList<BoardModel>>,
                response: Response<ArrayList<BoardModel>>
            ) {
                mutableLiveData.value = response.body()
            }

            override fun onFailure(call: Call<ArrayList<BoardModel>>, t: Throwable) {

            }
        })
    }

    public fun getBookmarkData() : ArrayList<BookmarkData>{
        sqlRead = parkingAppDBHelper.readableDatabase
        cursor=sqlRead!!.rawQuery("SELECT name, address, phone, fee FROM bookmarkTBL;",null)
        val arr:ArrayList<BookmarkData> = ArrayList<BookmarkData>()
        while(cursor!!.moveToNext()){
            arr.add(BookmarkData(cursor!!.getString(0), cursor!!.getString(1),cursor!!.getString(2),cursor!!.getString(3)))
        }
        sqlRead!!.close()
        cursor = null
        return arr
    }
    public fun deleteBookmark(address : String?) : Boolean{
        sqlWrite = parkingAppDBHelper.writableDatabase
        sqlWrite!!.execSQL("DELETE FROM bookmarkTBL WHERE address = '${address}';")
        sqlWrite?.close()
        return false
    }
    public fun saveBookmark(bookMarkData:BookmarkData) : Boolean{// name address phone, fee
        sqlWrite = parkingAppDBHelper.writableDatabase
        sqlRead = parkingAppDBHelper.readableDatabase
        sqlWrite!!.execSQL("CREATE TABLE IF NOT EXISTS bookmarkTBL(name char(50), address varchar(50), phone varchar(20), fee varchar(10));")
        cursor = sqlRead!!.rawQuery("SELECT COUNT(*) FROM bookmarkTBL WHERE address='${bookMarkData.address}';", null)
        cursor!!.moveToNext()
        if(cursor!!.getString(0).toString().equals("0")){
            sqlWrite!!.execSQL("INSERT INTO bookmarkTBL VALUES('${bookMarkData.name}', '${bookMarkData.address}', '${bookMarkData.phone}','${bookMarkData.fee}');")
        }

        cursor = null
        sqlWrite?.close()
        sqlRead?.close()
        return true
    }

    public fun checkBookmark(address : String?) : Boolean{
        sqlRead = parkingAppDBHelper.readableDatabase
        cursor=sqlRead!!.rawQuery("SELECT COUNT(*) FROM bookmarkTBL WHERE address='${address}';",null)
        cursor!!.moveToNext()

        if(cursor!!.getString(0).toString().equals("0")){   //북마크 기록 없을경우 false-> icon_bookmark_unselected
           return false
        }

        cursor = null
        sqlRead?.close()
        return true
    }

}

