package com.common.commondata.remote.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
    @SerializedName("country_id")
    val countryId: Int?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("phone")
    val phone: String?
)