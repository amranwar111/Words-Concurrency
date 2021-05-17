package com.common.commondata.dataStore


import com.common.commondomain.interactor.login.LoginModel
import com.coredomain.BaseVS
import io.reactivex.Single

interface ICommonDataStore {
    fun login(params: LoginModel): Single<BaseVS>

}