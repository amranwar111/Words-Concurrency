package com.coreui.ui.fragment


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.coredomain.BaseVS
import com.coreui.CoreApp
import com.coreui.presentation.base.BaseIntent
import com.coreui.presentation.base.BaseViewModel
import com.coreui.ui.activity.DefaultBaseActivity
import com.coreui.utils.navigateSafe
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

abstract class BaseDialogFragment<VM, I> :
    DefaultBaseDialogFragment() where VM : ViewModel, I : BaseIntent {

    //    @Inject
//    lateinit var factory: ViewModelFactory
    var navStack: ArrayList<NavDirections> = arrayListOf()
    lateinit var navControl: NavController
    lateinit var vm: VM
    abstract fun intents(): io.reactivex.Observable<I>
    abstract fun getFragmentVM(): Class<out VM>
    abstract fun render(state: BaseVS)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[getFragmentVM()]
        compositeDisposable.add((vm as BaseViewModel<I, BaseVS>).states().subscribe { render(it) })
        (vm as BaseViewModel<I, BaseVS>).processIntents(intents())
    }


    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }


}