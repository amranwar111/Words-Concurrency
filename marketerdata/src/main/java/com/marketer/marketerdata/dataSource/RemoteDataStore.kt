package com.marketer.marketerdata.dataSource

import com.marketer.marketerdata.IDataStore
import javax.inject.Inject

class RemoteDataStore @Inject constructor(val iRemote: IRemote) : IDataStore {

//    override fun register(params: ConsultationUserModel?): Single<BaseVS.Success> {
//        return iRemote.register(params)
//    }
}