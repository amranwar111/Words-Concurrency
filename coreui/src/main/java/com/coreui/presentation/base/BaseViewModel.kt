package com.coreui.presentation.base

import com.coredomain.BaseVS
import io.reactivex.Observable

interface BaseViewModel<I : BaseIntent, S : BaseVS> {
    fun processIntents(intents: Observable<I>)

    fun states(): Observable<S>
}