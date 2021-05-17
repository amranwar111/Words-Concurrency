package com.user.userdata

import com.google.gson.Gson
import okhttp3.ResponseBody
import java.lang.Exception

fun ResponseBody.getMessage():String{
    val messageText = this.string()
    try {
        val model: RemoteResponse = Gson().fromJson(messageText, RemoteResponse::class.java)
        return model.message ?: ""
    }catch (e:Exception){
        return messageText
    }
}
fun ResponseBody.getMessage(model: RemoteResponse):String{
    return model.message?:""
}