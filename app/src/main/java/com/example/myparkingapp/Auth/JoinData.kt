package com.example.myparkingapp.Auth

import com.google.gson.annotations.SerializedName

public class JoinData(_email : String, _password : String) {
    @SerializedName("email")
    private var email:String = _email

    @SerializedName("password")
    private var password:String = _password
}

public class LoginData (_email : String, _password : String){
    @SerializedName("email")
    private var email:String = _email

    @SerializedName("password")
    private var password:String = _password
}

public class AuthData(_token:String?){
    @SerializedName("token")
    var token:String? = _token
}

public class ResponseData {
    @SerializedName("code")
    var code:Int =0

    @SerializedName("uuid")
    var uuid:String? = null

    @SerializedName("message")
    var message:String?=null

    @SerializedName("email")
    var email:String? = null

    @SerializedName("token")
    var token : String?=null
}




