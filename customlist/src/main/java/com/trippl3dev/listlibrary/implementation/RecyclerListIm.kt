package com.trippl3dev.listlibrary.implementation


import com.trippl3dev.listlibrary.interfaces.IListCallback
import com.trippl3dev.listlibrary.interfaces.IListFeature
import com.trippl3dev.listlibrary.interfaces.IListOp
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.trippl3dev.listlibrary.interfaces.IAdaptee


open class RecyclerListIm(val holder: ListCallbackFunctionality, var operation: IListOp<Any>) : IListFeature {
    override fun getState(): Int {
        return holder.getState()
    }

    override fun getAdaptee(): IAdaptee<Any>?  = holder?.getAdaptee()




    override fun setState(state: Int) {
        holder.setState(state)
    }


    override fun setListVMCallback(listCallback: IListCallback) {
        holder.setListVMCallback(listCallback)
    }

    override fun addItemDecorator(decorator: RecyclerView.ItemDecoration) {
        holder.getRecycler().addItemDecoration(decorator)
    }

    override fun filter(value: Any):Boolean {
        return holder.filter(value)
    }

    private var manager: RecyclerView.LayoutManager? = null

    override fun setHaFixedSize(fixed: Boolean) {
        holder.getRecycler().setHasFixedSize(fixed)
    }

    override fun changeVM(className: String) {

        holder.swapperCallback(className)
    }

    override fun resetData() {
        holder.resetVMData()
    }


    override fun setRecyclerListener(listener: RecyclerView.RecyclerListener) {
        holder.getRecycler().setRecyclerListener(listener)
    }

    override fun addOnChildAttachStateChangeListener(listener: RecyclerView.OnChildAttachStateChangeListener) {
        holder.getRecycler().addOnChildAttachStateChangeListener(listener)
    }

    override fun clearOnChildAttachStateChangeListeners() {
        holder.getRecycler().clearOnChildAttachStateChangeListeners()
    }

    override fun setLayoutManager(layout: RecyclerView.LayoutManager) {
        manager = layout
        holder.getRecycler().layoutManager = manager
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager? {
        return manager ?: LinearLayoutManager(holder.getRecycler().context)
    }

    override fun setOnFlingListener(onFlingListener: RecyclerView.OnFlingListener?) {
        holder.getRecycler().onFlingListener = onFlingListener
    }

    override fun setRecycledViewPool(pool: RecyclerView.RecycledViewPool) {
//        holder.getRecycler().recycledViewPool = pool
    }

    override fun addItemDecoration(decor: RecyclerView.ItemDecoration) {
        holder.getRecycler().addItemDecoration(decor)
    }

    override fun addOnScrollListener(listener: RecyclerView.OnScrollListener) {
        holder.getRecycler().addOnScrollListener(listener)
    }

    override fun clearOnScrollListeners() {
        holder.getRecycler().clearOnScrollListeners()
    }

    override fun scrollToPosition(position: Int) {
        holder.getRecycler().scrollToPosition(position)

    }

    override fun scrollTo(x: Int, y: Int) {
        holder.getRecycler().scrollTo(x, y)
    }

    override fun smoothScrollToPosition(position: Int) {
        holder.getRecycler().smoothScrollToPosition(position)
    }


    override fun stopScroll() {
//        holder.getRecycler().stopScroll()
//        val params  =holder.getRecycler().layoutParams
//        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
//        holder.getRecycler().layoutParams = params
        ViewCompat.setNestedScrollingEnabled(holder.getRecycler(), false)
//        holder.getRecycler().recycledViewPool.setMaxRecycledViews(1, 0)


//        holder.getRecycler().invalidate()

    }

    override fun setScrollView(scrollView: NestedScrollView) {
        stopScroll()
        scrollView.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
        scrollView.isFocusable = true
        scrollView.isFocusableInTouchMode = true

//        scrollView.setOnTouchListener{ v, event ->
//            v.requestFocusFromTouch()
//            false
//        )
        scrollView.setLister(holder.getScrollListener(), holder.getRecycler())
    }

    override fun addOnItemTouchListener(listener: RecyclerView.OnItemTouchListener) {
        holder.getRecycler().addOnItemTouchListener(listener)
    }

    override fun removeOnItemTouchListener(listener: RecyclerView.OnItemTouchListener) {
        holder.getRecycler().removeOnItemTouchListener(listener)
    }

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        holder.getRecycler().requestDisallowInterceptTouchEvent(disallowIntercept)
    }

    override fun setItemAnimator(animator: RecyclerView.ItemAnimator) {
        holder.getRecycler().itemAnimator = animator
    }


    interface ListCallbackFunctionality {
        fun swapperCallback(className: String)
        fun getRecycler(): RecyclerView
        fun filter(value: Any):Boolean
        fun setListVMCallback(listCallback: IListCallback)
        fun resetVMData()
        fun setState(state: Int)
        fun getScrollListener(): RecyclerView.OnScrollListener
        fun getAdaptee():IAdaptee<Any>?
        fun getState(): Int
    }


    private fun NestedScrollView.setLister(listener: RecyclerView.OnScrollListener, recyclerView: RecyclerView?) {
        this.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener
        { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (recyclerView != null)
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
                        scrollY > oldScrollY) {

                    val visibleItemCount = recyclerView.layoutManager?.childCount ?:0
                    val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
                    var lastVisibleItemPosition = 0

                    if (recyclerView.layoutManager is StaggeredGridLayoutManager) {
                        val lastVisibleItemPositions = (recyclerView.layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                        // get maximum element within the list
                        lastVisibleItemPosition = (recyclerView.layoutManager as StaggeredGridLayoutManager).findLastCompletelyVisibleItemPositions(null)[0]
                    } else if (recyclerView.layoutManager is GridLayoutManager) {
                        lastVisibleItemPosition = (recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                    } else if (recyclerView.layoutManager is LinearLayoutManager) {
                        lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    }

                    if ((visibleItemCount?.plus(lastVisibleItemPosition)) >= totalItemCount) {
                        (listener as EndlessRecyclerViewScrollListener).onLoadMore(0, totalItemCount, recyclerView)
                    }
                }
            }
        })
    }
}