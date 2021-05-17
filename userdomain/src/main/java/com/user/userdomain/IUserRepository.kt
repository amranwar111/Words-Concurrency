package com.user.userdomain

import com.common.commondomain.interactor.main.about.news.model.ModelRequest.NewsListModelRequest
import com.common.commondomain.interactor.main.search.newsListClassResult
import com.coredomain.BaseVS
import com.coredomain.model.BaseListResult
import io.reactivex.Single

 interface IUserRepository {
     fun newsList(model: NewsListModelRequest): Single<newsListClassResult>
}