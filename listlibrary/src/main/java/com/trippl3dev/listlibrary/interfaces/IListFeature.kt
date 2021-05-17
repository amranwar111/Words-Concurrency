
package com.trippl3dev.listlibrary.interfaces

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView


interface IListFeature {

    fun setHaFixedSize(fixed: Boolean)
    fun setListVMCallback(listCallback:IListCallback)


    fun changeVM(className: String)
    fun resetData()
    fun setRecyclerListener(listener: RecyclerView.RecyclerListener)

    fun addOnChildAttachStateChangeListener(listener: RecyclerView.OnChildAttachStateChangeListener)

    fun clearOnChildAttachStateChangeListeners()

    fun addItemDecorator(decorator: RecyclerView.ItemDecoration)


    fun setLayoutManager(layout: RecyclerView.LayoutManager)

    fun getLayoutManager(): RecyclerView.LayoutManager?

    fun setOnFlingListener(onFlingListener: RecyclerView.OnFlingListener?)


    fun setRecycledViewPool(pool: RecyclerView.RecycledViewPool)

    fun addItemDecoration(decor: RecyclerView.ItemDecoration)

    fun addOnScrollListener(listener: RecyclerView.OnScrollListener)

    fun clearOnScrollListeners()

    fun scrollToPosition(position: Int)

    fun smoothScrollToPosition(position: Int)

    fun stopScroll()

    fun addOnItemTouchListener(listener: RecyclerView.OnItemTouchListener)

    fun removeOnItemTouchListener(listener: RecyclerView.OnItemTouchListener)

    fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean)

    fun setItemAnimator(animator: RecyclerView.ItemAnimator)

//    fun enableSnapeHelper()
    fun filter( value:Any):Boolean
    fun setState(state:Int)
    fun getState():Int
    fun setScrollView(scrollView: NestedScrollView)
    fun scrollTo(x: Int, y: Int)
    fun getAdaptee():IAdaptee<Any>?
}