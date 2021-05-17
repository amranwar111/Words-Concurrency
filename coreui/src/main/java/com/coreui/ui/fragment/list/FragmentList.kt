package com.coreui.ui.fragment.list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.coredomain.BaseVS
import com.coreui.R
import com.coreui.ui.fragment.BaseFragment
import com.tripl3dev.prettystates.StatesConstants
import com.tripl3dev.prettystates.setState
import com.trippl3dev.listlibrary.implementation.PrettyList
import com.trippl3dev.listlibrary.implementation.RecyclerListIm
import com.trippl3dev.listlibrary.interfaces.States
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

abstract class FragmentList<V, I> : BaseFragment<V, I>(), IBaseList where V : ViewModel, I : BaseListIntent {
    private val intentRelay = BehaviorSubject.create<BaseListIntent.ListBaseIntent>()
    var listHolder: RecyclerListIm? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getTheContentView(inflater, container)
    }

    open fun getTheContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(getContentViewId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addList()
    }

    open fun getIntentList(): Observable<I> {
        return Observable.merge(intentRelay, retryIntent) as Observable<I>
    }


    val retryIntent = BehaviorSubject.create<BaseListIntent.ListBaseIntent>()
    open fun retryIntent(): Observable<I> {
        return retryIntent as Observable<I>
    }

    override fun render(baseVS: BaseVS) {
        when (baseVS) {
            is BaseVS.Loading -> {

                if (baseVS.type == 0)
                    if (listHolder?.operation?.getList()?.isEmpty()!!) {
                        view?.findViewById<View>(getListContainerId())?.setState(StatesConstants.LOADING_STATE)
                    } else {
                        listHolder?.setState(States.LOADING)
                    }
            }
            is BaseVS.Error -> {
                if (baseVS.type == 0)
                    if (listHolder?.operation?.getList()?.isEmpty()!!) {
                        view?.findViewById<View>(getListContainerId())?.setState(StatesConstants.ERROR_STATE)
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
                        setEmptyView().setOnClickListener {
                            retryIntent.onNext(BaseListIntent.ListBaseIntent(getCurrentPage()))
                        }

                    } else
                        listHolder?.setState(States.NORMAL)
            }
            else -> {
                view?.findViewById<View>(getListContainerId())?.setState(StatesConstants.NORMAL_STATE)
                listHolder?.setState(States.NORMAL)
                renderResult(baseVS)
            }
        }
    }

    open fun onErrorClicked() {
        retryIntent.onNext(BaseListIntent.ListBaseIntent(getCurrentPage()))
    }

    fun setEmptyView(): View {
        val emptyView = view?.findViewById<View>(getListContainerId())?.setState(StatesConstants.ERROR_STATE)
        val textView = emptyView?.findViewById<TextView>(R.id.text)
        textView?.text = getEmptyMessage()
//        textView?.setBackgroundColor(Color.RED)
        return emptyView!!
    }


    override fun onDestroy() {
        view?.findViewById<View>(getListContainerId())?.setState(StatesConstants.NORMAL_STATE)
        super.onDestroy()
    }

    fun addList() {
        PrettyList.get(childFragmentManager)
            .init()
            .setVM(getVMClassName())
            .putListInContainerWithId(getListContainerId())
            .addListener(object : PrettyList.ListReadyCallbak {
                override fun onListReady(baseListVM: RecyclerListIm?) {
                    listHolder = baseListVM
                    listHolder?.setListVMCallback(this@FragmentList)
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

