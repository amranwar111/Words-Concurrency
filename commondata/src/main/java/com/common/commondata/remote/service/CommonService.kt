package com.common.commondata.remote.service

import com.common.commondata.remote.model.request.*
import com.common.commondata.remote.model.response.*
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface CommonService {


    /***************** Login ******************/
    @POST("{user}/login")
    fun login(
        @Path("user") user: String,
        @Body model: LoginRequestModel
    ): Single<Response<LoginResponseModel>>


}

