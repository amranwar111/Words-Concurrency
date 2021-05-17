package com.user.userdata.remote

import com.user.userdomain.model.ModelResult.NewsListModelResult
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface UserRemoteService {

    @GET("news")
    fun newsList(
        @Query("page") page: Int
        , @Header("country") country: Int
    ): Single<Response<NewsListModelResult>>

}