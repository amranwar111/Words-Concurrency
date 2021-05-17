package com.trippl3dev.listlibrary


//import android.arch.lifecycle.MutableLiveData
//import android.arch.lifecycle.Observer
//import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
//import android.support.v4.app.Fragment
//import android.support.v4.widget.SwipeRefreshLayout
//import android.support.v7.util.DiffUtil
//import RecyclerView
//import SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.trippl3dev.listlibrary.implementation.FullListVM
import com.trippl3dev.listlibrary.implementation.PrettyList
import com.trippl3dev.listlibrary.implementation.RecyclerListIm
import com.trippl3dev.listlibrary.implementation.VMFactory
import com.trippl3dev.listlibrary.interfaces.IAdaptee
import com.trippl3dev.listlibrary.interfaces.IListCallback
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*


class GenericListK : Fragment(), RecyclerListIm.ListCallbackFunctionality,IEmpty {
    override fun getAdaptee(): IAdaptee<Any>? {
        return fullListVM?.getAdaptee()
    }

    override fun getState(): Int {
        return adapter.getState()
    }

    override fun getScrollListener(): RecyclerView.OnScrollListener {
        return fullListVM?.getScrollListener(recyclerView.layoutManager!!)!!
    }

    override fun setState(state: Int) {
        adapter.setState(state)
    }

    override fun resetVMData() {
        fullListVM?.resetData()
    }

    override fun setListVMCallback(listCallback: IListCallback) {
        fullListVM?.setIListCallback(listCallback)
    }

    override fun filter(value: Any):Boolean {
        val list  = fullListVM?.filter(value)!!
        fullListVM?.getAdaptee()?.setList(list)
        adapter.notifyDataSetChanged()
        return list.isEmpty()
    }

    override fun swapperCallback(className: String) {
        currentVMclassName = className
        fullListVM?.getLiveDataList()?.removeObservers(this)
        fullListVM?.getListOp()?.setList(ArrayList())
        fullListVM?.getAdaptee()?.setList(ArrayList())
        resetVMData()
        fullListVM = null
        disposable?.dispose()
        fullListVM = ViewModelProvider(this,VMFactory()).get<FullListVM<Any,Any,IListCallback>>(getVMClass(className)!!)
//        fullListVM?.setBundle(arguments?.getBundle(Bundle))
        prepareData()
        recyclerCallback?.value?.operation = fullListVM?.getListOp()!!
        recyclerView.clearOnScrollListeners()
        prepareList()
        listReadyCallback?.onListReady(recyclerCallback?.value)
        fullListVM?.fetchData()

    }

    private val listStateKey = "recycler_list_state"

    override fun getRecycler(): RecyclerView {
        return recyclerView
    }

    private var currentVMclassName : String = ""
    private lateinit var bundle: PrettyList.ListBundle
    private  var fullListVM: FullListVM<Any,Any,IListCallback>? = null
    private var listReadyCallback: PrettyList.ListReadyCallbak? = null
    private lateinit var adapter: BaseAdapter<Any>
    private var disposable: Disposable? = null
    private var recyclerCallback: MutableLiveData<RecyclerListIm>? = MutableLiveData()
    private lateinit var recyclerView:RecyclerView
    //    private lateinit var refreshLayout:SwipeRefreshLayout
//    private lateinit var container: ViewGroup
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        binding = DataBindingUtil.inflate(inflater,R.layout.list_layout_view,container,false)

//        this.container = container!!
        if (savedInstanceState == null)
            getBundle()
        fullListVM = ViewModelProvider(this,VMFactory()).get<FullListVM<Any,Any,IListCallback>>(getVMClass(currentVMclassName)!!)
        lifecycle.addObserver(fullListVM!!)
        if(savedInstanceState != null)
            listState = savedInstanceState.getParcelable(listStateKey)
        val view:View?
        if(fullListVM?.hasSwipeToRefresh()!!) {
            view = inflater.inflate(R.layout.list_layout_view, container, false)
            this.recyclerView = view.findViewById(R.id.list)
            val  refreshLayout: SwipeRefreshLayout = view.findViewById(R.id.refreshLayout)
            refreshLayout.isEnabled = true
            refreshLayout.setOnRefreshListener {
                resetVMData()
                fullListVM?.onRefresh()
                refreshLayout.isRefreshing = false
            }
        }else{
            view = inflater.inflate(R.layout.list_layout_view2, container, false)
            this.recyclerView = view.findViewById(R.id.list)
        }

        prepareData()
        retainInstance = true
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.refreshLayout.

//        fullListVM?.setBundle(arguments?.getBundle(Bundle))
        prepareList()
        if (listReadyCallback != null) {
            listReadyCallback!!.onListReady(recyclerCallback?.value)
//            fullListVM?.resetData()
//            Handler().postDelayed({
//                this.fullListVM?.fetchData()
//            },500)
        }
    }

    private var isEmpty = false;
    override fun setEmpty(isEmpty: Boolean?) {
//        recyclerView.visibility = if(isEmpty!!)View.GONE else View.VISIBLE
//        val error  = PreferenceModule.getInstance(context!!).errorId
//        if(isEmpty) {
//            val errorView :View
//            if (error != -1) {
//                errorView = LayoutInflater.from(context).inflate(error!!, container, false)
//            }else{
//                errorView = TextView(context).apply { text = "Empty" }
//            }
//                if (fullListVM?.hasSwipeToRefresh()!!) {
//                    errorView.layoutParams = refreshLayout.layoutParams
//                } else {
//                    errorView.layoutParams = recyclerView.layoutParams
//                }
//                errorView.id = 555
//                container.addView(errorView)
//                this.isEmpty = true
//        }else{
//            val errorView  = container.findViewById<View>(555)
//            container.removeView(errorView)
//            this.isEmpty = false
//        }
    }

    override fun isEmpty(): Boolean? {
        return isEmpty
    }
    override fun onResume() {

        super.onResume()
        if(recyclerCallback?.value?.getLayoutManager() != null)
            recyclerView.layoutManager = recyclerCallback?.value?.getLayoutManager()
        if (listState != null && recyclerView.layoutManager !=null) {
            recyclerView.layoutManager!!.onRestoreInstanceState(listState)
            if(fullListVM?.hasLoadMore()!! && !fullListVM?.isInNestedScroll()!!){
                recyclerView.clearOnScrollListeners()
                recyclerView.addOnScrollListener(fullListVM?.getScrollListener(recyclerView.layoutManager!!)!!)
            }
        }
    }

    private fun prepareData(){
        fullListVM?.getLiveDataList()?.observe(viewLifecycleOwner, Observer<ArrayList<Any>> { objects ->
            val oldList: ArrayList<Any> = fullListVM?.getAdaptee()?.getList()?.let { ArrayList(it) }!!
            fullListVM?.getAdaptee()?.setList(ArrayList(objects))
            disposable = Flowable.fromArray<ArrayList<Any>>(objects)
                .map<DiffUtil.DiffResult> { it -> DiffUtil.calculateDiff(GenericlistDiffUtils(oldList, it)) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread(),true)
                .subscribe { diffResult ->
                    if (oldList.size <= 0) {
                        if(fullListVM?.hasLoadMore()!! && !fullListVM?.isInNestedScroll()!!){
                            recyclerView.clearOnScrollListeners()
                            recyclerView.addOnScrollListener(fullListVM?.getScrollListener(recyclerView.layoutManager!!)!!)
                        }
//                            Handler().postDelayed({
                        adapter.notifyDataSetChanged()
//                            },500)

//                            diffResult.dispatchUpdatesTo(adapter)

                    } else {
                        recyclerView.recycledViewPool.clear()
//                            adapter.notifyDataSetChanged()
                        diffResult.dispatchUpdatesTo(adapter)
                    }
                }
        })
        if (listState == null) {
            recyclerCallback?.value = RecyclerListIm(this, fullListVM?.getListOp()!!)
            recyclerCallback?.value?.setLayoutManager(this.fullListVM?.getListLayoutManager(context!!)!!)
        }

    }

    private fun prepareList() {
        if (fullListVM?.attachSnapHelper()!!) {
//            recyclerView.onFlingListener = null
            fullListVM?.getSnapHelper()
                ?.attachToRecyclerView(recyclerView)


        }
        adapter = BaseAdapter(fullListVM?.getAdaptee(), context)
        adapter.setIEmpty(this)
        recyclerView.adapter = adapter



        if(bundle.list != null)
            fullListVM?.getListOp()?.setList(bundle.list!!)
        if(recyclerCallback?.value?.getLayoutManager() != null)
            recyclerView.layoutManager = recyclerCallback?.value?.getLayoutManager()
    }
    private fun getBundle() {
        bundle = arguments!!.getSerializable(dataKey) as PrettyList.ListBundle
        currentVMclassName = bundle.className


    }

    @Suppress("UNCHECKED_CAST")
    private fun getVMClass(name: String): Class<FullListVM<Any,Any,IListCallback>>? {
        val clazz: Class<*>?
        try {
            clazz = Class.forName(name)

            return clazz as Class<FullListVM<Any,Any,IListCallback>>?
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }catch (e:Exception){
            e.printStackTrace()
        }

        return null
    }

    fun setListReadyCallback(callback: PrettyList.ListReadyCallbak) {
        this.listReadyCallback = callback

    }


    companion object {
        const val dataKey = "dataKey"
        val Bundle: String = "Bundle"
    }

    override fun onStop() {
        disposable?.dispose()
        recyclerView.layoutManager = null
        super.onStop()
    }

    private var listState: Parcelable? = null
    override fun onSaveInstanceState(outState: Bundle) {
        if (recyclerView.layoutManager == null){
            super.onSaveInstanceState(outState)
            return
        }
        listState =  recyclerView.layoutManager?.onSaveInstanceState()
        outState.putParcelable(listStateKey,listState)
        super.onSaveInstanceState(outState)


    }
}

interface  IEmpty{
    fun setEmpty(isEmpty: Boolean?)
    fun isEmpty():Boolean?
}