package com.common.commondata.remote.model.response.profile

import com.google.gson.annotations.SerializedName


 open class UserProfileResponseModel {
    @SerializedName("birth_year")
    var birthYear: Int? = null
    @SerializedName("country")
    var country: Country? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("email_verified")
    var emailVerified: Int? = null
    @SerializedName("gender")
    var gender: String? = null
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("image")
    var image: String? = null
    @SerializedName("thumb_image")
    var thumb_image: String? = null
    @SerializedName("name")
    var name: String? = null

    @SerializedName("balance")
    var balance: String? = null
    @SerializedName("has_free")
    var hasFree: Int? = null
    @SerializedName("free_payed")
    var freePayed : Int? = null
    @SerializedName("debt")
    var debt: Int? = null

    @SerializedName("nationality_id")
    var nationalityId: String? = null
    @SerializedName("phone")
    var phone: String? = null
    @SerializedName("rate")
    var rate: Int? = null
    @SerializedName("isOnline")
    var isOnline: Boolean? = null
    @SerializedName("offer_id")
    var offerId: Int? = null

    var userValidData:Boolean? = null
    get()  {
       return email?.isNullOrEmpty()?.not()?:false
    }
}
