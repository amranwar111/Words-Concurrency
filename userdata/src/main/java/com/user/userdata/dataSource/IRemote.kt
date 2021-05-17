package com.user.userdata.dataSource

import com.common.commondomain.interactor.main.about.news.model.ModelRequest.NewsListModelRequest
import com.common.commondomain.interactor.main.search.newsListClassResult
import com.coredomain.BaseVS
import io.reactivex.Single


interface IRemote {

    fun newsList(model: NewsListModelRequest): Single<newsListClassResult>
  }