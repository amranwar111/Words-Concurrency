package com.common.commondata.remote.model.response

import com.common.commondata.remote.model.response.profile.ConsultationProfileResponseModel
import com.google.gson.annotations.SerializedName

data class LoginResponseModel(
    @SerializedName("token")
    val token: String
) : GRemoteResponse<ConsultationProfileResponseModel>()