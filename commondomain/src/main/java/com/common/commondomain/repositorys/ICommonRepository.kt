package com.common.commondomain.repositorys

import com.common.commondomain.interactor.login.LoginModel
import com.common.commondomain.interactor.words.WordsClassResult
import com.coredomain.BaseVS
import io.reactivex.Single

open interface ICommonRepository {
    fun login(params: LoginModel?): Single<BaseVS>
    fun getWords(): Single<WordsClassResult>
}