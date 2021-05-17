package com.user.userdata.dataSource

import com.common.commondomain.interactor.main.about.news.model.ModelRequest.NewsListModelRequest
import com.common.commondomain.interactor.main.search.newsListClassResult
import com.user.userdata.remote.IDataStore
import io.reactivex.Single
import javax.inject.Inject

class RemoteDataStore @Inject constructor(val iRemote: IRemote) :
    IDataStore
{
    override fun newsList(model: NewsListModelRequest): Single<newsListClassResult> {
        return iRemote.newsList(model)
    }
}