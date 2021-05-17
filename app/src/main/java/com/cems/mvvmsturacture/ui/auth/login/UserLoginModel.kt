package com.cems.mvvmsturacture.ui.auth.login


import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.cems.mvvmsturacture.BR


class UserLoginModel : BaseObservable() {
    @get:Bindable
    var email: String? =null
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
        }
    @get:Bindable
    var phone: String? =null
        set(value) {
            field = value
            notifyPropertyChanged(BR.phone)
        }
    @get:Bindable
    var password: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.password)
        }
    @get:Bindable
    var countryId: Int? =1
        set(value) {
            field = value
            notifyPropertyChanged(BR.countryId)
        }
    @get:Bindable
    var countryCode: String? = "966"
        set(value) {
            field = "$value"
            notifyPropertyChanged(BR.countryCode)
        }
    @get:Bindable
    var countryFlag: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.countryFlag)
        }

}