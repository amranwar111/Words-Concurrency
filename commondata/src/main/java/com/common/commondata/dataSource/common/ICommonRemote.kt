package com.common.commondata.dataSource.common


import com.common.commondomain.interactor.login.LoginModel

import com.coredomain.BaseVS
import io.reactivex.Single

interface ICommonRemote {
    fun login(params: LoginModel): Single<BaseVS>

}