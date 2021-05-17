package com.coreui.ui.activity


import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coredomain.BaseVS
import com.coreui.presentation.base.BaseIntent
import com.coreui.presentation.base.BaseViewModel
import javax.inject.Inject


abstract class BaseActivity<T, I> : DefaultBaseActivity() where T : ViewModel, I : BaseIntent {


//    @Inject
//    lateinit var factory: ViewModelFactory
    lateinit var vm: T

    abstract fun intents(): io.reactivex.Observable<I>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[getActivityVM()]
//        vm.viewState.observeForever { render(it) }
// compositeDisposable stop render
        compositeDisposable.add((vm as BaseViewModel<I, BaseVS>).states().subscribe { render(it) })
        (vm as BaseViewModel<I, BaseVS>).processIntents(intents())
    }

    //     abstract fun render(it: BaseVS?)
    abstract fun getActivityVM(): Class<out T>

    abstract fun render(state: BaseVS)

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }


}