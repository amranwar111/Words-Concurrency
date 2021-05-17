package com.common.commondata.remote.model.response.profile

import com.google.gson.annotations.SerializedName

data class ConsultingType(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String
)