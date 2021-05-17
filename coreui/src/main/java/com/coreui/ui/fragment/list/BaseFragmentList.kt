package com.coreui.ui.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.coredomain.BaseVS
import com.coredomain.model.BaseListResult
import com.coreui.R
import com.coreui.ui.fragment.BaseFragment
import com.trippl3dev.listlibrary.implementation.PrettyList
import com.trippl3dev.listlibrary.implementation.RecyclerListIm
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.lang.Exception

class BaseFragmentList<T> : BaseFragment<T, BaseListIntent>(), IBaseList where T:ViewModel{
    override fun onItemSelected(position: Int, model: BaseListModel) {

    }


    companion object {
        val LISTVM = "LISTVM"
        val LISTVIEWVM = "LISTVIEWVM"
        val CONTAINERID = "CONTAINERID"
        val LAYOUTID = "LAYOUTID"
        val SELECTEDID = "SELECTEDID"
        fun <K: ViewModel> getInstance(
            classNameVM: String,
            designVM: String = BaseListVM::class.java.name,
            containerId: Int = R.id.container,
            layoutId: Int = R.layout.fragment_list_container,
            selectedId: Int = -1
        ): BaseFragmentList<K> {
            val fragment = BaseFragmentList<K>()
            fragment.arguments = Bundle().apply {
                this.putString(LISTVM, classNameVM)
                this.putString(LISTVIEWVM, designVM)
                this.putInt(CONTAINERID, containerId)
                this.putInt(LAYOUTID, layoutId)
                this.putInt(SELECTEDID, selectedId)
            }
            return fragment
        }
    }

    private val listPublisher = BehaviorSubject.create<BaseListIntent.ListBaseIntent>()

    override fun intents(): Observable<BaseListIntent> {
        return listPublisher as Observable<BaseListIntent>
    }


    override fun accept(page: Int) {
        listPublisher.onNext(BaseListIntent.ListBaseIntent(page))
    }

    override fun getFragmentVM(): Class<out T> {
        val className = arguments?.getString(LISTVM, null)
        var clazz: Class<out T>? = null
        try {
            clazz = className?.let { Class.forName(it) } as Class<out T>
        } catch (e: Exception) {

        } finally {
            return clazz!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_list_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addList()
    }

    private var listHolder: RecyclerListIm? = null

        fun addList() {
            PrettyList.get(childFragmentManager)
                .init()
                .setVM(arguments?.getString(LISTVIEWVM, null) ?: BaseListVM::class.java.name)
                .putListInContainerWithId(arguments?.getInt(CONTAINERID) ?: R.id.container)
                .addListener(object : PrettyList.ListReadyCallbak {
                    override fun onListReady(baseListVM: RecyclerListIm?) {
                        listHolder = baseListVM
                        listHolder?.setListVMCallback(this@BaseFragmentList)
                    }
                })
        }



    override fun render(state: BaseVS) {
        when (state) {
            is BaseVS.Loading -> { }
            is BaseVS.Error -> { }
            is BaseListResult -> {
                listHolder?.operation?.addList(state.items.map {
                    BaseListModel().apply {
                        this.id = it.id
                        this.name = it.name
                        this.image = it.image
                        this.selected = it.id == arguments?.getInt(SELECTEDID)?:-1
                    }
                })
            }
        }
    }
}