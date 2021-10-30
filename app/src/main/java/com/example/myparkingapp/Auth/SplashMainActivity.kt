package com.example.myparkingapp.Auth

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.myparkingapp.main.MainActivity
import com.example.myparkingapp.main.ParkingAppDBHelper
import com.example.myparkingapp.R
import com.example.myparkingapp.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

/*
internal uuid == null일 경우 앱을 introactivity로 이동 시켜야함
 */



class SplashMainActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"
    private lateinit var parkingAppDBHelper : ParkingAppDBHelper
    private var sqlRead : SQLiteDatabase? = null
    private var cursor: Cursor?=null
    private lateinit var requestAuthAPI: RequestAuthAPI
    private lateinit var context: Context
    private var internalUUID:String? = null
    private var internalID:String? = null
    private var token:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_main)
        context=this
        parkingAppDBHelper = ParkingAppDBHelper(this)
        sqlRead = parkingAppDBHelper.readableDatabase
        //dropDatabase()


        if(getInternalUserData()==null){
            Handler().postDelayed({
                startActivity(Intent(context, IntroActivity::class.java))
                finish()
            }, 2000)
        }else{
            getAuthFromServer()
        }
    }

    private fun dropDatabase(){
        val sqlWriter = parkingAppDBHelper.writableDatabase
        sqlWriter.execSQL("DROP TABLE IF EXISTS userTBL;")

    }

    private fun getInternalUserData() : String?{

        try{
            cursor = sqlRead?.rawQuery("SELECT uuid, id, token FROM userTBL", null)
            cursor!!.moveToNext()

            internalUUID = cursor!!.getString(0).toString()
            internalID = cursor!!.getString(1).toString()
            Log.e("InternalID", internalID.toString())
            token = cursor!!.getString(2).toString()

            sqlRead!!.close()
        }catch (e:Exception){
            Log.e(TAG, e.message.toString())
            sqlRead!!.close()
        }
        return token
    }

    private fun getAuthFromServer(){

        val retrofit = Retrofit.Builder().baseUrl("http://13.209.7.225:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        requestAuthAPI = retrofit.create(RequestAuthAPI::class.java)
        val call = requestAuthAPI.getAuthCall(AuthData(token))

        call.enqueue(object: Callback<ResponseData>{
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if(response.body()?.code==200){ //요청 성공 & 인증 성공

                    UserData.getInstance()?.apply { login(token, internalID) }  //요청 & 인증 성공 singleton유저 정보 저장
                    Log.e("InternalID", internalID.toString())
                    Handler().postDelayed({
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 2000)
                }else{  //요청은 성공하였으나 response code가 200이 아닌경우 intro창으로 이동
                    Handler().postDelayed({
                        startActivity(Intent(context, IntroActivity::class.java))
                        finish()
                    }, 2000)
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {//인증에 실패한 경우
                Log.e("result of login", t.message.toString())
                Toast.makeText(applicationContext, "server 접속 실패", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    startActivity(Intent(context, IntroActivity::class.java))
                    finish()
                }, 2000)
            }
        })
    }

}