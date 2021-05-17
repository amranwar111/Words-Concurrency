package com.common.commondata.remote.remoteImp

import com.common.commondata.dataSource.common.ICommonRemote
import com.common.commondata.remote.getMessage
import com.common.commondata.remote.model.request.*
import com.common.commondata.remote.service.CommonService
import com.common.commondomain.interactor.login.LoginModel
import com.common.commondomain.interactor.words.WordsClassResult
import com.coredata.module.PreferenceModule
import com.coredomain.BaseVS
import com.coredomain.MyRemoteException
import com.google.gson.Gson
import io.reactivex.Single

import javax.inject.Inject


class CommonRemoteImp @Inject constructor(
    val commonService: CommonService,
    val preferenceModule: PreferenceModule,
    val gson: Gson
) :
    ICommonRemote {
    var progress = "0"
    override fun login(model: LoginModel): Single<BaseVS> {
        return commonService.login(
            preferenceModule.getUserType(),
            LoginRequestModel(model.countryId, model.username, model.password, model.username)
        ).map {
            if (it.isSuccessful) {
                preferenceModule.saveUser(gson.toJson(it.body()?.data))
                preferenceModule.saveToken(it.body()?.token)
                BaseVS.Success().apply { this.message = it.body()?.message!! }
            } else {
                throw MyRemoteException(it.code(), it.errorBody()?.getMessage() ?: "")
            }
        }

//            .flatMap {
////            commonService.updatePlayerId(PlayerIdModel(preferenceModule.getPlayerId() ?: ""))
//        }.map {
//            if (it.isSuccessful) {
//                BaseVS.Success().apply { this.message = it.body()?.message!! }
//
//            } else {
//                BaseVS.Success()
//            }
        }

    override fun getWords(): Single<WordsClassResult> {
        return commonService.getWords()
            .map {
                if (it.isSuccessful) {
                    WordsClassResult(it.body()!!)
                } else {
                    throw MyRemoteException(it.code(), it.errorBody()?.getMessage() ?: "")
                }
            }
    }
}



//    fun <T> from(originalObservable: Observable<T>): Observable<T> {
//        return originalObservable
//            .retryWhen { t ->
//                t.zipWith(Observable.range(1, 1),
//                    BiFunction<Throwable, Int, Pair<Throwable, Boolean>> { t1, t2 ->
//                        if (t2 < 1 && t1 is MyRemoteException && t1.code == 401) {
//                            Log.d(TAG, "401 authorization error, try again")
//                            return@BiFunction Pair(t1, true)
//                        }
//                        Pair(t1, false)
//                    }).flatMap { t ->
//                    if (t.second) {
////                        refreshToken().toObservable()
//                        Observable.error<Any>(t.first)
//                    } else {
//                        Observable.error<Any>(t.first)
//                    }
//                }
//            }
//    }
//
//
//
//
//
//fun <T> Observable<T>.checkAuth(refresh: Single<*>): Observable<T> {
//    return this.retryWhen {
//        it.flatMap { throwable ->
//            if (throwable is MyRemoteException && throwable.code == 401) {
//                refresh.toObservable()
//            } else {
//                Observable.error<Any>(throwable)
//            }
//        }
//    }
//}

