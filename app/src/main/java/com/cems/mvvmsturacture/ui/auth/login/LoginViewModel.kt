package com.cems.mvvmsturacture.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.hilt.lifecycle.ViewModelInject
import com.coredomain.BaseVS
import com.coreui.presentation.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject



class LoginViewModel @ViewModelInject internal constructor(
    private val loginProcessor: LoginProcessor
) : ViewModel(), BaseViewModel<LoginIntent, BaseVS> {


    private val loginError_ = MutableLiveData<String>()
    val loginError: LiveData<String>
        get() = loginError_

    private val loginEvent_ = MutableLiveData<Boolean>()
    val loginEvent: LiveData<Boolean>
        get() = loginEvent_

    init {
        loginError_.value = ""
        loginEvent_.value = false
    }


    fun loginEvent() {
        loginEvent_.value = true
    }

    fun loginEventComplete() {
        loginEvent_.value = false
    }

    fun loginError(msg: String) {
        loginError_.value = msg
    }

    fun loginErrorComplete() {
        loginError_.value = ""
    }


    val userLoginModel = UserLoginModel()
    var userLoginModelList: List<UserLoginModel> = listOf()
    private var intentsSubject: PublishSubject<LoginIntent> = PublishSubject.create()
    private val statesSubject: Observable<BaseVS> = compose()


    private fun compose(): Observable<BaseVS> {
        return intentsSubject
            .compose(loginProcessor.actionProcessor)
            .replay(1)
            .autoConnect(0)
    }

    override fun processIntents(intents: Observable<LoginIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<BaseVS> {
        return statesSubject
    }

    fun setUsers() {
        val userLoginModelList: ArrayList<UserLoginModel> = arrayListOf()
        val userLoginModel = UserLoginModel()
        userLoginModel.email = ";alkdljs"
        userLoginModel.password = ";l;sdjkl"
        userLoginModel.phone = "22"
        for (i in 0..5) {
//            userLoginModel.phone = i.toString() + "22"
            userLoginModelList.add(userLoginModel)
        }
        this.userLoginModelList = userLoginModelList
    }
}