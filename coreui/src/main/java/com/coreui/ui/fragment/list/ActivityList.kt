package com.coreui.ui.fragment.list

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.coredomain.BaseVS
import com.coreui.R
import com.coreui.ui.activity.BaseActivity
import com.tripl3dev.prettystates.StatesConstants
import com.tripl3dev.prettystates.setState
import com.trippl3dev.listlibrary.implementation.PrettyList
import com.trippl3dev.listlibrary.implementation.RecyclerListIm
import com.trippl3dev.listlibrary.interfaces.States
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

abstract class ActivityList<V, I> : BaseActivity<V, I>(), IBaseList where V : ViewModel, I : BaseListIntent {
    var msavedInstanceState: Bundle? = null
    val intentRelay = BehaviorSubject.create<BaseListIntent.ListBaseIntent>()
    var listHolder: RecyclerListIm? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (getContentViewId() >= 0)
        msavedInstanceState = savedInstanceState

        setTheContent()
        addList()
    }


    open fun setTheContent() {
        setContentView(getContentViewId())
    }

    fun getIntentList(): Observable<BaseListIntent.ListBaseIntent> {
        return intentRelay
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

    val retryIntent = BehaviorSubject.create<BaseListIntent.ListBaseIntent>()
    fun retryIntent(): Observable<BaseListIntent.ListBaseIntent> {
        return retryIntent
    }

    override fun render(baseVS: BaseVS) {
        when (baseVS) {
            is BaseVS.Loading -> {
                if (baseVS.type == 0)
                    if (listHolder?.operation?.getList()?.isEmpty()!!) {
                        findViewById<View>(getListContainerId())?.setState(StatesConstants.LOADING_STATE)
                    } else {
                        listHolder?.setState(States.LOADING)
                    }
            }
            is BaseVS.Error -> {
                if (baseVS.type == 0)
                    if (listHolder?.operation?.getList()?.isEmpty()!!) {
                        findViewById<View>(getListContainerId())?.setState(StatesConstants.ERROR_STATE)
                            ?.setOnClickListener {
                                onErrorClicked()
                            }
                    } else {
                        listHolder?.setState(States.ERROR)
                    }

            }
            is BaseVS.Empty -> {
                if (baseVS.type == 0)
                    if (listHolder?.operation?.getList()?.isEmpty()!!) {
                        setEmptyView()?.setOnClickListener {
                            retryIntent.onNext(BaseListIntent.ListBaseIntent(getCurrentPage()))
                        }
                    } else
                        listHolder?.setState(States.NORMAL)
            }
            else -> {
                findViewById<View>(getListContainerId())?.setState(StatesConstants.NORMAL_STATE)
                listHolder?.setState(States.NORMAL)
                renderResult(baseVS)
            }
        }
    }

    open fun onErrorClicked() {
        retryIntent.onNext(BaseListIntent.ListBaseIntent(getCurrentPage()))

    }

    fun setEmptyView(): View {
        val emptyView = findViewById<View>(getListContainerId())?.setState(StatesConstants.ERROR_STATE)
        val textView = emptyView?.findViewById<TextView>(R.id.text)
        textView?.text = getEmptyMessage()
//        textView?.setBackgroundColor(Color.RED)
        return emptyView!!
    }


    fun addList() {
        PrettyList.get(supportFragmentManager)
            .init()
            .setVM(getVMClassName())
            .putListInContainerWithId(getListContainerId())
            .addListener(object : PrettyList.ListReadyCallbak {
                override fun onListReady(baseListVM: RecyclerListIm?) {
                    listHolder = baseListVM
                    listHolder?.setListVMCallback(this@ActivityList)
                    onListReady()
                }
            })
    }

    override fun accept(currentPage: Int) {
        intentRelay.onNext(BaseListIntent.ListBaseIntent(currentPage))
    }


    abstract fun onListReady()
    abstract fun getCurrentPage(): Int
    abstract fun renderResult(baseVS: BaseVS)
    abstract fun getContentViewId(): Int
    abstract fun getVMClassName(): String
    abstract fun getListContainerId(): Int
    abstract fun getEmptyMessage(): String?


}

