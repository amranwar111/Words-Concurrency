package com.coreui.ui.fragment

import androidx.fragment.app.DialogFragment
import dagger.android.support.DaggerDialogFragment
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable

open class DefaultBaseDialogFragment :  DialogFragment() {
    val compositeDisposable = CompositeDisposable()

}