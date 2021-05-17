package com.trippl3dev.listlibrary.implementation


import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.trippl3dev.listlibrary.GenericListK
import java.io.Serializable

@Suppress("unused")
class PrettyList(internal val fragmentManger: FragmentManager) {
    private var listBundle: ListBundle = ListBundle()
    private var genericList = GenericListK()

    companion object {
         fun get(fragmentManager: FragmentManager): PrettyList {
            return PrettyList(fragmentManager)
        }
    }



    fun init(): ListData {
        return ListData(this)
    }

    fun addListener(callback: ListReadyCallbak){
//        listBundle.callback = callback
        genericList.setListReadyCallback(callback)

    }




    class ListData(private val listBuilder: PrettyList) {


        fun setVM(className: String): FragmentBuilder {
            this.listBuilder.listBundle.className = className
            return FragmentBuilder(listBuilder)
        }

        fun setVM(className: String, list: ArrayList<*>): FragmentBuilder {
            this.listBuilder.listBundle.className = className
            this.listBuilder.listBundle.list = list
            return FragmentBuilder(listBuilder)
        }
    }


    class FragmentBuilder(private val listBuilder: PrettyList) {
        val bundle = Bundle()
        fun putListInContainerWithId(containerId: Int): PrettyList {
            if (listBuilder.fragmentManger.findFragmentByTag("$containerId") == null) {
                bundle.putSerializable(GenericListK.dataKey, listBuilder.listBundle)
                listBuilder.genericList.arguments = bundle
                listBuilder.fragmentManger.beginTransaction().replace(containerId, listBuilder.genericList, "$containerId").commit()
            }else{
                listBuilder.genericList = listBuilder.fragmentManger.findFragmentByTag("$containerId") as GenericListK
            }
            return listBuilder
        }


    }


    interface ListReadyCallbak:Serializable{
        fun onListReady(baseListVM: RecyclerListIm?)
    }

    class ListBundle : Serializable {
         var className: String = ""
        var list: List<Any>? = null

    }
}
