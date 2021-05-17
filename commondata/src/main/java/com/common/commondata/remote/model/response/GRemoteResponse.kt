package com.common.commondata.remote.model.response

import com.google.gson.annotations.SerializedName

open class GRemoteResponse<T> : RemoteResponse() {
    @SerializedName("data")
    var data: T? = null
}