package com.marketer.marketerdata.remote

import com.marketer.marketerdata.dataSource.IRemote
import com.coredata.module.PreferenceModule
import com.google.gson.Gson
import javax.inject.Inject

class RemoteImp @Inject constructor(
    val service: MarketerRemoteService, val preferenceModule: PreferenceModule,
    val gson: Gson
) : IRemote {

//    override fun register(model: ConsultationUserModel?): Single<BaseVS.Success> {
//        return service.register(
//            ConsultationRegisterRequest(
//                model?.academyImage,
//                model?.academySpecialId,
//                model?.bio,
//                model?.birthYear,
//                model?.consultCost,
//                model?.consultingTypes?.map { it?.id },
//                model?.countryId,
//                model?.email,
//                model?.gender,
//                model?.image,
//                model?.lawDegreeId,
//                model?.licensesImage,
//                model?.licensesNo,
//                model?.name,
//                model?.password,
//                model?.phone,
//                model?.socialId,
//                model?.socialType
//            )
//        ).map {
//            if (it.isSuccessful) {
//                preferenceModule.saveToken("")
//                BaseVS.Success().apply {
//                    this.message = ""
////                    this.code = it.body()?.code
//                }
//            } else {
//                throw MyRemoteException(it.code(), it.errorBody().toString() ?: "")
//            }
//        }
//    }
}