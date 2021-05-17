package com.coreui.ui.fragment

import androidx.fragment.app.Fragment
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable

open class DefaultBaseFragment :  Fragment(){
    val compositeDisposable = CompositeDisposable()

}