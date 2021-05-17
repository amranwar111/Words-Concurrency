package com.common.commondata.remote.model.response.profile

import com.google.gson.annotations.SerializedName

class ConsultationProfileResponseModel : UserProfileResponseModel() {
    @SerializedName("academy_image")
    var academyImage: String? = null
    @SerializedName("bio")
    var bio: String? = null
    @SerializedName("consult_cost")
    var consultCost: Int? = null
    @SerializedName("consultant_academic")
    var consultantAcademic: ConsultantAcademic? = null
    @SerializedName("consulting_types")
    var consultingTypes: List<ConsultingType>? = null
    @SerializedName("law_degree")
    var lawDegree: LawDegree? = null
    @SerializedName("licenses_image")
    var licensesImage: String? = null
    @SerializedName("licenses_no")
    var licensesNo: String? = null

}