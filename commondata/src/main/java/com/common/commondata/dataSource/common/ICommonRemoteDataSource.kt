package com.common.commondata.dataSource.common

import com.common.commondata.dataStore.ICommonDataStore
import com.common.commondomain.interactor.login.LoginModel
import com.common.commondomain.interactor.words.WordsClassResult
import com.coredomain.BaseVS
import io.reactivex.Single
import javax.inject.Inject


class ICommonRemoteDataSource @Inject constructor(val commonRemote: ICommonRemote) :
    ICommonDataStore {


    override fun login(params: LoginModel): Single<BaseVS> {
        return commonRemote.login(params)
    }

    override fun getWords(): Single<WordsClassResult> {
        return commonRemote.getWords()
    }


}