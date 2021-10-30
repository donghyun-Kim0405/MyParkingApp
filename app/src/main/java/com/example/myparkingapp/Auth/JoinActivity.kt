package com.example.myparkingapp.Auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myparkingapp.R
import com.example.myparkingapp.databinding.ActivityJoinBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JoinActivity : AppCompatActivity() {

    private lateinit var binding : ActivityJoinBinding
    private lateinit var requestAuthAPI: RequestAuthAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        val retrofit = Retrofit.Builder().baseUrl("http://13.209.7.225:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        requestAuthAPI = retrofit.create(RequestAuthAPI::class.java)

        binding.btnJoin.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val passwd = binding.editPasswd.text.toString()
            val check = binding.editCheck.text.toString()
            var joinFlag=true

            if(email.isEmpty()){
                Toast.makeText(this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                joinFlag=false
            }else if(passwd.isEmpty()){
                Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                joinFlag=false
            }else if(check.isEmpty()){
                Toast.makeText(this, "확인을 위한 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                joinFlag=false
            }else if(!passwd.equals(check)){
                Toast.makeText(this, "pass1과 passcheck가 서로 다릅니다.", Toast.LENGTH_SHORT).show()
                joinFlag=false
            }else if(passwd.length<6){
                Toast.makeText(this, "6자 이상의 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                joinFlag=false
            }

            if(joinFlag){
                val call = requestAuthAPI.getJoinCall(JoinData(email, passwd))
                call.enqueue(object : Callback<ResponseData> {
                    override fun onResponse(
                        call: Call<ResponseData>,
                        response: Response<ResponseData>
                    ) {
                        if(response.body()?.code.toString()=="200"){
                            Toast.makeText(applicationContext, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            Handler().postDelayed({
                                finish()
                            }, 1000)
                        }else{
                            Toast.makeText(applicationContext, "이미 존재하는 이메일 입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                        Log.e("result of join", t.message.toString())
                    }
                })
            }
        }
    }
}