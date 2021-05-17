package com.coreui.ui.fragment.list

import com.trippl3dev.listlibrary.interfaces.IListCallback

interface IBaseList : IListCallback{
    fun accept(page:Int)
    fun onItemSelected(position:Int,model:BaseListModel){}
}