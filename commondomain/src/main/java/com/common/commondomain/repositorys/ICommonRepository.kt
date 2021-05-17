package com.common.commondomain.repositorys

import com.common.commondomain.interactor.login.LoginModel
import com.coredomain.BaseVS
import io.reactivex.Single

open interface ICommonRepository {
    fun login(params: LoginModel?): Single<BaseVS>


}