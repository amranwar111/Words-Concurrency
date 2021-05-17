package com.common.commondata.dataSource.common


import com.common.commondomain.interactor.login.LoginModel
import com.common.commondomain.interactor.words.WordsClassResult

import com.coredomain.BaseVS
import io.reactivex.Single
import okhttp3.ResponseBody

interface ICommonRemote {
    fun login(params: LoginModel): Single<BaseVS>
    fun getWords(): Single<WordsClassResult>
}