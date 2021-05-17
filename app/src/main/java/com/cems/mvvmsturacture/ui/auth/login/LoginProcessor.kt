package com.cems.mvvmsturacture.ui.auth.login

import com.common.commondomain.interactor.login.LoginUsecase
import com.coredomain.BaseVS
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject
class LoginProcessor @Inject constructor(private var loginUseCase: LoginUsecase) {

    private val loginProcessor: ObservableTransformer<LoginIntent.loginIntent, BaseVS> =
        ObservableTransformer { it ->
            it.switchMap {
                loginUseCase.execute(it.loginModel)
                    .toObservable()
                    .map { BaseVS.Success() as BaseVS }
                    .onErrorReturn { e -> BaseVS.Error(e) }
                    .startWith(BaseVS.Loading())
            }
        }

    var actionProcessor: ObservableTransformer<LoginIntent, BaseVS>

    init {
        this.actionProcessor = ObservableTransformer { it ->
            it.publish {
                it.ofType(LoginIntent.loginIntent::class.java)
                    .compose(loginProcessor)
                    .mergeWith(it.filter { a -> a !is LoginIntent }
                        .flatMap {
                            Observable.error<BaseVS>(
                                IllegalArgumentException("Unknown Action type")
                            )
                        })
            }
        }
    }


}