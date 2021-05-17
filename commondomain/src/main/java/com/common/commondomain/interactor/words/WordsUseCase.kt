package com.common.commondomain.interactor.words

import com.common.commondomain.repositorys.ICommonRepository
import com.coredomain.BaseVS
import com.coredomain.definteractor.SingleUseCase
import com.coredomain.diinterfaces.excuter.PostExecutionThread
import com.coredomain.diinterfaces.excuter.ThreadExecutor
import io.reactivex.Single
import okhttp3.ResponseBody
import javax.inject.Inject

class WordsUseCase @Inject constructor(val repository: ICommonRepository,
                                       threadExecutor: ThreadExecutor,
                                       postExecutionThread: PostExecutionThread
): SingleUseCase<WordsClassResult, Unit>(threadExecutor,postExecutionThread) {

    override fun buildUseCaseObservable(params: Unit?): Single<WordsClassResult> {
        return repository.getWords()
    }
}

class WordsClassResult(val model: ResponseBody): BaseVS