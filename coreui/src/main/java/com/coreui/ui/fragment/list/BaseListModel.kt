package com.coreui.ui.fragment.list


import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.coreui.BR
import java.io.Serializable

class BaseListModel  : BaseObservable(),Serializable {
    @get:Bindable
    var id: Int? =null
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }
    @get:Bindable
    var name: String? =null
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }
    @get:Bindable
    var description: String? =null
        set(value) {
            field = value
            notifyPropertyChanged(BR.description)
        }
    @get:Bindable
    var image: String? =null
        set(value) {
            field = value
            notifyPropertyChanged(BR.image)
        }
    @get:Bindable
    var code: String? = "966 +"
        set(value) {
            field = value
            notifyPropertyChanged(BR.code)
        }
    var cost:String? = null
    @get:Bindable
    var selected: Boolean? =null
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }

    var type:Int? = null
    var types:List<Int>? = null

}