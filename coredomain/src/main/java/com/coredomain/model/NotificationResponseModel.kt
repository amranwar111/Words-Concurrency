package com.coredomain.model

import com.google.gson.annotations.SerializedName

data class NotificationResponseModel(
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("created_at_string")
    var createdAtString: String?, // 1 hour ago
    @SerializedName("from")
    var from: From?,
    @SerializedName("id")
    var id: Int?, // 175
    @SerializedName("consulting_id",alternate = ["consultation_id"])
    var consultingId: Int?, // 175
    @SerializedName("is_seen")
    var isSeen: Int?, // 0
    @SerializedName("message")
    var message: String?, // عرض جديد على الاستشارة
    @SerializedName("to")
    var to: To?,
    @SerializedName("type")
    var type: Int?, // 0
    @SerializedName("type_title")
    var typeTitle: String? // اضافة عرض جديد
) {

    @SerializedName("offer_id")
    var offerId: Int? = null // 175
    data class From(
        @SerializedName("id")
        var id: Int?, // 158
        @SerializedName("image")
        var image: Any?, // null
        @SerializedName("name")
        var name: String? // mahmoud Abdelaal
    )

    data class To(
        @SerializedName("id")
        var id: Int?, // 157
        @SerializedName("image")
        var image: String?, // /public/assets/uploads/154937322415493732245c598f282987e.jpg
        @SerializedName("name")
        var name: String? // mahmoud
    )

    data class CreatedAt(
        @SerializedName("date")
        var date: String?, // 2019-02-05 14:42:03.000000
        @SerializedName("timezone")
        var timezone: String?, // UTC
        @SerializedName("timezone_type")
        var timezoneType: Int? // 3
    )
}