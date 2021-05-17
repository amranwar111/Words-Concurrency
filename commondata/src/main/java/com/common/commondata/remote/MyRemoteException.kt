package com.common.commondata.remote

import com.common.commondata.remote.model.response.RemoteResponse
import com.coredomain.MyRemoteException
import com.google.gson.Gson
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
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
fun ResponseBody.getMessage(model:RemoteResponse):String{
    return model.message?:""
}

//fun <T> Single<Response<*>>.checkIfAuthorized(refresh:Single<*>):Single<T>{
//    this.retryWhen (refresh)
//    } }
//    this.flatMap {
//        if (it.code() == 401 || it.code() == 403){
//            return refresh.flatMap { it-> observable}
//        }else{
//            return Single.just(T)
//        }
//    }
//}