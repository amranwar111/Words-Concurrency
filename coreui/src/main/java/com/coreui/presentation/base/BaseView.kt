package com.coreui.presentation.base

import com.coredomain.BaseVS
import io.reactivex.Observable

interface BaseView<I : BaseIntent, in S : BaseVS> {
    fun intents(): Observable<I>

    fun render(state: S)
}