package com.common.commondomain.interactor.main.search

import com.common.commondomain.interactor.main.about.news.model.ModelRequest.NewsListModelRequest
import com.coredomain.BaseVS
import com.coredomain.definteractor.SingleUseCase
import com.coredomain.diinterfaces.excuter.PostExecutionThread
import com.coredomain.diinterfaces.excuter.ThreadExecutor
import com.user.userdomain.IUserRepository
import com.user.userdomain.model.ModelResult.NewsListModelResult
import io.reactivex.Single
import javax.inject.Inject

class NewListUsecase @Inject constructor(
    val repository: IUserRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : SingleUseCase<newsListClassResult, NewsListModelRequest>(threadExecutor, postExecutionThread) {
    override fun buildUseCaseObservable(params: NewsListModelRequest?): Single<newsListClassResult> {
//        aboutAppUsecase
//            .execute()
//            .map { it as BaseVS }
//            .onErrorReturn { BaseVS.Error(it) }
//            .subscribe({},{})
        return repository.newsList(params!!)
    }

    companion object {
        val name_ = 422
    }


}

class newsListClassResult(val model: NewsListModelResult) : BaseVS
