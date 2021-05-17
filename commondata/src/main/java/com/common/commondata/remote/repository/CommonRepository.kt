package com.common.commondata.remote.repository

import android.content.Context
import com.common.commondata.dataSource.common.CommonDataFactory
import com.common.commondata.remote.model.UserModule
import com.common.commondomain.repositorys.ICommonRepository
import com.common.commondomain.interactor.login.LoginModel
import com.coredata.module.PreferenceModule
import com.coredomain.BaseVS
import com.coredomain.diinterfaces.AppContext
import io.reactivex.Single
import javax.inject.Inject


class CommonRepository @Inject constructor(
    @AppContext val context: Context,
    val userModule: UserModule,
    val preferenceModule: PreferenceModule,
    val commonDataFactory: CommonDataFactory
) : ICommonRepository {
    override fun login(params: LoginModel?): Single<BaseVS> {
        return commonDataFactory.retrieveDataSource().login(params!!)
    }
}


