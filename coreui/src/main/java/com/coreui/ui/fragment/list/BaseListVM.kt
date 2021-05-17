package com.coreui.ui.fragment.list

import android.content.Context

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.coreui.R
import com.trippl3dev.listlibrary.implementation.FullListVM
import com.trippl3dev.listlibrary.implementation.MyLinearLayoutManager
import com.trippl3dev.listlibrary.implementation.SubBaseListVM

open class BaseListVM : SubBaseListVM<BaseListModel, IBaseList>() {
    override fun getViewId(type: Int): Int  = R.layout.item_selection_list
    override fun getLayoutManager(context: Context): RecyclerView.LayoutManager
        = MyLinearLayoutManager(context,RecyclerView.VERTICAL,false) as RecyclerView.LayoutManager

    override fun fetchData() {
        super.fetchData()
        listCallback.accept(currentPage)
    }
    override fun setPageStartIndex(): Int  = 1
    override fun hasSwipeToRefresh(): Boolean = true
    override fun onBindView(root: View?, position: Int) {
        super.onBindView(root, position)
        root?.setOnClickListener {
            val item = getListOp().getList()?.get(position)
            item?.selected = item?.selected?.not()
            listCallback.onItemSelected(position, item!!)
        }

    }

    override fun filterCondition(value: Any, it: BaseListModel): Boolean {
        return it.name?.contains(value.toString().toRegex())!!
    }
    override fun onRefresh() {
        val transactionListModel = ArrayList<BaseListModel>()
        resetData()
        getAdaptee().setList(transactionListModel)
        getAdaptee().getAdapter().notifyDataSetChanged()
        fetchData()
    }

}