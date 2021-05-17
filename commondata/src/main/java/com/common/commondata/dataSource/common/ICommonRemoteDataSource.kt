package com.common.commondata.dataSource.common

import com.common.commondata.dataStore.ICommonDataStore
import com.common.commondomain.interactor.login.LoginModel
import com.coredomain.BaseVS
import io.reactivex.Single
import javax.inject.Inject


class ICommonRemoteDataSource @Inject constructor(val commonRemote: ICommonRemote) :
    ICommonDataStore {


    override fun login(params: LoginModel): Single<BaseVS> {
        return commonRemote.login(params)
    }


}