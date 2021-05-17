package com.marketer.marketerdata

import com.marketer.marketerdata.dataSource.Factory
import com.marketer.marketerdomain.IMarketerRepository
import javax.inject.Inject

class RepositoryImp @Inject constructor(val factory: Factory) : IMarketerRepository {


//    override fun register(params: ConsultationUserModel?): Single<BaseVS.Success> {
//        return factory.retrieveDataSource().register(params)
//    }
}