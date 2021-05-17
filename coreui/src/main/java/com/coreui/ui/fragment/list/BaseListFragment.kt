package com.coreui.ui.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.coreui.ui.fragment.DefaultBaseFragment
import com.trippl3dev.listlibrary.implementation.PrettyList
import com.trippl3dev.listlibrary.implementation.RecyclerListIm
abstract class BaseListFragment : DefaultBaseFragment(), IBaseList{

    var listHolder: RecyclerListIm? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getTheContentView(inflater,container)
    }

    open fun getTheContentView(inflater: LayoutInflater, container: ViewGroup?):View{
             return inflater.inflate(getContentViewId(),container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addList()
    }





    fun addList() {
        PrettyList.get(childFragmentManager)
                .init()
                .setVM(getVMClassName())
                .putListInContainerWithId(getListContainerId())
                .addListener(object : PrettyList.ListReadyCallbak {
                    override fun onListReady(baseListVM: RecyclerListIm?) {
                        listHolder = baseListVM
                        listHolder?.setListVMCallback(this@BaseListFragment)
                        onListReady()
                    }
                })
    }


    abstract fun onListReady()
    abstract fun getContentViewId(): Int
    abstract fun getVMClassName(): String
    abstract fun getListContainerId(): Int

}

