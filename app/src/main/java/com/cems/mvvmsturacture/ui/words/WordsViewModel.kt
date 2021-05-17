package com.cems.mvvmsturacture.ui.words

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coredomain.BaseVS
import com.coreui.presentation.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class WordsViewModel @ViewModelInject internal constructor(
    private val wordsProcessor: WordsProcessor
) : ViewModel(), BaseViewModel<WordsIntent, BaseVS> {


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

    private var intentsSubject: PublishSubject<WordsIntent> = PublishSubject.create()
    private val statesSubject: Observable<BaseVS> = compose()


    private fun compose(): Observable<BaseVS> {
        return intentsSubject
            .compose(wordsProcessor.myActionProcessor)
            .replay(1)
            .autoConnect(0)
    }

    override fun processIntents(intents: Observable<WordsIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<BaseVS> {
        return statesSubject
    }
}