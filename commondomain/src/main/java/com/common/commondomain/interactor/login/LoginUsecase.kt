package com.common.commondomain.interactor.login

import com.common.commondomain.repositorys.ICommonRepository
import com.coredomain.BaseVS
import com.coredomain.definteractor.SingleUseCase
import com.coredomain.diinterfaces.excuter.PostExecutionThread
import com.coredomain.diinterfaces.excuter.ThreadExecutor
import io.reactivex.Single
import javax.inject.Inject

class LoginUsecase @Inject constructor(val repository: ICommonRepository,
                                       threadExecutor: ThreadExecutor,
                                       postExecutionThread: PostExecutionThread
): SingleUseCase<BaseVS,LoginModel>(threadExecutor,postExecutionThread) {
    override fun buildUseCaseObservable(params: LoginModel?): Single<BaseVS> {
//        aboutAppUsecase
//            .execute()
//            .map { it as BaseVS }
//            .onErrorReturn { BaseVS.Error(it) }
//            .subscribe({},{})
        return repository.login(params)
    }
    companion object {
        val CODEparameterfaildvalidation =  422
        val CODElogindatainvalid =  401
        val CODEaccountnotactive =  405
        val CODEemailnotverified =  403
    }
}