package com.coreui.ui.fragment.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.coreui.R
import com.coreui.ui.activity.DefaultBaseActivity
import com.coreui.ui.fragment.DefaultBaseFragment
import kotlinx.android.synthetic.main.activity_list_container_multiselection.*

open class MultiSelectionActivityList<T> : SingleSelectionList<T>() where T: ViewModel {
    companion object {
        val ITEMDATA = "ITEMDATA"
        val LISTVM = "LISTVM"
        val LISTVIEWVM = "LISTVIEWVM"
        val CONTAINERID = "CONTAINERID"
        val LAYOUTID = "LAYOUTID"
        val SELECTEDID = "SELECTEDID"
        fun  open(
            context: Context,
            clazz: Class<*>,
            requestCode:Int,
            classNameVM: String,
            designVM: String = BaseListVM::class.java.name,
            containerId: Int = R.id.containerList,
            layoutId: Int = R.layout.fragment_list_container,
            selectedId: ArrayList<Int> = ArrayList()
        ) {
            val intent = Intent(context,clazz)
            intent.putExtras( Bundle().apply {
                this.putString(LISTVM, classNameVM)
                this.putString(LISTVIEWVM, designVM)
                this.putInt(CONTAINERID, containerId)
                this.putInt(LAYOUTID, layoutId)
                this.putIntegerArrayList(SELECTEDID, selectedId)
            })
            (context as DefaultBaseActivity).startActivityForResult(intent,requestCode)
        }

        fun  open(
            fragment: DefaultBaseFragment,
            clazz: Class<*>,
            requestCode:Int,
            classNameVM: String,
            designVM: String = BaseListVM::class.java.name,
            containerId: Int = R.id.containerList,
            layoutId: Int = R.layout.fragment_list_container,
            selectedId: ArrayList<Int> = ArrayList()
        ) {
            val intent = Intent(fragment.context,clazz)
            intent.putExtras( Bundle().apply {
                this.putString(LISTVM, classNameVM)
                this.putString(LISTVIEWVM, designVM)
                this.putInt(CONTAINERID, containerId)
                this.putInt(LAYOUTID, layoutId)
                this.putIntegerArrayList(SELECTEDID, selectedId)
            })
            fragment.startActivityForResult(intent,requestCode)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_list_container_multiselection
    }

    override fun onItemSelected(position: Int, model: BaseListModel) {
//        super.onItemSelected(position, selectedModel)
//        if (model.selected!!) {
//            select.isEnabled = true

        val list = listHolder?.operation?.getList()?.map { it as BaseListModel }
        select.isEnabled = list?.filter { it.selected == true }?.size!! > 0

    }

    override fun isSelected(id:Int?): Boolean {
         val ids = intent?.extras?.getIntegerArrayList(SingleSelectionList.SELECTEDID)
        val isSelected = ids?.filter { it == id }?.size?:0 > 0
        if(isSelected){
            select.isEnabled = true
        }
        return isSelected
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        select.setOnClickListener {
            val selectedList = listHolder?.operation?.getList()?.map { item -> item as BaseListModel }
                ?.filter {item-> item.selected!! }
            val itemsList:ArrayList<BaseListModel> = ArrayList()
            itemsList.addAll(selectedList!!)
            val intent = Intent()
            intent.putExtra(SingleSelectionList.ITEMDATA,itemsList)
            setResult(Activity.RESULT_OK,intent)
            finish()

        }
    }
}