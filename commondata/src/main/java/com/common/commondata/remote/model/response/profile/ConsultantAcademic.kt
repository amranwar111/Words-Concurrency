package com.common.commondata.remote.model.response.profile

import com.google.gson.annotations.SerializedName

data class ConsultantAcademic(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("updated_at")
    val updatedAt: String
)