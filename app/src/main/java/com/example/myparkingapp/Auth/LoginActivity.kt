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
import androidx.databinding.DataBindingUtil
import com.example.myparkingapp.main.MainActivity
import com.example.myparkingapp.main.ParkingAppDBHelper
import com.example.myparkingapp.R
import com.example.myparkingapp.UserData
import com.example.myparkingapp.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    private lateinit var requestAuthAPI: RequestAuthAPI
    private lateinit var context: Context
    private lateinit var parkingAppDBHelper : ParkingAppDBHelper
    private var sqlWrite:SQLiteDatabase? = null
    private var sqlRead : SQLiteDatabase? = null
    private var cursor: Cursor?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        context=this

        parkingAppDBHelper = ParkingAppDBHelper(context)
        sqlWrite = parkingAppDBHelper.writableDatabase
        sqlRead = parkingAppDBHelper.readableDatabase

        val retrofit = Retrofit.Builder().baseUrl("http://13.209.7.225:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        requestAuthAPI = retrofit.create(RequestAuthAPI::class.java)

        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val passwd = binding.editPasswd.text.toString()
            var joinFlag=true

            if(email.isEmpty()){
                Toast.makeText(this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                joinFlag=false
            }else if(passwd.isEmpty()){
                Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                joinFlag=false
            }

            if(joinFlag){
                val call = requestAuthAPI.getLoginCall(LoginData(email, passwd))
                call.enqueue(object : Callback<ResponseData> {
                    override fun onResponse(
                        call: Call<ResponseData>,
                        response: Response<ResponseData>
                    ) {
                        val code:String? = response.body()?.code.toString()
                        if(code.equals("200")){
                            Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()

                            val uuid:String?=response.body()?.uuid.toString()
                            val email:String?=response.body()?.email.toString()
                            val token:String?=response.body()?.token.toString()
                            updateInternalDatabase(uuid, email, token)// 내부 데이터베이스에 login정보 저장

                            Log.e("result of login", email.toString())

                            UserData.getInstance()?.apply { login(token, email) } //요청 & 안증 성공 -> singleton유저 정보 저장 .

                            Handler().postDelayed({
                                val intent = Intent(context, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }, 1000)
                        }else{
                            Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                        Log.e("result of login", t.message.toString())
                        Toast.makeText(applicationContext, "server 접속 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    fun updateInternalDatabase(uuid:String?, id:String?, token:String?){    //internal db에 token column추가필요

        sqlWrite!!.execSQL("CREATE TABLE IF NOT EXISTS userTBL(uuid char(36), id varchar(30), token text);")
        cursor=sqlRead!!.rawQuery("SELECT COUNT(*) FROM userTBL", null)
        cursor!!.moveToNext()
        var internalUUID = cursor!!.getString(0).toString()
        if(internalUUID.equals("0")){
            sqlWrite!!.execSQL("INSERT INTO userTBL VALUES('${uuid}', '${id}', '${token}');")    //내부 데이터베이스에 서버로 부터 받은 uuid저장 -> splash에서 서버로 유효성 검사 수행
            Log.e("INSERT INTO OPERATE", uuid.toString())
        }else{
            Log.e("UPDATE CALLED", token.toString())
            sqlWrite!!.execSQL("UPDATE userTBL SET uuid='${uuid}';")    //내부 데이터베이스에 서버로 부터 받은 uuid저장 -> splash에서 서버로 유효성 검사 수행
            sqlWrite!!.execSQL("UPDATE userTBL SET id='${id}';")
            sqlWrite!!.execSQL("UPDATE userTBL SET token='${token}';")
        //내부 데이터베이스에 서버로 부터 받은 uuid저장 -> splash에서 서버로 유효성 검사 수행
        }
    }


}