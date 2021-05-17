package com.common.commondata.remote.model.response.profile

import com.google.gson.annotations.SerializedName

data class LawDegree(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name",alternate = ["title"])
    val name: String,
    @SerializedName("updated_at")
    val updatedAt: String
)