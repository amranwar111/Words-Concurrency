package com.common.commondata.remote.model

import com.common.commondata.remote.model.response.profile.ConsultationProfileResponseModel
import com.common.commondata.remote.model.response.profile.UserProfileResponseModel
import com.coredata.module.PreferenceModule
import com.google.gson.Gson
import javax.inject.Inject

class UserModule @Inject constructor(
    private val preferenceModule: PreferenceModule,
    private val gson: Gson
) {

    fun getConsultation(): ConsultationProfileResponseModel? {
        if (preferenceModule.getUser() == null)return null
        return gson.fromJson<ConsultationProfileResponseModel>(
            preferenceModule.getUser(),
            ConsultationProfileResponseModel::class.java
        )
    }

    fun getUser(): UserProfileResponseModel {
        return gson.fromJson<UserProfileResponseModel>(preferenceModule.getUser(), UserProfileResponseModel::class.java)
    }
}