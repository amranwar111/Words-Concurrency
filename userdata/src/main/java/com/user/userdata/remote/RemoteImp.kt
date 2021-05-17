package com.user.userdata.remote

import com.common.commondomain.interactor.main.about.news.model.ModelRequest.NewsListModelRequest
import com.common.commondomain.interactor.main.search.newsListClassResult
import com.coredata.module.PreferenceModule
import com.coredomain.MyRemoteException
import com.google.gson.Gson
import com.user.userdata.dataSource.IRemote
import com.user.userdata.getMessage
import io.reactivex.Single
import javax.inject.Inject

class RemoteImp @Inject constructor(
    val service: UserRemoteService, val preferenceModule: PreferenceModule,
    val gson: Gson
) : IRemote {

    override fun newsList(model: NewsListModelRequest): Single<newsListClassResult> {
        return service.newsList(model.page!!, model.country_id!!)
            .map {
                if (it.isSuccessful) {
//                    BaseVS.Success().apply { this.message = "" }
                    newsListClassResult(it.body()!!)
                } else {
                    throw MyRemoteException(it.code(), it.errorBody()?.getMessage() ?: "")
                }
            }
    }



}