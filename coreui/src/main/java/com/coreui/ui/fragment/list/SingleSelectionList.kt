package com.coreui.ui.fragment.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.coredomain.BaseVS
import com.coredomain.model.BaseListResult
import com.coredomain.model.ListItem
import com.coreui.R
import com.coreui.ui.activity.BaseActivity
import com.coreui.ui.fragment.DefaultBaseFragment
import com.coreui.utils.DimensionUtils
import com.coreui.utils.EqualSpacingItemDecoration
import com.trippl3dev.listlibrary.implementation.PrettyList
import com.trippl3dev.listlibrary.implementation.RecyclerListIm
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_list_container.*
import java.lang.Exception
open class SingleSelectionList<T> : ActivityList<T, BaseListIntent>(), IBaseList where  T : ViewModel {



    companion object {
        val ITEMDATA = "ITEMDATA"
        val LISTVM = "LISTVM"
        val LISTVIEWVM = "LISTVIEWVM"
        val CONTAINERID = "CONTAINERID"
        val LAYOUTID = "LAYOUTID"
        val SELECTEDID = "SELECTEDID"
        fun  open(
            context:Context,
             clazz: Class<*>,
            requestCode:Int,
            classNameVM: String,
            designVM: String = BaseListVM::class.java.name,
            containerId: Int = R.id.containerList,
            layoutId: Int = R.layout.fragment_list_container,
            selectedId: Int = -1
        ) {
            val intent = Intent(context,clazz)
            intent.putExtras( Bundle().apply {
                this.putString(LISTVM, classNameVM)
                this.putString(LISTVIEWVM, designVM)
                this.putInt(CONTAINERID, containerId)
                this.putInt(LAYOUTID, layoutId)
                this.putInt(SELECTEDID, selectedId)
            })
            (context as AppCompatActivity).startActivityForResult(intent,requestCode)
        }

        fun  open(
            fragment:DefaultBaseFragment,
            clazz: Class<*>,
            requestCode:Int,
            classNameVM: String,
            designVM: String = BaseListVM::class.java.name,
            containerId: Int = R.id.containerList,
            layoutId: Int = R.layout.fragment_list_container,
            selectedId: Int = -1
        ) {
            val intent = Intent(fragment.context,clazz)
            intent.putExtras( Bundle().apply {
                this.putString(LISTVM, classNameVM)
                this.putString(LISTVIEWVM, designVM)
                this.putInt(CONTAINERID, containerId)
                this.putInt(LAYOUTID, layoutId)
                this.putInt(SELECTEDID, selectedId)
            })
            fragment.startActivityForResult(intent,requestCode)
        }
    }


    override fun intents(): Observable<BaseListIntent> {
        return getIntentList() as Observable<BaseListIntent>
    }

    override fun onErrorClicked() {
        accept(getCurrentPage())
    }



    override fun getActivityVM(): Class<out T> {
        val className = intent?.extras?.getString(LISTVM, null)
        var clazz: Class<out T>? = null
        try {
            clazz = Class.forName(className!!) as Class<out T>
        } catch (e: Exception) {

        } finally {
            return clazz!!
        }
    }


    override fun onItemSelected(position: Int, model: BaseListModel) {
        if (model.selected!!) {
            select.isEnabled = true
            selectedId = model.id
            listHolder?.operation?.getList()?.map { it as BaseListModel }
                ?.map { it.selected = model.id == it.id }
        }else{
            select.isEnabled = false
        }
//        this.selectedModel = model

    }

    open fun getLayoutId():Int = R.layout.activity_list_container

    private var selectedId: Int? = 0

    open fun onSelect(item:BaseListModel?){
        this.selectedId = item?.id
        val intent = Intent()
        intent.putExtra(ITEMDATA,item)
        setResult(Activity.RESULT_OK,intent)
        finish()
    }
    override fun setTheContent() {
        super.setTheContent()
        select.setOnClickListener {
            val items = listHolder?.operation?.getList()?.map { item ->item as BaseListModel }
                ?.filter { item-> item.selected == true }
            if(items?.size?:0 > 0) {
                onSelect(items?.get(0))
            }
        }
        select?.isEnabled = intent?.extras?.getInt(SELECTEDID)?:-1 > 0
        buildView()
    }

    override fun onResume() {
        super.onResume()
        selectedId = getLastSelected()
    }

    open fun buildView(){
        search.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                listHolder?.filter(s.toString())
                clear.visibility = if (s?.toString()?.trim()?.length!! > 0) View.VISIBLE  else View.GONE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
        clear.setOnClickListener {
            search.setText("")
        }
        close.setOnClickListener {
            onBackPressed()
        }
    }





    fun mapToListModel(it:ListItem):BaseListModel{
        return BaseListModel().apply {
            this.id = it.id
            this.name = it.name
            this.image = it.image
            this.selected = isSelected(it.id)
            this.code = it.code
            this.description = it.description
        }
    }
    open fun isSelected(id:Int?): Boolean {
        return id == selectedId
    }
    fun getLastSelected():Int{
        return intent?.extras?.getInt(SELECTEDID)?:-1
    }


    override fun onListReady() {
        val verticalSpace = DimensionUtils.convertDPToPX(this@SingleSelectionList, 8)
        val mDividerItemDecoration = EqualSpacingItemDecoration(verticalSpace, LinearLayoutManager.VERTICAL)
        listHolder?.addItemDecoration(mDividerItemDecoration)
    }

    override fun getCurrentPage(): Int {
        return 0
    }

    override fun renderResult(state: BaseVS) {
        when (state) {
            is BaseListResult -> {
                listHolder?.operation?.addList(state.items.map {
                    mapToListModel(it)
                })
            }
        }
    }

    override fun getContentViewId(): Int {
       return getLayoutId()
    }

    override fun getVMClassName(): String {
       return intent?.extras?.getString(LISTVIEWVM, null) ?: BaseListVM::class.java.name
    }

    override fun getListContainerId(): Int {
        return intent?.extras?.getInt(CONTAINERID) ?: R.id.containerList
    }

    override fun getEmptyMessage(): String? {
        return "لا يوجد"
    }
}