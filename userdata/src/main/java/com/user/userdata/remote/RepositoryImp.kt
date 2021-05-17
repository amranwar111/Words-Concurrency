package com.user.userdata.remote

import com.common.commondomain.interactor.main.about.news.model.ModelRequest.NewsListModelRequest
import com.common.commondomain.interactor.main.search.newsListClassResult
import com.coredomain.BaseVS
import com.coredomain.model.BaseListResult
import com.user.userdata.dataSource.UserDataFactory
import com.user.userdomain.IUserRepository
import io.reactivex.Single
import javax.inject.Inject

class RepositoryImp @Inject constructor(val factory: UserDataFactory): IUserRepository {

    override fun newsList(model: NewsListModelRequest): Single<newsListClassResult> {
        return factory.retrieveDataSource().newsList(model)  }


}