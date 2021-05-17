package com.coreui.ui.fragment.list

import android.os.Bundle
import android.view.MenuItem
import com.coreui.ui.activity.DefaultBaseActivity
import com.trippl3dev.listlibrary.implementation.PrettyList
import com.trippl3dev.listlibrary.implementation.RecyclerListIm
abstract class BaseActivityList : DefaultBaseActivity(), IBaseList{

    var listHolder: RecyclerListIm? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheContent()
        addList()
    }


    open fun setTheContent() {
        setContentView(getContentViewId())
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun addList() {
        PrettyList.get(supportFragmentManager)
                .init()
                .setVM(getVMClassName())
                .putListInContainerWithId(getListContainerId())
                .addListener(object : PrettyList.ListReadyCallbak {
                    override fun onListReady(baseListVM: RecyclerListIm?) {
                        listHolder = baseListVM
                        listHolder?.setListVMCallback(this@BaseActivityList)
                        onListReady()
                    }
                })
    }


    abstract fun onListReady()
    abstract fun getContentViewId(): Int
    abstract fun getVMClassName(): String
    abstract fun getListContainerId(): Int

}

